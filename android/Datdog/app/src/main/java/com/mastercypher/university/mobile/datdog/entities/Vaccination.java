package com.mastercypher.university.mobile.datdog.entities;

import android.content.ContentValues;
import android.database.Cursor;

import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Vaccination {

    public static final String TABLE_NAME = "vaccination";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_DOG = "dog";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE_WHEN = "booked";
    public static final String COLUMN_DATE_CREATE = "created";
    public static final String COLUMN_DATE_UPDATE = "updated";
    public static final String COLUMN_DATE_COMPLETED = "completed";
    public static final String COLUMN_DELETE = "deleted";

    private String id;
    private String dog;
    private String name;
    private Date when;
    private Date create;
    private Date update;
    private Date completed;
    private int delete;

    public Vaccination (String nameSection, int sizeSection){
        id = UtilProj.NONE_VALUE;
        dog = UtilProj.NONE_VALUE;
        name = nameSection;
        when = null;
        create = null;
        update = null;
        delete = sizeSection;
    }

    public Vaccination(Map<String, String> vax) throws ParseException {
        id = vax.get("id");
        dog = vax.get("id_dog_v");
        name = vax.get("name_v");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        when = sdf.parse(vax.get("date_when_v"));
        create = sdf.parse(vax.get("date_create_v"));
        update = sdf.parse(vax.get("date_update_v"));
        if (vax.get("date_completed_v").equals(UtilProj.NONE_VALUE)) {
            completed = null;
        } else {
            completed = sdf.parse(vax.get("date_completed_v"));
        }
    }

    public Vaccination(Cursor c) throws ParseException {
        id = c.getString(c.getColumnIndex(COLUMN_ID));
        dog = c.getString(c.getColumnIndex(COLUMN_ID_DOG));
        name = c.getString(c.getColumnIndex(COLUMN_NAME));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        when = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_WHEN)));
        create = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_CREATE)));
        update = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_UPDATE)));
        if (c.getString(c.getColumnIndex(COLUMN_DATE_COMPLETED)).equals(UtilProj.NONE_VALUE)) {
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
            cv.put(COLUMN_DATE_COMPLETED, UtilProj.NONE_VALUE);
        } else {
            cv.put(COLUMN_DATE_COMPLETED, sdf.format(completed));
        }
        cv.put(COLUMN_DELETE, delete);
        return cv;
    }

    public static String createId(String dogId, String nameVax, String dateNow){
        return dogId + "-" + nameVax + "-" + dateNow;
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
