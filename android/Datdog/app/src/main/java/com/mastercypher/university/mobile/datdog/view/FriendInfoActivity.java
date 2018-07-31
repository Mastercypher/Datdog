package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.FriendshipDbManager;
import com.mastercypher.university.mobile.datdog.database.UserDbManager;
import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.RemoteFriendshipTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.Date;

public class FriendInfoActivity extends AppCompatActivity {

    private User mFriend;
    private Friendship mFriendship;
    private Button mBtnRemove;
    private TextView mTxvTitleName;
    private TextView mtxvName;
    private TextView mtxvSurname;
    private TextView mTxvPhone;
    private TextView mTxvBirth;
    private TextView mTxvEmail;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(FriendInfoActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(FriendInfoActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:

                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(FriendInfoActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(FriendInfoActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_friends);

        this.initComponent();
    }

    private void initComponent() {
        String idFriendship = getIntent().getStringExtra("idFriendship");
        int idFriend = getIntent().getIntExtra("idFriend", 0);
        try {
            mFriendship = new FriendshipDbManager(this).selectFriendship(idFriendship);
            mFriend = new UserDbManager(FriendInfoActivity.this).selectUser(idFriend);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mFriend != null) {
            mTxvTitleName = findViewById(R.id.txt_title_name);
            mtxvName = findViewById(R.id.txv_name);
            mtxvSurname = findViewById(R.id.txv_surname);
            mTxvPhone = findViewById(R.id.txv_phone);
            mTxvBirth = findViewById(R.id.txv_birth);
            mTxvEmail = findViewById(R.id.txv_email);
            mBtnRemove = findViewById(R.id.btn_remove);

            String nameTitle = mFriend.getName() + " " + mFriend.getSurname();
            mTxvTitleName.setText(nameTitle);
            mtxvName.setText(mFriend.getName());
            mtxvSurname.setText(mFriend.getSurname());
            mTxvPhone.setText(mFriend.getPhone());
            mTxvBirth.setText(UtilProj.formatData(mFriend.getBirth()));
            mTxvEmail.setText(mFriend.getEmail());

            mBtnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFriendship.setDelete(UtilProj.DB_ROW_DELETE);
                    mFriendship.setUpdate(new Date());
                    boolean success = new FriendshipDbManager(FriendInfoActivity.this).updateFriendship(mFriendship);
                    if (success) {
                        new RemoteFriendshipTask(ActionType.UPDATE, mFriendship).execute();
                        UtilProj.showToast(FriendInfoActivity.this, "Friendship removed");
                        finish();
                    }
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UtilProj.connectionPresent(FriendInfoActivity.this)) {
            mBtnRemove.setEnabled(false);
        } else {
            mBtnRemove.setEnabled(true);
        }
    }
}
