package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.adapter.ConnectAdapter;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConnectActivity extends AppCompatActivity {

    private ConnectAdapter mConnAdapter;
    private ListView mListView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ConnectActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(ConnectActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(ConnectActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:

                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(ConnectActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_connect);


        // ListView DOGS
        // Prepare adapter for list of dogs
        mListView = findViewById(R.id.ltv_dogs);
        ArrayList<Dog> dogs = new ArrayList<>();
        mConnAdapter = new ConnectAdapter(this, dogs);
        mListView.setAdapter(mConnAdapter);
        this.refreshConnects();

        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Dog dogClicked = (Dog) mListView.getItemAtPosition(position);
                if(dogClicked.getIdNfc().equals(UtilProj.NONE_VALUE)) {
                    Intent intent = new Intent(getBaseContext(), ConnectDogActivity.class);
                    intent.putExtra("id", dogClicked.getId());
                    startActivity(intent);
                } else {
                    // TODO go to view connected
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListView != null) {
            this.refreshConnects();
        }
    }

    private void refreshConnects() {
        User user = AccountDirectory.getInstance().getUser();

        if (user != null) {
            List<Dog> mDogsToConnect = new ArrayList<>();
            List<Dog> mDogsConnected = new ArrayList<>();
            List<Dog> mDogsFinal = new ArrayList<>();
            int sizeToConnect = 0;
            int sizeConnected = 0;
            mConnAdapter.clear();

            List<Dog> dogs = new DogDbManager(this).getAllDogs(user.getId());
            for (Dog dog : dogs) {
                // Do something
                if (dog.getIdNfc().equals(UtilProj.NONE_VALUE)) {
                    sizeToConnect++;
                } else {
                    sizeConnected++;
                }
            }
            // Set section name and size
            Dog sectionToConnect = new Dog("To connect: ", sizeToConnect);
            Dog sectionConnected = new Dog("Connected: ", sizeConnected);


            for (Dog dog : dogs) {
                // Do something
                if (dog.getIdNfc().equals(UtilProj.NONE_VALUE)) {
                    mDogsToConnect.add(dog);
                } else {
                    mDogsConnected.add(dog);
                }
            }

            mDogsFinal.add(sectionToConnect);
            mDogsFinal.addAll(mDogsToConnect);
            mDogsFinal.add(sectionConnected);
            mDogsFinal.addAll(mDogsConnected);

            mConnAdapter.addAll(mDogsFinal);

        } else {
            UtilProj.showToast(this, "User account problem, restart the application");
        }
    }

}
