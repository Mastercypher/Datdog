package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mastercypher.university.mobile.datdog.User;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "datdog.db";
    private static final int DATABASE_VERSION = 1;

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String CREATE_TABLE_USER = "CREATE TABLE " +
            User.TABLE_NAME + " (" +
            User.COLUMN_ID + " INTEGER PRIMARY KEY, " +
            User.COLUMN_NAME + " TEXT NOT NULL, " +
            User.COLUMN_SURNAME + " TEXT NOT NULL, " +
            User.COLUMN_PHONE + " TEXT NOT NULL, " +
            User.COLUMN_BIRTH + " TEXT NOT NULL, " +
            User.COLUMN_MAIL + " TEXT NOT NULL UNIQUE, " +
            User.COLUMN_PASSWORD + " TEXT NOT NULL, " +
            User.COLUMN_DATE_CREATE + " TEXT NOT NULL, " +
            User.COLUMN_DATE_UPDATE + " TEXT NOT NULL, " +
            User.COLUMN_DELETE + " INTEGER NOT NULL)";

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
