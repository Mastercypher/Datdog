package com.mastercypher.university.mobile.datdog.util;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegisterTask extends AsyncTask<String, String, Boolean> {
    @Override
    protected Boolean doInBackground(String... strings) {
        System.out.println(strings[0]);
        HttpURLConnection conn = null;
        StringBuilder result = new StringBuilder();
        BufferedReader rd = null;
        Map<String,String> res = null;
        try {
            URL url = new URL(strings[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60*1000);
            conn.setReadTimeout(60*1000);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                JSONObject responseObject = new JSONObject(result.toString());
                if (responseObject.getBoolean("success")) {
                    return true;
                }
            }
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
        return false;
    }
}
