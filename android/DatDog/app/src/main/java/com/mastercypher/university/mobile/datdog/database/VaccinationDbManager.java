package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.Vaccination;

public class VaccinationDbManager {

    private DatabaseHelper databseHelper;

    public VaccinationDbManager(Context context) {
        databseHelper = new DatabaseHelper(context);
    }

    public boolean addVaccination(Vaccination vaccination) {
        SQLiteDatabase db = databseHelper.getWritableDatabase();
        long row = db.insert(Vaccination.TABLE_NAME, null, vaccination.getContentValues());
        return row > 0;
    }

    public boolean updateVaccination(Vaccination vaccination) {
        SQLiteDatabase db = databseHelper.getWritableDatabase();
        long row = db.update(Vaccination.TABLE_NAME, vaccination.getContentValues(),
                Vaccination.COLUMN_ID + " = ?", new String[]{String.valueOf(vaccination.getId())});
        return row > 0;
    }

    public boolean deleteVaccination(Vaccination vaccination) {
        SQLiteDatabase db = databseHelper.getWritableDatabase();
        long row = db.delete(Vaccination.TABLE_NAME,
                Vaccination.COLUMN_ID + " = ? ", new String[]{String.valueOf(vaccination.getId())});
        return row > 0;
    }

    public boolean selectVaccination() {
        //TODO: Do I need it?
        return false;
    }
}
