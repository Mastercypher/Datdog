package com.mastercypher.university.mobile.datdog.trash;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mastercypher.university.mobile.datdog.Dog;
import com.mastercypher.university.mobile.datdog.User;

public class DogDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "datdog.db";
    private static final int DATABASE_VERSION = 5;

    public DogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("CODDIOOOOOOOOOOOOOOOOOOOOOOOO");
        db.execSQL(CREATE_TABLE_DOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Dog.TABLE_NAME);
        onCreate(db);
    }
}