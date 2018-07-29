package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mastercypher.university.mobile.datdog.AccountDirectory;
import com.mastercypher.university.mobile.datdog.DlTask;
import com.mastercypher.university.mobile.datdog.Dog;
import com.mastercypher.university.mobile.datdog.Report;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ReportDbDl {

    private Context context;

    public ReportDbDl(Context c) {
        context = c;
    }

    public void doInBackground() {
        Collection<Map<String, String>> elem = null;
        Iterator<Map<String,String>> it;
        try {
            elem = new DlTask().execute("http://datdog.altervista.org/report.php?action=select_user&id_user_r=" +
                    AccountDirectory.getInstance().getUser().getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        it = elem.iterator();
        while (it.hasNext()) {
            try {
                new ReportDbManager(context).addReport(new Report(it.next()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<Dog> dogs = new DogDbManager(context).getAllDogs();
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
            it = elem.iterator();
            while (it.hasNext()) {
                try {
                    new ReportDbManager(context).addReport(new Report(it.next()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
