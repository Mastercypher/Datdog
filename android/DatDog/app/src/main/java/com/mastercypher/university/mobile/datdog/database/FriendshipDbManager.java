package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.Friendship;

import java.util.LinkedList;
import java.util.List;

public class FriendshipDbManager {

    private DatabaseHelper databaseHelper;

    public FriendshipDbManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addFriendship(Friendship friendship) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.insert(Friendship.TABLE_NAME, null, friendship.getContentValues());
        return row > 0;
    }

    public boolean updateFriendship(Friendship friendship) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.update(Friendship.TABLE_NAME, friendship.getContentValues(),
                Friendship.COLUMN_ID + " = ?", new String[]{String.valueOf(friendship.getId())});
        return row > 0;
    }

    public boolean deleteFriendship(Friendship friendship) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.delete(Friendship.TABLE_NAME,
                Friendship.COLUMN_ID + " = ? ", new String[]{String.valueOf(friendship.getId())});
        return row > 0;
    }

    public List<Friendship> getAllFriendships() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        List<Friendship> friendships = new LinkedList<>();
        Cursor c = null;

        try {
            String query = "SELECT *" +
                    " FROM " + Friendship.TABLE_NAME;

            c = db.rawQuery(query, null);
            while (c.moveToNext()) {
                friendships.add(new Friendship(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return friendships;
    }
}
