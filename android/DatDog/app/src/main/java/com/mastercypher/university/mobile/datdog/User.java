package com.mastercypher.university.mobile.datdog;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class User {

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_BIRTH = "birth";
    public static final String COLUMN_MAIL = "mail";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_DATE_CREATE = "date_create";
    public static final String COLUMN_DATE_UPDATE = "date_update";
    public static final String COLUMN_DELETE = "delete";

    private String id;
    private String name;
    private String surname;
    private String phone;
    private Date birth;
    private String mail;
    private String pw;
    private Date create;
    private Date update;
    private boolean delete;

    public User(Map<String, String> res) throws ParseException {
        id = res.get("id");
        name = res.get("name_u");
        surname = res.get("surname_u");
        phone = res.get("phone_u");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        birth = sdf.parse(res.get("birth_u"));
        mail = res.get("email_u");
        pw = res.get("password_u");
        sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(res.get("date_create_u"));
        update = sdf.parse(res.get("date_update_u"));
        delete = res.get("delete_u").equals("0") ? false : true;
    }

    public User(Cursor c) throws ParseException {
        id = c.getString(c.getColumnIndex(COLUMN_ID));
        name = c.getString(c.getColumnIndex(COLUMN_NAME));
        surname = c.getString(c.getColumnIndex(COLUMN_SURNAME));
        phone = c.getString(c.getColumnIndex(COLUMN_PHONE));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        birth = sdf.parse(c.getString(c.getColumnIndex(COLUMN_BIRTH)));
        mail = c.getString(c.getColumnIndex(COLUMN_MAIL));
        pw = c.getString(c.getColumnIndex(COLUMN_PASSWORD));
        sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        create = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_CREATE)));
        update = sdf.parse(c.getString(c.getColumnIndex(COLUMN_DATE_UPDATE)));
        delete = c.getString(c.getColumnIndex(COLUMN_DELETE)).equals("0") ? false : true;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_SURNAME, surname);
        cv.put(COLUMN_PHONE, phone);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        cv.put(COLUMN_BIRTH, sdf.format(birth));
        cv.put(COLUMN_MAIL, mail);
        cv.put(COLUMN_PASSWORD, pw);
        sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        cv.put(COLUMN_DATE_CREATE, sdf.format(create));
        cv.put(COLUMN_DATE_UPDATE, sdf.format(update));
        cv.put(COLUMN_DELETE, delete ? "1" : "0");
        return cv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
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

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
