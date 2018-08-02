package com.mastercypher.university.mobile.datdog.util;

import android.os.AsyncTask;

import com.mastercypher.university.mobile.datdog.entities.Report;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class RemoteReportTask extends AsyncTask<Void, Void, Void> {

    private ActionType actionType;
    private Report report;

    public RemoteReportTask(ActionType at, Report r) {
        actionType = at;
        report = r;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        String mode = actionType == ActionType.INSERT ? "insert" : "update";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        String foundDate = report.getFound() == null ? UtilProj.NONE_VALUE : sdf.format(report.getFound());
        try {
            URL url = new URL("http://datdog.altervista.org/report.php?action=" + mode +
                    "&id=" + report.getId() +
                    "&id_user_r=" + report.getUser() +
                    "&id_dog_r=" + report.getDog() +
                    "&location_r=" + report.getLocation() +
                    "&date_create_r=" + sdf.format(report.getCreate()) +
                    "&date_update_r=" + sdf.format(report.getUpdate()) +
                    "&date_found_r=" + foundDate +
                    "&delete_r=" + report.getDelete());
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
