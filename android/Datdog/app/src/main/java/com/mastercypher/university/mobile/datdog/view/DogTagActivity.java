package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;

public class DogTagActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(DogTagActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(DogTagActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(DogTagActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:

                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(DogTagActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_tag);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_connect);
    }
}
