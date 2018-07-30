package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Vaccination;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.RemoteVaccinationTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
        } else if (vaccination.getUpdate().before(inDb.getUpdate())) {
            new RemoteVaccinationTask(ActionType.UPDATE, inDb);
            return true;
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
        return updateDog(vaccination);
    }

    public boolean completeVaccination(Vaccination vaccination) {
        Date date = new Date();
        vaccination.setCompleted(date);
        vaccination.setUpdate(date);
        return updateDog(vaccination);
    }

    public List<Vaccination> getAllVaxs(String idDog) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        List<Vaccination> vaxs = new LinkedList<>();
        Cursor c = null;
        try {
            String query = "SELECT * " + "FROM " + Vaccination.TABLE_NAME +
                    " WHERE " + Vaccination.COLUMN_ID_DOG + " = '" + idDog + "'";

            c = db.rawQuery(query, null);
            while (c.moveToNext()) {
                Vaccination vax = new Vaccination(c);
                if (vax.getDelete() != UtilProj.DB_ROW_DELETE) {
                    vaxs.add(vax);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return vaxs;
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
