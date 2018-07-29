package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.Dog;

import java.util.LinkedList;
import java.util.List;

public class DogDbManager {

    private DatabaseHelper databaseHelper;

    public DogDbManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addDog(Dog dog) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.insert(Dog.TABLE_NAME, null, dog.getContentValues());
        return row > 0;
    }

    public boolean updateDog(Dog dog) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.update(Dog.TABLE_NAME, dog.getContentValues(),
                Dog.COLUMN_ID + " = ?", new String[]{String.valueOf(dog.getId())});
        return row > 0;
    }

    public boolean deleteDog(Dog dog) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.delete(Dog.TABLE_NAME,
                Dog.COLUMN_ID + " = ? ", new String[]{String.valueOf(dog.getId())});
        return row > 0;
    }

    public List<Dog> getAllDogs() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        List<Dog> dogs = new LinkedList<>();
        Cursor c = null;
        try {
            String query = "SELECT *" +
                    " FROM " + Dog.TABLE_NAME;

            c = db.rawQuery(query, null);
            while (c.moveToNext()) {
                dogs.add(new Dog(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return dogs;
    }

    public boolean selectDog() {
        //TODO: Do I need it?
        return false;
    }
}
