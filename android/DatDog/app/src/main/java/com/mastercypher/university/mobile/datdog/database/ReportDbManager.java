package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.Friendship;
import com.mastercypher.university.mobile.datdog.Report;

public class ReportDbManager {

    private ReportDbHelper reportDbHelper;

    public ReportDbManager(Context context) {
        reportDbHelper = new ReportDbHelper(context);
    }

    public boolean addUser(Report report) {
        SQLiteDatabase db = reportDbHelper.getWritableDatabase();
        long row = db.insert(Report.TABLE_NAME, null, report.getContentValues());
        return row > 0;
    }

    public boolean updateUser(Report report) {
        SQLiteDatabase db = reportDbHelper.getWritableDatabase();
        long row = db.update(Report.TABLE_NAME, report.getContentValues(),
                Report.COLUMN_ID + " = ?", new String[]{String.valueOf(report.getId())});
        return row > 0;
    }

    public boolean deleteUser(Report report) {
        SQLiteDatabase db = reportDbHelper.getWritableDatabase();
        long row = db.delete(Report.TABLE_NAME,
                Report.COLUMN_ID + " = ? ", new String[]{String.valueOf(report.getId())});
        return row > 0;
    }

    public boolean selectUser() {
        //TODO: Do I need it?
        return false;
    }
}
