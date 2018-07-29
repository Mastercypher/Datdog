package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mastercypher.university.mobile.datdog.DlTask;
import com.mastercypher.university.mobile.datdog.Dog;
import com.mastercypher.university.mobile.datdog.Vaccination;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class VaccinationDbDl {

    private Context context;

    public VaccinationDbDl(Context c) {
        context = c;
    }

    public void doInBackground() {
        List<Dog> dogs = new DogDbManager(context).getAllDogs();
        Iterator<Dog> doggy = dogs.iterator();
        while (doggy.hasNext()) {
            Collection<Map<String, String>> vaxes = null;
            try {
                vaxes = new DlTask().execute("http://datdog.altervista.org/vaccination.php?action=select&id_dog_v=" +
                        doggy.next().getId()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Iterator<Map<String, String>> vax = vaxes.iterator();
            while (vax.hasNext()) {
                try {
                    new VaccinationDbManager(context).addVaccination(new Vaccination(vax.next()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
