package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.adapter.FriendAdapter;
import com.mastercypher.university.mobile.datdog.database.FriendshipDbManager;
import com.mastercypher.university.mobile.datdog.database.UserDbManager;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.DlTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FriendsActivity extends AppCompatActivity {

    private FriendAdapter mDogAdapter;
    private FloatingActionButton mFabAddFriend;
    private ListView mListView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(FriendsActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(FriendsActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:

                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(FriendsActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(FriendsActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_friends);

        // Button ADD
        mFabAddFriend = findViewById(R.id.fab_add_friend);
        mFabAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendsActivity.this, AddFriendActivity.class));
            }
        });

        // ListView FRIENDA
        // Prepare adapter for list of dogs
        mListView = findViewById(R.id.ltv_friends);
        ArrayList<Friendship> friendsArray = new ArrayList<>();
        mDogAdapter = new FriendAdapter(this, friendsArray);
        mListView.setAdapter(mDogAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Friendship friendship = (Friendship) mListView.getItemAtPosition(position);
                try {
                    User friend = new UserDbManager(FriendsActivity.this).selectUser(friendship.getFriend());
                    if (friend == null) {
                        Collection<Map<String, String>> usr = null;
                        usr = new DlTask().execute("http://datdog.altervista.org/user.php?action=select-user&id=" + friendship.getFriend()).get();
                        Iterator<Map<String, String>> it = usr.iterator();
                        if (it.hasNext()) {
                            friend = new User(it.next());
                            new UserDbManager(FriendsActivity.this).addUser(friend);
                        }
                    }
                    Intent intent = new Intent(getBaseContext(), FriendInfoActivity.class);
                    intent.putExtra("idFriend", friend.getId());
                    intent.putExtra("idFriendship", friendship.getId());
                    startActivity(intent);
                }catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        this.refreshDogs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListView != null) {
            this.refreshDogs();
        }

        if (!UtilProj.connectionPresent(FriendsActivity.this)) {
            mFabAddFriend.setEnabled(false);
        } else {
            mFabAddFriend.setEnabled(true);
        }
    }

    private void refreshDogs() {
        User user = AccountDirectory.getInstance().getUser();
        if (user != null) {
            mDogAdapter.clear();
            FriendshipDbManager friendDb = new FriendshipDbManager(this);

            List<Friendship> friendships = friendDb.getUserFriends(user.getId());
            Iterator<Friendship> i = friendships.iterator();
            while (i.hasNext()) {
                Friendship friedFriendship = i.next(); // must be called before you can call i.remove()
                // Do something
                if (friedFriendship.getDelete() == UtilProj.DB_ROW_DELETE) {
                    i.remove();
                }
            }
            mDogAdapter.addAll(friendships);
        } else {
            UtilProj.showToast(this, "User account problem, restart the application");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FriendsActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }
}
