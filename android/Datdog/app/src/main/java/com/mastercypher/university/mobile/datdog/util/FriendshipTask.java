package com.mastercypher.university.mobile.datdog.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.mastercypher.university.mobile.datdog.contract.FriendshipContract;
import com.mastercypher.university.mobile.datdog.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


public class FriendshipTask extends AsyncTask<Void, Void, Map<String, String>> {

    private static final String TAG = "UploadSchedule";
    private static final String JSON_SUCCESS = "success";
    private static final String JSON_ADD = "additional";

    private Activity mActivity;
    private FriendshipContract mListener;
    private int mIdFriend;

    public FriendshipTask(Activity activity, FriendshipContract listener, int idFriend) {
        mActivity = activity;
        mListener = listener;
        mIdFriend = idFriend;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected Map<String, String> doInBackground(Void... params) {
        Map<String, String> friendFound = null;
        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        String urlString = "http://datdog.altervista.org/user.php?action=select-user&id=" + mIdFriend;

        Log.d(TAG, "urlToConnect: " + urlString);
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(60 * 1000);
            connection.setConnectTimeout(60 * 1000);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject responseObject = new JSONObject(response.toString());
                if (responseObject.getBoolean(JSON_SUCCESS)) {
                    Log.d(TAG, "success == true");
                    JSONArray elements = responseObject.getJSONArray(this.JSON_ADD);
                    Collection<Map<String,String>> moreUsers = new HashSet<>();
                    int count = 0;
                    while (count++ < elements.length()) {
                        JSONObject elem = elements.getJSONObject(0);

                        Map<String, String> actual = new HashMap<>();

                        Iterator<String> keysItr = elem.keys();
                        while (keysItr.hasNext()) {
                            String key = keysItr.next();
                            String value = elem.getString(key);
                            actual.put(key, value);
                        }
                        moreUsers.add(actual);
                    }

                    for(Map<String, String> friend: moreUsers){
                        friendFound = friend;
                        break;
                    }

                } else {
                    Log.e(TAG, "Upload of instant feed with errors ");
                    friendFound = null;
                }
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "L'url non è formattato correttamente", e);
        } catch (IOException e) {
            Log.e(TAG, "Errore durante la connessione con il server", e);
        } catch (JSONException e) {
            Log.e(TAG, "Errore durante la deserializzazioen della risposta", e);
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (Exception ignored) {
            }
        }

        return friendFound;
    }

    @Override
    protected void onPostExecute(Map<String, String> friendFound) {
        super.onPostExecute(friendFound);
        if (friendFound == null) {
            this.showPopupError();
        } else {
            try {
                User friend = new User(friendFound);

                Log.d(TAG, "Friend accepted");
                mListener.onFinishCreation(friend);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private void showPopupError() {
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setTitle("Attention")
                .setMessage("Friendship error, check your connection and try again later")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
