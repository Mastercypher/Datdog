package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DogDbManager {

    private DatabaseHelper databaseHelper;

    public DogDbManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addDog(Dog dog) throws ParseException {
        Dog inDb = selectDog(dog.getId());
        if (inDb == null) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long row = db.insert(Dog.TABLE_NAME, null, dog.getContentValues());
            return row > 0;
        } else if (dog.getUpdate().after(inDb.getUpdate())) {
            return updateDog(dog);
        } else {
            return false;
        }
    }

    public boolean updateDog(Dog dog) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.update(Dog.TABLE_NAME, dog.getContentValues(),
                    Dog.COLUMN_ID + " = ?", new String[]{String.valueOf(dog.getId())});
        return row > 0;
    }

    public List<Dog> getAllDogs(int idUser) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        List<Dog> dogs = new LinkedList<>();
        Cursor c = null;
        try {
            String query = "SELECT * " + "FROM " + Dog.TABLE_NAME+
                    " WHERE user = " + idUser;

            c = db.rawQuery(query, null);
            while (c.moveToNext()) {
                Dog dog = new Dog(c);
                if(dog.getDelete() != UtilProj.DB_ROW_DELETE) {
                    dogs.add(dog);
                }
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

    public Dog selectDog(String id_dog) throws ParseException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c;
        String query = "SELECT *" +
                "FROM " + Dog.TABLE_NAME +
                " WHERE id = ?";
         c = db.rawQuery(query, new String[]{String.valueOf(id_dog)});
         if (c.getCount() == 0) {
             return null;
         } else {
             c.moveToNext();
             return new Dog(c);
         }
    }
}
