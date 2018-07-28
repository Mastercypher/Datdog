package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.Dog;
import com.mastercypher.university.mobile.datdog.Friendship;
import com.mastercypher.university.mobile.datdog.User;

public class FriendshipDbManager {

    private FriendshipDbHelper friendshipDbHelper;

    public FriendshipDbManager(Context context) {
        friendshipDbHelper = new FriendshipDbHelper(context);
    }

    public boolean addFriendship(Friendship friendship) {
        SQLiteDatabase db = friendshipDbHelper.getWritableDatabase();
        long row = db.insert(Friendship.TABLE_NAME, null, friendship.getContentValues());
        return row > 0;
    }

    public boolean updateFriendship(Friendship friendship) {
        SQLiteDatabase db = friendshipDbHelper.getWritableDatabase();
        long row = db.update(Friendship.TABLE_NAME, friendship.getContentValues(),
                Friendship.COLUMN_ID + " = ?", new String[]{String.valueOf(friendship.getId())});
        return row > 0;
    }

    public boolean deleteFriendship(Friendship friendship) {
        SQLiteDatabase db = friendshipDbHelper.getWritableDatabase();
        long row = db.delete(Friendship.TABLE_NAME,
                Friendship.COLUMN_ID + " = ? ", new String[]{String.valueOf(friendship.getId())});
        return row > 0;
    }

    public boolean selectFriendship() {
        //TODO: Do I need it?
        return false;
    }
}
