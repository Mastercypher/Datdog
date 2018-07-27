package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.Dog;
import com.mastercypher.university.mobile.datdog.User;

public class DogDbManager {

    private DogDbHelper dogDbHelper;

    public DogDbManager(Context context) {
        dogDbHelper = new DogDbHelper(context);
    }

    public boolean addUser(Dog dog) {
        SQLiteDatabase db = dogDbHelper.getWritableDatabase();
        long row = db.insert(User.TABLE_NAME, null, dog.getContentValues());
        return row > 0;
    }

    public boolean updateUser(Dog dog) {
        SQLiteDatabase db = dogDbHelper.getWritableDatabase();
        long row = db.update(User.TABLE_NAME, dog.getContentValues(),
                User.COLUMN_ID + " = ?", new String[]{String.valueOf(dog.getId())});
        return row > 0;
    }

    public boolean deleteUser(Dog dog) {
        SQLiteDatabase db = dogDbHelper.getWritableDatabase();
        long row = db.delete(User.TABLE_NAME,
                Dog.COLUMN_ID + " = ? ", new String[]{String.valueOf(dog.getId())});
        return row > 0;
    }

    public boolean selectUser() {
        //TODO: Do I need it?
        return false;
    }
}
