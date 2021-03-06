package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;

import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.util.DlTask;
import com.mastercypher.university.mobile.datdog.entities.Dog;

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
            elem = new DlTask().execute("http://datdog.altervista.org/dog.php?action=select-user&id_user_d=" +
                    AccountDirectory.getInstance().getUser().getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(elem != null) {
            Iterator<Map<String, String>> it = elem.iterator();
            while (it.hasNext()) {
                try {
                    new DogDbManager(context).addDog(new Dog(it.next()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
