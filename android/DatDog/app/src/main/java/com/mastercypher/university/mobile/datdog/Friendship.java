package com.mastercypher.university.mobile.datdog;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class Friendship {

    public static final String TABLE_NAME = "friendship";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_USER = "user";
    public static final String COLUMN_ID_FRIEND = "friend";
    public static final String COLUMN_DATE_CREATE = "created";
    public static final String COLUMN_DATE_UPDATE = "updated";
    public static final String COLUMN_DELETE = "deleted";

    private String id;
    private int user;
    private int friend;
    private Date create;
    private Date update;
    private int delete;

    public Friendship(Map<String, String> friendship) throws ParseException {
        id = friendship.get("id");
        user = Integer.parseInt(friendship.get("id_user_f"));
        friend = Integer.parseInt(friendship.get("id_friend_f"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(friendship.get("date_create_f"));
        update = sdf.parse(friendship.get("date_update_f"));
        delete = Integer.parseInt(friendship.get("delte_f"));
    }

    public Friendship(Cursor c) throws ParseException {
        id = c.getString(c.getColumnIndex(COLUMN_ID));
        user = c.getInt(c.getColumnIndex(COLUMN_ID_USER));
        friend = c.getInt(c.getColumnIndex(COLUMN_ID_FRIEND));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_CREATE)));
        update = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_UPDATE)));
        delete = c.getInt(c.getColumnIndex(COLUMN_DELETE));
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_ID_USER, user);
        cv.put(COLUMN_ID_FRIEND, friend);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        cv.put(COLUMN_DATE_CREATE, sdf.format(create));
        cv.put(COLUMN_DATE_UPDATE, sdf.format(update));
        cv.put(COLUMN_DELETE, delete);
        return cv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getFriend() {
        return friend;
    }

    public void setFriend(int friend) {
        this.friend = friend;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }
}
