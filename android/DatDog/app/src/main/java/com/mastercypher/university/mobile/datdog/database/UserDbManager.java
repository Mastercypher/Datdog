package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.User;

public class UserDbManager {

    private DatabaseHelper databaseHelper;

    public UserDbManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.insert(User.TABLE_NAME, null, user.getContentValues());
        return row > 0;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.update(User.TABLE_NAME, user.getContentValues(),
                User.COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        return row > 0;
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.delete(User.TABLE_NAME,
                User.COLUMN_ID + " = ? ", new String[]{String.valueOf(user.getId())});
        return row > 0;
    }

    public boolean selectUser() {
        //TODO: Do I need it?
        return false;
    }
}
