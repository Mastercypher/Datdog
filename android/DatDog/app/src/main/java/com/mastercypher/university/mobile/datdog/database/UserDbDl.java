package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;

import com.mastercypher.university.mobile.datdog.util.DlTask;
import com.mastercypher.university.mobile.datdog.entities.Friendship;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserDbDl {

    private Context context;

    public UserDbDl(Context c) {
        context = c;
    }

    public void doInBackground() {

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
