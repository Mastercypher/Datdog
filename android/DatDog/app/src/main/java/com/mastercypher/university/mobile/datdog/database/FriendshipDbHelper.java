package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mastercypher.university.mobile.datdog.Dog;
import com.mastercypher.university.mobile.datdog.Friendship;

public class FriendshipDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "datdog.db";
    private static final int DATABASE_VERSION = 1;

    public FriendshipDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String CREATE_TABLE_USER = "CREATE TABLE " +
            Friendship.TABLE_NAME + " (" +
            Friendship.COLUMN_ID + " INTEGER PRIMARY KEY, " +
            Friendship.COLUMN_ID_USER + " TEXT NOT NULL, " +
            Friendship.COLUMN_ID_FRIEND + " INTEGER NOT NULL, " +
            Friendship.COLUMN_DATE_CREATE + " TEXT NOT NULL, " +
            Friendship.COLUMN_DATE_UPDATE + " TEXT NOT NULL, " +
            Friendship.COLUMN_DELETE + " INTEGER NOT NULL)";

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
