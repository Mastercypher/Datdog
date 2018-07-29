package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.Vaccination;

import java.text.ParseException;

public class VaccinationDbManager {

    private DatabaseHelper databaseHelper;

    public VaccinationDbManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addVaccination(Vaccination vaccination) throws ParseException {
        Vaccination inDb = selectVaccination(vaccination.getId());
        if (inDb == null) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long row = db.insert(Vaccination.TABLE_NAME, null, vaccination.getContentValues());
            return row > 0;
        } else if (vaccination.getUpdate().after(inDb.getUpdate())) {
            return updateDog(vaccination);
        } else {
            return false;
        }
    }

    public boolean updateDog(Vaccination vaccination) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.update(Vaccination.TABLE_NAME, vaccination.getContentValues(),
                Vaccination.COLUMN_ID + " = ?", new String[]{String.valueOf(vaccination.getId())});
        return row > 0;
    }

    public boolean deleteVaccination(Vaccination vaccination) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.delete(Vaccination.TABLE_NAME,
                Vaccination.COLUMN_ID + " = ? ", new String[]{String.valueOf(vaccination.getId())});
        return row > 0;
    }

    public Vaccination selectVaccination(String id) throws ParseException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c;
        String query = "SELECT *" +
                "FROM " + Vaccination.TABLE_NAME +
                " WHERE id = ?";
        c = db.rawQuery(query, new String[]{String.valueOf(id)});
        if (c.getCount() == 0) {
            return null;
        } else {
            c.moveToNext();
            return new Vaccination(c);
        }
    }
}
