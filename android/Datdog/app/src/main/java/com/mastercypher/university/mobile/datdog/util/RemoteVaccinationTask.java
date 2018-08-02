package com.mastercypher.university.mobile.datdog.util;

import android.os.AsyncTask;
import android.util.Log;

import com.mastercypher.university.mobile.datdog.entities.Vaccination;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class RemoteVaccinationTask extends AsyncTask<Void, Void, Void> {

    private ActionType actionType;
    private Vaccination vaccination;

    public RemoteVaccinationTask(ActionType at, Vaccination v) {
        actionType = at;
        vaccination = v;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        String mode = actionType == ActionType.INSERT ? "insert" : "update";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        String completedDate = vaccination.getCompleted() == null ? UtilProj.NONE_VALUE : sdf.format(vaccination.getCompleted());
        try {
            URL url = new URL("http://datdog.altervista.org/vaccination.php?action=" + mode +
                    "&id=" + vaccination.getId() +
                    "&id_dog_v=" + vaccination.getDog() +
                    "&name_v=" + vaccination.getName() +
                    "&date_when_v=" + sdf.format(vaccination.getWhen()) +
                    "&date_create_v=" + sdf.format(vaccination.getCreate()) +
                    "&date_update_v=" + sdf.format(vaccination.getUpdate()) +
                    "&date_completed_v=" + completedDate +
                    "&delete_v=" + vaccination.getDelete());
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
