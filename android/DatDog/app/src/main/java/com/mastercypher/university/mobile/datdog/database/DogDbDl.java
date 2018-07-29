package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mastercypher.university.mobile.datdog.AccountDirectory;
import com.mastercypher.university.mobile.datdog.DlTask;
import com.mastercypher.university.mobile.datdog.Dog;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DogDbDl {

    private Context context;

    public DogDbDl(Context c){
        context = c;
    }

    public void doInBackground() {
        Collection<Map<String, String>> elem = null;
        try {
            elem = new DlTask().execute("http://datdog.altervista.org/dog.php?action=select&id_user_d=" +
                    AccountDirectory.getInstance().getUser().getId()).get();
            if (elem!=null){
                System.out.println("not null");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Iterator<Map<String, String>> it = elem.iterator();
        while (it.hasNext()) {
            System.out.println("got next");
            try {
                new DogDbManager(context).addDog(new Dog(it.next()));
                System.out.println("Inserted");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
