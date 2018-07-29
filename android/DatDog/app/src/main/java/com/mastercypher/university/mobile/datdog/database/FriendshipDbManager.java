package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.Friendship;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class FriendshipDbManager {

    private DatabaseHelper databaseHelper;

    public FriendshipDbManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addFriendship(Friendship friendship) throws ParseException {
        Friendship inDb = selectFriendship(friendship.getId());
        if (inDb == null) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long row = db.insert(Friendship.TABLE_NAME, null, friendship.getContentValues());
            return row > 0;
        } else if (friendship.getUpdate().after(inDb.getUpdate())) {
            return updateDog(friendship);
        } else {
            return false;
        }
    }

    public boolean updateDog(Friendship friendship) {
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

    public Friendship selectFriendship(String id) throws ParseException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c;
        String query = "SELECT *" +
                "FROM " + Friendship.TABLE_NAME +
                " WHERE id = ?";
        c = db.rawQuery(query, new String[]{String.valueOf(id)});
        if (c.getCount() == 0) {
            return null;
        } else {
            c.moveToNext();
            return new Friendship(c);
        }
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
