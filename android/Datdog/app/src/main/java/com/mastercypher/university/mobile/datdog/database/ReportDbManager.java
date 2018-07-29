package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.entities.Report;

import java.text.ParseException;
import java.util.Date;

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
        report.setDelete(1);
        report.setUpdate(new Date());
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
}
