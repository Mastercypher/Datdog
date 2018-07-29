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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserDbDl {

    private Context context;

    public UserDbDl(Context c) {
        context = c;
        System.out.println("usryyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
    }

    public void doInBackground() {
        System.out.println("not null");

        Collection<Map<String, String>> elem = null;
        Iterator<Map<String,String>> it;
        List<Friendship> friendships = new FriendshipDbManager(context).getAllFriendships();
        Iterator<Friendship> frey = friendships.iterator();
        while (frey.hasNext()) {
            try {
                elem = new DlTask().execute("http://datdog.altervista.org/user.php?action=select-user&id="
                        + frey.next().getFriend()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            it = elem.iterator();
            while (it.hasNext()) {
                try {
                    new FriendshipDbManager(context).addFriendship(new Friendship(it.next()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
