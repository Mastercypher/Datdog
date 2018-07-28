package com.mastercypher.university.mobile.datdog;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Vaccination {

    public static final String TABLE_NAME = "vaccination";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_DOG = "dog";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE_WHEN = "when";
    public static final String COLUMN_DATE_CREATE = "create";
    public static final String COLUMN_DATE_UPDATE = "update";
    public static final String COLUMN_DATE_COMPLETED = "completed";
    public static final String COLUMN_DELETE = "delete";

    private String id;
    private String dog;
    private String name;
    private Date when;
    private Date create;
    private Date update;
    private Date completed;
    private int delete;

    public Vaccination(Cursor c) throws ParseException {
        id = c.getString(c.getColumnIndex(COLUMN_ID));
        dog = c.getString(c.getColumnIndex(COLUMN_ID_DOG));
        name = c.getString(c.getColumnIndex(COLUMN_NAME));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        when = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_WHEN)));
        create = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_CREATE)));
        update = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_UPDATE)));
        if (c.getString(c.getColumnIndex(COLUMN_DATE_COMPLETED)).equals("")) {
            completed = null;
        } else {
            completed = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_COMPLETED)));
        }
        delete = c.getInt(c.getColumnIndex(COLUMN_DELETE));
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_ID_DOG, dog);
        cv.put(COLUMN_NAME, name);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        cv.put(COLUMN_DATE_WHEN, sdf.format(when));
        cv.put(COLUMN_DATE_CREATE, sdf.format(create));
        cv.put(COLUMN_DATE_UPDATE, sdf.format(update));
        if (completed == null) {
            cv.put(COLUMN_DATE_COMPLETED, "");
        } else {
            cv.put(COLUMN_DATE_COMPLETED, sdf.format(completed));
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

    public String getDog() {
        return dog;
    }

    public void setDog(String dog) {
        this.dog = dog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
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

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(Date completed) {
        this.completed = completed;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }
}
