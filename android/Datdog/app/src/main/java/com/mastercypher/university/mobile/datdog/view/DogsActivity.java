package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.adapter.DogAdapter;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DogsActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private DogAdapter mDogAdapter;
    private FloatingActionButton mBtnAddDog;
    private ListView mListView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(DogsActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(DogsActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(DogsActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(DogsActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogs);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dogs);

        // Button ADD
        mBtnAddDog = findViewById(R.id.floatingActionButton2);
        mBtnAddDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DogsActivity.this, AddDogActivity.class));
            }
        });

        // ListView DOGS
        // Prepare adapter for list of dogs
        mListView = findViewById(R.id.lsvDogs);
        ArrayList<Dog> dogsArray = new ArrayList<>();
        mDogAdapter = new DogAdapter(this, dogsArray);
        mListView.setAdapter(mDogAdapter);
        this.refreshDogs();

        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dog dogClicked = (Dog) mListView.getItemAtPosition(position);
                // TODO go to dog's view
                Intent intent = new Intent(getBaseContext(), DogInfoActivity.class);
                intent.putExtra("id", dogClicked.getId());
                startActivity(intent);
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
            DogDbManager dogDb = new DogDbManager(this);

            List<Dog> dogs = dogDb.getAllDogs(user.getId());
            Iterator<Dog> i = dogs.iterator();
            while (i.hasNext()) {
                Dog dog = i.next(); // must be called before you can call i.remove()
                // Do something
                if (dog.getDelete() == UtilProj.DB_ROW_DELETE) {
                    i.remove();
                }
            }
            mDogAdapter.addAll(dogs);
        } else {
            UtilProj.showToast(this, "User account problem, restart the application");
        }
    }
}
