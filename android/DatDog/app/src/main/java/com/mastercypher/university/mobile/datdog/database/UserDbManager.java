package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.Dog;
import com.mastercypher.university.mobile.datdog.User;

import java.util.LinkedList;
import java.util.List;

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

    public List<User> getAllUsers() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        List<User> users = new LinkedList<>();
        Cursor c = null;
        try {
            String query = "SELECT * " + "FROM " + User.TABLE_NAME;

            c = db.rawQuery(query, null);
            while (c.moveToNext()) {
                users.add(new User(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return users;
    }
}
