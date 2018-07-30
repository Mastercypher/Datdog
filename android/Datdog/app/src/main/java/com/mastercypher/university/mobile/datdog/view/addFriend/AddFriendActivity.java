package com.mastercypher.university.mobile.datdog.view.addFriend;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.UtilProj;
import com.mastercypher.university.mobile.datdog.view.ConnectActivity;
import com.mastercypher.university.mobile.datdog.view.DogsActivity;
import com.mastercypher.university.mobile.datdog.view.HomeActivity;
import com.mastercypher.university.mobile.datdog.view.MissingActivity;

public class AddFriendActivity extends AppCompatActivity implements AddFriendFragment.AddListener, CreateFriendFragment.CreateListener{

    int mIdFriend;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(AddFriendActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(AddFriendActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:

                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(AddFriendActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(AddFriendActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onFriendDetected(int idFriend) {
        mIdFriend = idFriend;
        replaceFragment(CreateFriendFragment.newInstance(), false);
    }

    @Override
    public int getIdFriend() {
        return mIdFriend;
    }

    @Override
    public void onFinishCreation(User friend) {
        UtilProj.showToast(this, "Friendship created with "+ friend.getName());
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        // Navigation
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_friends);
        // Add first activity
        addFragment(AddFriendFragment.newInstance(), false);
    }



    protected void addFragment(Fragment fragment, boolean back) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        if (back) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    protected void replaceFragment(Fragment fragment, boolean back) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (back) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
