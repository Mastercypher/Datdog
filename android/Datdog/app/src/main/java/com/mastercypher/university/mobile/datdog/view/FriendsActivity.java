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
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        this.refreshDogs();

        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO See info friendship
                /*
                Dog dogClicked = (Dog) mListView.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), DogInfoActivity.class);
                intent.putExtra("id", dogClicked.getId());
                startActivity(intent);
                 */
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListView != null) {
            this.refreshDogs();
        }
    }

    private void refreshDogs() {
        User user = AccountDirectory.getInstance().getUser();
        if (user != null) {
            mDogAdapter.clear();
            FriendshipDbManager friendDb = new FriendshipDbManager(this);

            List<Friendship> friendships = friendDb.getAllFriendships(user.getId());
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
