package com.mastercypher.university.mobile.datdog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccountDirectory {

    private static AccountDirectory instance = null;

    private Account account = null;

    private AccountDirectory() {}

    public static AccountDirectory getInstance() {
        if (instance == null) {
            instance = new AccountDirectory();
        }
        return instance;
    }

    public void setAccount(Account acc) {
        account = acc;
    }

    public Account getAccount() {
        return account;
    }

    public boolean checkDate(String strDate) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject;

        try{

            dateObject = formatter.parse(strDate);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);

            if (dateObject.after(today.getTime())) {
                return false;
            }
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
    }
}
