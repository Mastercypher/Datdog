package com.mastercypher.university.mobile.datdog.util;

import android.os.AsyncTask;

import com.mastercypher.university.mobile.datdog.entities.Friendship;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class RemoteFriendshipTask extends AsyncTask<Void, Void, Void> {

    private ActionType actionType;
    private Friendship friendship;

    public RemoteFriendshipTask(ActionType at, Friendship f) {
        actionType = at;
        friendship = f;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        String mode = actionType == ActionType.INSERT ? "insert" : "update";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        try {
            URL url = new URL("http://datdog.altervista.org/friend.php?action=" + mode +
                    "&id=" + friendship.getId() +
                    "&id_user_f=" + friendship.getUser() +
                    "&id_friend_f=" + friendship.getFriend() +
                    "&date_create_f=" + sdf.format(friendship.getCreate()) +
                    "&date_update_f=" + sdf.format(friendship.getUpdate()) +
                    "&delete_f=" + friendship.getDelete());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
