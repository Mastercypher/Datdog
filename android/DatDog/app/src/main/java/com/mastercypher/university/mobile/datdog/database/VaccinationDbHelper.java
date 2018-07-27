package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mastercypher.university.mobile.datdog.Report;
import com.mastercypher.university.mobile.datdog.Vaccination;

public class VaccinationDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "datdog.db";
    private static final int DATABASE_VERSION = 1;

    public VaccinationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String CREATE_TABLE_USER = "CREATE TABLE " +
            Vaccination.TABLE_NAME + " (" +
            Vaccination.COLUMN_ID + " TEXT PRIMARY KEY, " +
            Vaccination.COLUMN_ID_DOG + " TEXT NOT NULL, " +
            Vaccination.COLUMN_NAME + " TEXT NOT NULL, " +
            Vaccination.COLUMN_DATE_WHEN + " TEXT NOT NULL, " +
            Vaccination.COLUMN_DATE_CREATE + " TEXT NOT NULL, " +
            Vaccination.COLUMN_DATE_UPDATE + " TEXT NOT NULL, " +
            Vaccination.COLUMN_DATE_COMPLETED + " TEXT, " +
            Vaccination.COLUMN_DELETE + " INTEGER NOT NULL)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
