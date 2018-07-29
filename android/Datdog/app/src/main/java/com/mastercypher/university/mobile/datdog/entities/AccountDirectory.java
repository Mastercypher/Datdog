package com.mastercypher.university.mobile.datdog.entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountDirectory {

    private static AccountDirectory instance = null;

    private User user = null;

    private AccountDirectory() {}

    public static AccountDirectory getInstance() {
        if (instance == null) {
            instance = new AccountDirectory();
        }
        return instance;
    }

    public void setUser(User acc) {
        user = acc;
    }

    public User getUser() {
        return user;
    }

    public boolean checkDate(String strDate) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject;

        try{

            dateObject = formatter.parse(strDate);

            if (dateObject.after(new Date())) {
                return false;
            }
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
    }
}
