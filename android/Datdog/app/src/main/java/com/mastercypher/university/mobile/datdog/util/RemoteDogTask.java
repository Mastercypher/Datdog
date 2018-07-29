package com.mastercypher.university.mobile.datdog.util;

import android.os.AsyncTask;

import com.mastercypher.university.mobile.datdog.entities.Dog;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class RemoteDogTask extends AsyncTask<Void, Void, Void> {

    private ActionType actionType;
    private Dog dog;

    public RemoteDogTask(ActionType at, Dog d) {
        actionType = at;
        dog = d;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        String mode = actionType == ActionType.INSERT ? "insert" : "update";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            URL url = new URL("http://datdog.altervista.org/dog.php?action=" + mode +
                    "&id=" + dog.getId() +
                    "&id_nfc_d=" + dog.getId_nfc() +
                    "&id_user_d=" + dog.getId_user() +
                    "&name_d=" + dog.getName() +
                    "&breed_d=" + dog.getBreed() +
                    "&colour_d=" + dog.getColour() +
                    "&birth_d=" + sdf.format(dog.getBirth()) +
                    "&size_d=" + dog.getSize() +
                    "&sex_d=" + dog.getSex() +
                    "&date_create_d=" + sdf2.format(dog.getCreate()) +
                    "&date_update_d=" + sdf2.format(dog.getUpdate()) +
                    "&delete_d=" + dog.getDelete());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

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
