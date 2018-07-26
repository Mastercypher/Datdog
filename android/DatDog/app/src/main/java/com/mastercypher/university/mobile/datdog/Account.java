package com.mastercypher.university.mobile.datdog;

import android.provider.ContactsContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

public class Account {

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

    public Account(Map<String, String> res) throws ParseException {
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
        delete = (res.get("delete_u")).equals("0") ? false : true;
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
