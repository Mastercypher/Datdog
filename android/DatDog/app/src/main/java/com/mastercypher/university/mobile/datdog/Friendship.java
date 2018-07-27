package com.mastercypher.university.mobile.datdog;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Friendship {

    public static final String TABLE_NAME = "friendship";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_USER = "user";
    public static final String COLUMN_ID_FRIEND = "friend";
    public static final String COLUMN_DATE_CREATE = "create";
    public static final String COLUMN_DATE_UPDATE = "update";
    public static final String COLUMN_DELETE = "delete";

    private String id;
    private String user;
    private String friend;
    private Date create;
    private Date update;
    private int delete;

    public Friendship(Cursor c) throws ParseException {
        id = c.getString(c.getColumnIndex(COLUMN_ID));
        user = c.getString(c.getColumnIndex(COLUMN_ID_USER));
        friend = c.getString(c.getColumnIndex(COLUMN_ID_FRIEND));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_CREATE)));
        update = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_UPDATE)));
        delete = Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_DELETE)));
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
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