package com.mastercypher.university.mobile.datdog.entities;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Dog {

    public static final String TABLE_NAME = "dog";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_NFC = "nfc";
    public static final String COLUMN_ID_USER = "user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BREED = "breed";
    public static final String COLUMN_COLOUR = "colour";
    public static final String COLUMN_BIRTH = "birth";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_DATE_CREATE = "created";
    public static final String COLUMN_DATE_UPDATE = "updated";
    public static final String COLUMN_DELETE = "deleted";

    public static final int SIZE_SMALL = 0;
    public static final int SIZE_BIG = 1;
    public static final int SEX_M = 0;
    public static final int SEX_F = 1;

    private String id;
    private String id_nfc;
    private int id_user;
    private String name;
    private String breed;
    private String colour;
    private Date birth;
    private int size;
    private int sex;
    private Date create;
    private Date update;
    private int delete;

    public Dog(Map<String, String> dog) throws ParseException {
        id = dog.get("id");
        id_nfc = dog.get("id_nfc_d");
        id_user = Integer.parseInt(dog.get("id_user_d"));
        name = dog.get("name_d");
        breed = dog.get("breed_d");
        colour = dog.get("colour_d");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        birth = sdf.parse(dog.get("birth_d"));
        size = Integer.parseInt(dog.get("size_d"));
        sex = Integer.parseInt(dog.get("sex_d"));
        sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(dog.get("date_create_d"));
        update = sdf.parse(dog.get("date_update_d"));
        delete = Integer.parseInt(dog.get("delete_d"));
    }

    public Dog(Cursor c) throws ParseException {
        id = c.getString(c.getColumnIndex(COLUMN_ID));
        id_nfc = c.getString(c.getColumnIndex(COLUMN_ID_NFC));
        id_user = c.getInt(c.getColumnIndex(COLUMN_ID_USER));
        name = c.getString(c.getColumnIndex(COLUMN_NAME));
        breed = c.getString(c.getColumnIndex(COLUMN_BREED));
        colour = c.getString(c.getColumnIndex(COLUMN_COLOUR));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        birth = sdf.parse(c.getString(c.getColumnIndex(COLUMN_BIRTH)));
        size = c.getInt(c.getColumnIndex(COLUMN_SIZE));
        sex = c.getInt(c.getColumnIndex(COLUMN_SEX));
        sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_CREATE)));
        update = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_UPDATE)));
        delete = c.getInt(c.getColumnIndex(COLUMN_DELETE));
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_ID_NFC, id_nfc);
        cv.put(COLUMN_ID_USER, id_user);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_BREED, breed);
        cv.put(COLUMN_COLOUR, colour);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        cv.put(COLUMN_BIRTH, sdf.format(birth));
        cv.put(COLUMN_SIZE, size);
        cv.put(COLUMN_SEX, sex);
        sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        cv.put(COLUMN_DATE_CREATE, sdf.format(create));
        cv.put(COLUMN_DATE_UPDATE, sdf.format(update));
        cv.put(COLUMN_DELETE, delete);
        return cv;
    }

    public static String createId(int userId, String nameDog, String dateNow){
        return userId + "-" + nameDog + "-" + dateNow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_nfc() {
        return id_nfc;
    }

    public void setId_nfc(String id_nfc) {
        this.id_nfc = id_nfc;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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
