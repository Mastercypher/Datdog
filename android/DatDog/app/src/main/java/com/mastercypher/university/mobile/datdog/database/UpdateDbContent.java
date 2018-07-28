package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mastercypher.university.mobile.datdog.AccountDirectory;
import com.mastercypher.university.mobile.datdog.DlTask;
import com.mastercypher.university.mobile.datdog.Dog;
import com.mastercypher.university.mobile.datdog.Friendship;
import com.mastercypher.university.mobile.datdog.Report;
import com.mastercypher.university.mobile.datdog.Vaccination;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UpdateDbContent extends AsyncTask<Context, Void, Void> {
    @Override
    protected Void doInBackground(Context... context) {
        //Update user dogs
        Collection<Map<String, String>> elem = null;
        try {
            elem = new DlTask().execute("http://datdog.altervista.org/dog.php?action=select&id_user_d=" +
                    AccountDirectory.getInstance().getUser().getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Iterator<Map<String, String>> it = elem.iterator();
        while (it.hasNext()) {
            try {
                new DogDbManager(context[0]).addDog(new Dog(it.next()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Update report by user
        try {
            elem = new DlTask().execute("http://datdog.altervista.org/report.php?action=select_user&id_user_r=" +
                    AccountDirectory.getInstance().getUser().getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        while (it.hasNext()) {
            try {
                new ReportDbManager(context[0]).addReport(new Report(it.next()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Update report & vaccination by dog
        List<Dog> dogs = new DogDbManager(context[0]).getAllDogs();
        Iterator<Dog> doggy = dogs.iterator();
        while (doggy.hasNext()) {
            try {
                elem = new DlTask().execute("http://datdog.altervista.org/report.php?action=select_dog&id_dog_r=" +
                        doggy.next().getId()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Collection<Map<String, String>> vaxes = null;
            try {
                vaxes = new DlTask().execute("http://datdog.altervista.org/report.php?action=select_dog&id_dog_r=" +
                        doggy.next().getId()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            it = elem.iterator();
            while (it.hasNext()) {
                try {
                    new ReportDbManager(context[0]).addReport(new Report(it.next()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Iterator<Map<String, String>> vax = vaxes.iterator();
            while (vax.hasNext()) {
                try {
                    new VaccinationDbManager(context[0]).addVaccination(new Vaccination(it.next()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        //Update friend
        try {
            elem = new DlTask().execute("http://datdog.altervista.org/friend.php?action=select&id_user_f=" +
                    AccountDirectory.getInstance().getUser().getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        while (it.hasNext()) {
            try {
                new FriendshipDbManager(context[0]).addFriendship(new Friendship(it.next()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //TODO: Update users by friendships.

        //Update Vaccination
        doggy = dogs.iterator();
        while (doggy.hasNext()) {
            try {
                elem = new DlTask().execute("http://datdog.altervista.org/report.php?action=select_dog&id_dog_r=" +
                        doggy.next().getId()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            it = elem.iterator();
            while (it.hasNext()) {
                try {
                    new ReportDbManager(context[0]).addReport(new Report(it.next()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
