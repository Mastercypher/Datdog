package com.mastercypher.university.mobile.datdog;

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
}
