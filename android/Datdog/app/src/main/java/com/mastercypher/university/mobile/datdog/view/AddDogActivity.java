package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.RemoteDogTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDogActivity extends AppCompatActivity {

    private EditText mEdtName;
    private EditText mEdtBreed;
    private EditText mEdtColor;
    private EditText mEdtBirth;
    private RadioGroup mRdgSex;
    private RadioGroup mRdgSize;
    private Button mBtnAdd;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(AddDogActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(AddDogActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(AddDogActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(AddDogActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
        setContentView(R.layout.activity_add_dog);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dogs);

        this.initComponents();
    }

    private void initComponents() {
        final AddDogActivity activity = this;
        mEdtName = findViewById(R.id.edtName);
        mEdtBreed = findViewById(R.id.edtBreed);
        mEdtColor = findViewById(R.id.edtColour);
        mEdtBirth = findViewById(R.id.edtBirth);
        mRdgSex = findViewById(R.id.rbgSex);
        mRdgSize = findViewById(R.id.rbgSize);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.actionAdd();
            }
        });
    }

    private void actionAdd(){
        Map<String, String> dogMap = new HashMap<>();
        User user = AccountDirectory.getInstance().getUser();
        String name = mEdtName.getText().toString();
        String breed = mEdtBreed.getText().toString();
        String colour = mEdtColor.getText().toString();
        String birth = mEdtBirth.getText().toString();
        // Radio Group SEX
        int rdbSexId = mRdgSex.getCheckedRadioButtonId();
        RadioButton rdbSex = mRdgSex.findViewById(rdbSexId);
        int sex = mRdgSex.indexOfChild(rdbSex);
        // Radio Group SIZE
        int rdbSizeId = mRdgSize.getCheckedRadioButtonId();
        RadioButton rdbSize = mRdgSize.findViewById(rdbSizeId);
        int size = mRdgSize.indexOfChild(rdbSize);

        List<String> strToCheck = new ArrayList<>();
        strToCheck.add(name);
        strToCheck.add(breed);
        strToCheck.add(colour);
        strToCheck.add(birth);
        int result = UtilProj.checkValues(strToCheck);
        if (result == UtilProj.STRING_SIZE_SMALL){
            Toast.makeText(this, "You have to fill all the fields", Toast.LENGTH_LONG).show();
        } else if (result == UtilProj.STRING_SIZE_BIG){
            Toast.makeText(this, "All the field must be less than 50 characters", Toast.LENGTH_LONG).show();
        } else if (result == UtilProj.STRING_SIZE_OK){
            String dateNowStr = UtilProj.getDateNow();
            int status = UtilProj.DB_ROW_AVAILABLE;
            dogMap.put("id", Dog.createId(user.getId(), name, dateNowStr));
            dogMap.put("id_nfc_d", "");
            dogMap.put("id_user_d", user.getId() + "");
            dogMap.put("name_d", name);
            dogMap.put("breed_d", breed);
            dogMap.put("colour_d", colour);
            dogMap.put("birth_d", birth);
            dogMap.put("size_d", size + "");
            dogMap.put("sex_d", sex + "");
            dogMap.put("date_create_d", dateNowStr);
            dogMap.put("date_update_d", dateNowStr);
            dogMap.put("delete_d", status + "");

            try {
                Dog dogToAdd = new Dog(dogMap);
                new DogDbManager(this).addDog(dogToAdd); // Sync to local
                new RemoteDogTask(ActionType.INSERT, dogToAdd).execute(); // Sync to remote

                startActivity(new Intent(AddDogActivity.this, DogsActivity.class));
                Toast.makeText(this, name + " added successfully", Toast.LENGTH_LONG).show();
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, name + " not added due to an error", Toast.LENGTH_LONG).show();
            }
        }
    }
}
