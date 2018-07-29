package com.mastercypher.university.mobile.datdog;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.database.DogDbDl;
import com.mastercypher.university.mobile.datdog.database.FriendshipDbDl;
import com.mastercypher.university.mobile.datdog.database.ReportDbDl;
import com.mastercypher.university.mobile.datdog.database.UserDbDl;
import com.mastercypher.university.mobile.datdog.database.VaccinationDbDl;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Button btnMissing;
    private Button btnFriends;
    private Button btnConnect;
    private Button btnDogs;
    private Button btnUserInfo;
    private Button btnSettings;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(HomeActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(HomeActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(HomeActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(HomeActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        btnMissing = findViewById(R.id.button6);
        btnFriends = findViewById(R.id.button7);
        btnConnect = findViewById(R.id.button8);
        btnDogs = findViewById(R.id.button9);
        btnUserInfo = findViewById(R.id.button11);
        btnSettings = findViewById(R.id.button12);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        btnMissing.setWidth(width / 2);
        btnMissing.setHeight(width / 2);
        btnFriends.setWidth(width / 2);
        btnFriends.setHeight(width / 2);
        btnConnect.setWidth(width / 2);
        btnConnect.setHeight(width / 2);
        btnDogs.setWidth(width / 2);
        btnDogs.setHeight(width / 2);

        btnMissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        btnDogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        btnUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserInfoActivity.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });

        new DogDbDl(HomeActivity.this).doInBackground();
        new FriendshipDbDl(HomeActivity.this).doInBackground();
        new UserDbDl(HomeActivity.this).doInBackground();
        new ReportDbDl(HomeActivity.this).doInBackground();
        new VaccinationDbDl(HomeActivity.this).doInBackground();

    }
}
