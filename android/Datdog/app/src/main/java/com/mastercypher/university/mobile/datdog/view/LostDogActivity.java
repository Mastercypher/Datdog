package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.database.ReportDbManager;
import com.mastercypher.university.mobile.datdog.database.UserDbManager;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.entities.Vaccination;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.RemoteReportTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.Date;

public class LostDogActivity extends AppCompatActivity {

    private TextView mTxvStatus;
    private ConstraintLayout mCtlStatus;

    private TextView mTxvDogName;
    private TextView mTxvBreed;
    private TextView mTxvColour;
    private TextView mTxvOwnerName;
    private TextView mTxvPhone;

    private Button mBtnDelete;
    private Button mBtnFound;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(LostDogActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:

                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(LostDogActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(LostDogActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(LostDogActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_dog);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_missing);

        this.initComponent();
    }

    private void initComponent() {
        String idReport = getIntent().getStringExtra("id");
        try {
            final Report report = new ReportDbManager(this).selectReport(idReport);
            User user = new UserDbManager(this).selectUser(Integer.parseInt(report.getUser()));
            Dog dog = new DogDbManager(this).selectDog(report.getDog());

            mTxvStatus = findViewById(R.id.txv_status);
            mCtlStatus = findViewById(R.id.ctl_status);

            mTxvDogName = findViewById(R.id.txv_dog_name);
            mTxvBreed = findViewById(R.id.txv_breed);
            mTxvColour = findViewById(R.id.txv_colour);
            mTxvOwnerName = findViewById(R.id.txv_owner_name);
            mTxvPhone = findViewById(R.id.txv_phone);

            mBtnDelete = findViewById(R.id.btn_delete);
            mBtnFound = findViewById(R.id.btn_found);

            String ownerName = user.getName() + " " + user.getSurname().substring(0, 1) + ".";
            mTxvStatus.setText(Report.STATE_NOT_FOUND);
            mTxvDogName.setText(dog.getName());
            mTxvBreed.setText(dog.getBreed());
            mTxvColour.setText(dog.getColour());
            mTxvOwnerName.setText(ownerName);
            mTxvPhone.setText(user.getPhone());

            mBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    report.setDelete(UtilProj.DB_ROW_DELETE);
                    report.setUpdate(new Date());
                    boolean success = new ReportDbManager(LostDogActivity.this).updateReport(report);
                    if (success) {
                        new RemoteReportTask(ActionType.UPDATE, report).execute();
                        finish();
                    }
                }
            });

            mBtnFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    report.setDelete(UtilProj.DB_ROW_DELETE);
                    report.setUpdate(new Date());
                    boolean success = new ReportDbManager(LostDogActivity.this).updateReport(report);
                    if (success) {
                        new RemoteReportTask(ActionType.UPDATE, report).execute();

                        String state = Report.STATE_FOUND;
                        mTxvStatus.setText(state);
                        mCtlStatus.setBackgroundColor(ContextCompat.getColor(LostDogActivity.this, R.color.stateSuccess));
                        mBtnFound.setVisibility(View.GONE);
                    }
                }
            });


            if (report.getFound() != null) {
                // NOT FOUND
                String state = Report.STATE_FOUND;
                mTxvStatus.setText(state);
                mCtlStatus.setBackgroundColor(ContextCompat.getColor(LostDogActivity.this, R.color.stateSuccess));
                mBtnFound.setVisibility(View.GONE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UtilProj.connectionPresent(LostDogActivity.this)) {
            mBtnFound.setEnabled(false);
            mBtnDelete.setEnabled(false);
        } else {
            mBtnFound.setEnabled(true);
            mBtnDelete.setEnabled(true);
        }
    }
}
