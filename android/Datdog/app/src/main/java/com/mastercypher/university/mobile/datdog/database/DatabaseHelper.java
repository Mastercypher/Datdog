package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.entities.Vaccination;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "datdog.db";
    private static final int DATABASE_VERSION = 10;

    public DatabaseHelper(Context context) {
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
            User.COLUMN_CURRENT + " INTEGER NOT NULL, " +
            User.COLUMN_DELETE + " INTEGER NOT NULL)";

    public static final String CREATE_TABLE_DOG = "CREATE TABLE " +
            Dog.TABLE_NAME + " (" +
            Dog.COLUMN_ID + " TEXT PRIMARY KEY, " +
            Dog.COLUMN_ID_NFC + " TEXT, " +
            Dog.COLUMN_ID_USER + " INTEGER NOT NULL, " +
            Dog.COLUMN_NAME + " TEXT NOT NULL, " +
            Dog.COLUMN_BREED + " TEXT NOT NULL, " +
            Dog.COLUMN_COLOUR + " TEXT NOT NULL, " +
            Dog.COLUMN_BIRTH + " TEXT NOT NULL, " +
            Dog.COLUMN_SIZE + " INTEGER NOT NULL, " +
            Dog.COLUMN_SEX + " INTEGER NOT NULL, " +
            Dog.COLUMN_DATE_CREATE + " TEXT NOT NULL, " +
            Dog.COLUMN_DATE_UPDATE + " TEXT NOT NULL, " +
            Dog.COLUMN_DELETE + " INTEGER NOT NULL)";

    public static final String CREATE_TABLE_FRIENDSHIP = "CREATE TABLE " +
            Friendship.TABLE_NAME + " (" +
            Friendship.COLUMN_ID + " TEXT PRIMARY KEY, " +
            Friendship.COLUMN_ID_USER + " INTEGER NOT NULL, " +
            Friendship.COLUMN_ID_FRIEND + " INTEGER NOT NULL, " +
            Friendship.COLUMN_DATE_CREATE + " TEXT NOT NULL, " +
            Friendship.COLUMN_DATE_UPDATE + " TEXT NOT NULL, " +
            Friendship.COLUMN_DELETE + " INTEGER NOT NULL)";

    public static final String CREATE_TABLE_REPORT = "CREATE TABLE " +
            Report.TABLE_NAME + " (" +
            Report.COLUMN_ID + " TEXT PRIMARY KEY, " +
            Report.COLUMN_ID_USER + " TEXT NOT NULL, " +
            Report.COLUMN_ID_DOG + " TEXT NOT NULL, " +
            Report.COLUMN_LOCATION + " TEXT, " +
            Report.COLUMN_DATE_CREATE + " TEXT NOT NULL, " +
            Report.COLUMN_DATE_UPDATE + " TEXT NOT NULL, " +
            Report.COLUMN_DATE_FOUND + " TEXT, " +
            Report.COLUMN_DELETE + " INTEGER NOT NULL)";

    public static final String CREATE_TABLE_VACCINATION = "CREATE TABLE " +
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
        db.execSQL(CREATE_TABLE_DOG);
        db.execSQL(CREATE_TABLE_FRIENDSHIP);
        db.execSQL(CREATE_TABLE_REPORT);
        db.execSQL(CREATE_TABLE_VACCINATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Dog.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Friendship.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Report.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Vaccination.TABLE_NAME);
        onCreate(db);
    }
}
