package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.entities.Dog;

import java.text.ParseException;

public class ConnectDogActivity extends AppCompatActivity {

    private Dog mDog;
    private TextView mTxvName;
    private Button mBtnSeeDog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ConnectDogActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(ConnectDogActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(ConnectDogActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:

                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(ConnectDogActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_dog);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_connect);

        this.initComponent();
    }

    private void initComponent(){
        String dogId = getIntent().getStringExtra("id");
        try {
            mDog = new DogDbManager(this).selectDog(dogId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mDog != null) {
            mTxvName = findViewById(R.id.txv_title_name);
            mBtnSeeDog = findViewById(R.id.btn_see_dog);

            mTxvName.setText(mDog.getName());
            mBtnSeeDog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DogInfoActivity.class);
                    intent.putExtra("id", mDog.getId());
                    startActivity(intent);
                }
            });
        }
    }
}
