package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.RemoteReportTask;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ReportDbManager {

    private DatabaseHelper databaseHelper;

    public ReportDbManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addReport(Report report) throws ParseException {
        Report inDb = selectReport(report.getId());
        if (inDb == null) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long row = db.insert(Report.TABLE_NAME, null, report.getContentValues());
            return row > 0;
        } else if (report.getUpdate().after(inDb.getUpdate())) {
            return updateReport(report);
        } else if (report.getUpdate().before(inDb.getUpdate())) {
            new RemoteReportTask(ActionType.UPDATE, inDb);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateReport(Report report) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.update(Report.TABLE_NAME, report.getContentValues(),
                Report.COLUMN_ID + " = ?", new String[]{String.valueOf(report.getId())});
        return row > 0;
    }

    public boolean deleteReport(Report report) {
        return updateReport(report);
    }

    public boolean foundReport(Report report) {
        Date date = new Date();
        report.setFound(date);
        report.setUpdate(date);
        return updateReport(report);
    }

    public Report selectReport(String id) throws ParseException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c;
        String query = "SELECT *" +
                "FROM " + Report.TABLE_NAME +
                " WHERE id = ?";
        c = db.rawQuery(query, new String[]{String.valueOf(id)});
        if (c.getCount() == 0) {
            return null;
        } else {
            c.moveToNext();
            return new Report(c);
        }
    }


    public List<Report> getUserReports(int idUser) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        List<Report> reports = new LinkedList<>();
        Cursor c = null;

        try {
            String query = "SELECT * " + "FROM " + Report.TABLE_NAME +
                    " WHERE " + Report.COLUMN_ID_USER + " = " + idUser;

            c = db.rawQuery(query, null);
            while (c.moveToNext()) {
                reports.add(new Report(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return reports;
    }
}
