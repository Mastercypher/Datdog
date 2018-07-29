package com.mastercypher.university.mobile.datdog.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mastercypher.university.mobile.datdog.AccountDirectory;
import com.mastercypher.university.mobile.datdog.DlTask;
import com.mastercypher.university.mobile.datdog.Friendship;
import com.mastercypher.university.mobile.datdog.database.FriendshipDbManager;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FriendshipDbDl {

    private Context context;

    public FriendshipDbDl(Context c) {
        context = c;
        System.out.println("friendyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
    }

    public void doInBackground() {
        Collection<Map<String, String>> elem = null;
        try {
            elem = new DlTask().execute("http://datdog.altervista.org/friend.php?action=select&id_user_f=" +
                    AccountDirectory.getInstance().getUser().getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Iterator<Map<String,String>> it;
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
