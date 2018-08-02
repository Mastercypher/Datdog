package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.RemoteFriendshipTask;

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
            return updateFriendship(friendship);
        } else if (friendship.getUpdate().before(inDb.getUpdate())) {
            new RemoteFriendshipTask(ActionType.UPDATE, inDb);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateFriendship(Friendship friendship) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.update(Friendship.TABLE_NAME, friendship.getContentValues(),
                Friendship.COLUMN_ID + " = ?", new String[]{String.valueOf(friendship.getId())});
        return row > 0;
    }

    public boolean deleteFriendship(Friendship friendship) {
        return updateFriendship(friendship);
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

    public List<Friendship> getAllFriendships(int idFriend) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        List<Friendship> friendships = new LinkedList<>();
        Cursor c = null;

        try {
            String query = "SELECT * " + "FROM " + Friendship.TABLE_NAME +
                    " WHERE " + Friendship.COLUMN_ID_FRIEND + " = " + idFriend;

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

    public List<Friendship> getUserFriends(int idUser) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        List<Friendship> friendships = new LinkedList<>();
        Cursor c = null;

        try {
            String query = "SELECT * " + "FROM " + Friendship.TABLE_NAME +
                    " WHERE " + Friendship.COLUMN_ID_USER + " = " + idUser;

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
