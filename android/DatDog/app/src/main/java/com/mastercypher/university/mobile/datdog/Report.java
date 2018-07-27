package com.mastercypher.university.mobile.datdog;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {

    public static final String TABLE_NAME = "report";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_USER = "user";
    public static final String COLUMN_ID_DOG = "dog";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DATE_CREATE = "create";
    public static final String COLUMN_DATE_UPDATE = "update";
    public static final String COLUMN_DATE_FOUND = "found";
    public static final String COLUMN_DELETE = "delete";

    private String id;
    private String user;
    private String dog;
    private String location;
    private Date create;
    private Date update;
    private Date found;
    private int delete;

    public Report(Cursor c) throws ParseException {
        id = c.getString(c.getColumnIndex(COLUMN_ID));
        user = c.getString(c.getColumnIndex(COLUMN_ID_USER));
        dog = c.getString(c.getColumnIndex(COLUMN_ID_DOG));
        location = c.getString(c.getColumnIndex(COLUMN_LOCATION));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_CREATE)));
        update = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_UPDATE)));
        if (c.getString(c.getColumnIndex(COLUMN_DATE_FOUND)).toLowerCase().equals("null")) {
            found = null;
        } else {
            found = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_FOUND)));
        }
        delete = Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_DELETE)));
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_ID_USER, user);
        cv.put(COLUMN_ID_DOG, dog);
        cv.put(COLUMN_LOCATION, location);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        cv.put(COLUMN_DATE_CREATE, sdf.format(create));
        cv.put(COLUMN_DATE_UPDATE, sdf.format(update));
        if (found == null) {
            cv.put(COLUMN_DATE_FOUND, "null");
        } else {
            cv.put(COLUMN_DATE_FOUND, sdf.format(found));
        }
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

    public String getDog() {
        return dog;
    }

    public void setDog(String dog) {
        this.dog = dog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Date getFound() {
        return found;
    }

    public void setFound(Date found) {
        this.found = found;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }
}
