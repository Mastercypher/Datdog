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
import android.widget.Toast;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.database.VaccinationDbManager;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.entities.Vaccination;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.RemoteVaccinationTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditVaxActivity extends AppCompatActivity {

    private Dog mDog;
    private Vaccination mVax;
    private EditText mEdtName;
    private EditText mEdtDateDay;
    private EditText mEdtTime;
    private Button mBtnEdit;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(EditVaxActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(EditVaxActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(EditVaxActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(EditVaxActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
        setContentView(R.layout.activity_edit_vax);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dogs);

        this.initComponent();
    }


    private void initComponent() {
        String vaxId = getIntent().getStringExtra("id");
        try {
            mVax = new VaccinationDbManager(this).selectVaccination(vaxId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mEdtName = findViewById(R.id.edt_name);
        mEdtDateDay = findViewById(R.id.edt_date_day);
        mEdtTime = findViewById(R.id.edt_time);
        mBtnEdit = findViewById(R.id.btn_edit);

        String dateWhen = UtilProj.formatData(mVax.getWhen());
        String[] parts = dateWhen.split("-");
        String date = parts[0]; // 004
        String time = parts[1]; // 034556
        mEdtName.setText(mVax.getName());
        mEdtDateDay.setText(date);
        mEdtTime.setText(time);

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionEdit();
            }
        });
    }

    public void actionEdit() {
        if(mVax == null){
            return;
        }
        try {
            mDog = new DogDbManager(EditVaxActivity.this).selectDog(mVax.getDog());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(mDog == null) {
            return;
        }

        Map<String, String> vaxMap = new HashMap<>();
        User user = AccountDirectory.getInstance().getUser();
        String name = mEdtName.getText().toString();
        String dateDay = mEdtDateDay.getText().toString();
        String time = mEdtTime.getText().toString();

        // Data booked creation
        String bookedStr = dateDay + "-" + time;
        Date booked = UtilProj.parseDate(bookedStr);
        // Check data
        List<String> strToCheck = new ArrayList<>();
        strToCheck.add(name);
        strToCheck.add(dateDay);
        strToCheck.add(time);
        int result = UtilProj.checkValues(strToCheck);

        if (result == UtilProj.STRING_SIZE_SMALL) {
            Toast.makeText(this, "You have to fill all the fields", Toast.LENGTH_LONG).show();
        } else if (result == UtilProj.STRING_SIZE_BIG) {
            Toast.makeText(this, "All the field must be less than 50 characters", Toast.LENGTH_LONG).show();
        } else if (booked == null) {
            Toast.makeText(this, "You have to fill the time in the right format", Toast.LENGTH_LONG).show();
        } else if (result == UtilProj.STRING_SIZE_OK) {
            String dateNowStr = UtilProj.getDateNow();
            int status = UtilProj.DB_ROW_AVAILABLE;
            vaxMap.put("id", mVax.getId());
            vaxMap.put("id_dog_v", mDog.getId());
            vaxMap.put("name_v", name);
            vaxMap.put("date_when_v", bookedStr);
            vaxMap.put("date_create_v", UtilProj.formatData(mVax.getCreate()));
            vaxMap.put("date_update_v", dateNowStr);
            vaxMap.put("date_completed_v", UtilProj.NONE_VALUE);
            vaxMap.put("delete_v", mVax.getDelete() + "");

            try {
                Vaccination vaxToEdit = new Vaccination(vaxMap);
                boolean success = new VaccinationDbManager(this).addVaccination(vaxToEdit); // Sync to local
                if (success) {
                    new RemoteVaccinationTask(ActionType.UPDATE, vaxToEdit).execute(); // Sync to remote
                }

                Toast.makeText(this, name + " edited successfully", Toast.LENGTH_LONG).show();
                finish();
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, name + " not edited due to an error", Toast.LENGTH_LONG).show();
            }
        }
    }

}
