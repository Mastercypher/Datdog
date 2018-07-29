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

    private Button btnRemove;
    private Button btnEdit;
    private TextView mTextMessage;
    private TextView txvTitleName;
    private TextView txvBreed;
    private TextView txtColour;
    private TextView txtBirth;
    private TextView txtSex;
    private TextView txtSize;
    private RelativeLayout rtlVaccination;

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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dogs);

        this.initComponent();
    }


    private void initComponent(){
        String dogId = getIntent().getStringExtra("id");
        Dog dog = null;
        try {
            dog = new DogDbManager(this).selectDog(dogId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dog != null) {
            final Dog finalDog = dog;
            txvTitleName = findViewById(R.id.txv_title_name);
            txvBreed = findViewById(R.id.txv_breed);
            txtColour = findViewById(R.id.txv_colour);
            txtBirth = findViewById(R.id.txv_birth);
            txtSex = findViewById(R.id.txv_sex);
            txtSize = findViewById(R.id.txv_size);
            rtlVaccination = findViewById(R.id.rtl_vax);
            btnEdit = findViewById(R.id.btn_edit);
            btnRemove = findViewById(R.id.btn_remove);

            txvTitleName.setText(dog.getName());
            txvBreed.setText(dog.getBreed());
            txtColour.setText(dog.getColour());
            txtBirth.setText(UtilProj.formatDataNoTime(dog.getBirth()));
            txtSex.setText(dog.getSex() == Dog.SEX_F ? "Male" : "Female");
            txtSize.setText(dog.getSize() == Dog.SIZE_SMALL ? "Small" : "Big");

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DogDbManager(DogInfoActivity.this).deleteDog(finalDog);
                    startActivity(new Intent(DogInfoActivity.this, DogsActivity.class));
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DogInfoActivity.this, EditDogActivity.class));
                }
            });
        }
    }


}
