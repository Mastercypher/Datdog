package com.mastercypher.university.mobile.datdog.entities;

import android.content.ContentValues;
import android.database.Cursor;

import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Report {

    public static final String TABLE_NAME = "report";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_USER = "user";
    public static final String COLUMN_ID_DOG = "dog";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DATE_CREATE = "created";
    public static final String COLUMN_DATE_UPDATE = "updated";
    public static final String COLUMN_DATE_FOUND = "found";
    public static final String COLUMN_DELETE = "deleted";

    public static final String STATE_FOUND = "Found";
    public static final String STATE_NOT_FOUND = "Not found";

    public static final String STATUS_FOUND = "found";
    public static final String STATUS_NOT_FOUND = "not found";

    private String id;
    private String user;
    private String dog;
    private String location;
    private Date create;
    private Date update;
    private Date found;
    private int delete;

    public Report(String titleSection, int size) {
        id = "";
        user = "";
        dog = "";
        location = titleSection;
        create = null;
        update = null;
        found = null;
        delete = size;
    }

    public Report(Map<String, String> report) throws ParseException {
        id = report.get("id");
        user = report.get("id_user_r");
        dog = report.get("id_dog_r");
        location = report.get("location_r");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(report.get("date_create_r"));
        update = sdf.parse(report.get("date_update_r"));
        if (report.get("date_found_r").equals(UtilProj.NONE_VALUE)) {
            found = null;
        } else {
            found = sdf.parse(report.get("date_found_r"));
        }
        delete = Integer.parseInt(report.get("delete_r"));
    }

    public Report(Cursor c) throws ParseException {
        id = c.getString(c.getColumnIndex(COLUMN_ID));
        user = c.getString(c.getColumnIndex(COLUMN_ID_USER));
        dog = c.getString(c.getColumnIndex(COLUMN_ID_DOG));
        location = c.getString(c.getColumnIndex(COLUMN_LOCATION));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_CREATE)));
        update = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_UPDATE)));
        if (c.getString(c.getColumnIndex(COLUMN_DATE_FOUND)).equals(UtilProj.NONE_VALUE)) {
            found = null;
        } else {
            found = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_FOUND)));
        }
        delete = c.getInt(c.getColumnIndex(COLUMN_DELETE));
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
            cv.put(COLUMN_DATE_FOUND, UtilProj.NONE_VALUE);
        } else {
            cv.put(COLUMN_DATE_FOUND, sdf.format(found));
        }
        cv.put(COLUMN_DELETE, delete);
        return cv;
    }

    public static String createId(String dogId, int idReporter, String dateNow){
        return dogId + "-" + idReporter + "-" + dateNow;
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
