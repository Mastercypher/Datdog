package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.entities.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.text.ParseException;

public class UserDbManager {

    private DatabaseHelper databaseHelper;

    public UserDbManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addUser(User user) throws ParseException {
        User inDb = selectUser(user.getId());
        if (inDb == null) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long row = db.insert(User.TABLE_NAME, null, user.getContentValues());
            return row > 0;
        } else if (user.getUpdate().after(inDb.getUpdate())) {
            return updateUser(user);
        } else {
            return false;
        }
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.update(User.TABLE_NAME, user.getContentValues(),
                User.COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        return row > 0;
    }

    public boolean deleteUser(User user) {
        return updateUser(user);
    }

    public User selectUser(int id) throws ParseException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c;
        String query = "SELECT *" +
                "FROM " + User.TABLE_NAME +
                " WHERE id = ?";
        c = db.rawQuery(query, new String[]{String.valueOf(id)});
        if (c.getCount() == 0) {
            return null;
        } else {
            c.moveToNext();
            return new User(c);
        }
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
