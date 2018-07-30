package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;

public class DogInfoActivity extends AppCompatActivity {

    private Dog mDog;
    private Button mBtnRemove;
    private Button mBtnEdit;
    private TextView mTxvTitleName;
    private TextView mTxvBreed;
    private TextView mTxtColour;
    private TextView mTxtBirth;
    private TextView mTxtSex;
    private TextView mTxtSize;
    private RelativeLayout mRtlVaccination;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(DogInfoActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(DogInfoActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(DogInfoActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(DogInfoActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
        setContentView(R.layout.activity_dog_info);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dogs);

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
            final Dog finalDog = mDog;
            mTxvTitleName = findViewById(R.id.txv_title_name);
            mTxvBreed = findViewById(R.id.txv_breed);
            mTxtColour = findViewById(R.id.txv_colour);
            mTxtBirth = findViewById(R.id.txv_birth);
            mTxtSex = findViewById(R.id.txv_sex);
            mTxtSize = findViewById(R.id.txv_size);
            mRtlVaccination = findViewById(R.id.rtl_vax);
            mBtnEdit = findViewById(R.id.btn_edit);
            mBtnRemove = findViewById(R.id.btn_remove);

            mTxvTitleName.setText(mDog.getName());
            mTxvBreed.setText(mDog.getBreed());
            mTxtColour.setText(mDog.getColour());
            mTxtBirth.setText(UtilProj.formatDataNoTime(mDog.getBirth()));
            mTxtSex.setText(mDog.getSex() == Dog.SEX_M ? "Male" : "Female");
            mTxtSize.setText(mDog.getSize() == Dog.SIZE_SMALL ? "Small" : "Big");

            mBtnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DogDbManager(DogInfoActivity.this).deleteDog(finalDog);
                    startActivity(new Intent(DogInfoActivity.this, DogInfoActivity.class));
                }
            });

            mBtnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), EditDogActivity.class);
                    intent.putExtra("id", finalDog.getId());
                    startActivity(intent);
                }
            });
        }
    }


}
