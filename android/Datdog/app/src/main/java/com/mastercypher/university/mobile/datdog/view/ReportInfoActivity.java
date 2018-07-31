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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.database.ReportDbManager;
import com.mastercypher.university.mobile.datdog.database.UserDbManager;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.RemoteDogTask;
import com.mastercypher.university.mobile.datdog.util.RemoteReportTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.Date;

public class ReportInfoActivity extends AppCompatActivity {

    private TextView mTxvDogName;
    private TextView mTxvBreed;
    private TextView mTxvColour;
    private TextView mTxvOwnerName;
    private TextView mTxvPhone;
    private Button mBtnDelete;
    private MapView map;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ReportInfoActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:

                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(ReportInfoActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(ReportInfoActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(ReportInfoActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_info);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_missing);

        this.initComponent(savedInstanceState);
    }

    private void initComponent(Bundle savedInstanceState) {
        String idReport = getIntent().getStringExtra("id");
        try {
            final Report report = new ReportDbManager(this).selectReport(idReport);
            User user = new UserDbManager(this).selectUser(Integer.parseInt(report.getUser()));
            Dog dog = new DogDbManager(this).selectDog(report.getDog());

            mTxvDogName = findViewById(R.id.txv_dog_name);
            mTxvBreed = findViewById(R.id.txv_breed);
            mTxvColour = findViewById(R.id.txv_colour);
            mTxvOwnerName = findViewById(R.id.txv_owner_name);
            mTxvPhone = findViewById(R.id.txv_phone);
            mBtnDelete = findViewById(R.id.btn_delete);
            map = findViewById(R.id.mapView3);

            String ownerName = user.getName() + " " + user.getSurname().substring(0, 1) + ".";
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
                    boolean success = new ReportDbManager(ReportInfoActivity.this).updateReport(report);
                    if(success) {
                        new RemoteReportTask(ActionType.UPDATE, report).execute();
                        finish();
                    }
                }
            });

            final String[] dims = report.getLocation().split("---");

            map.onCreate(savedInstanceState);
            map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng target = new LatLng(Double.parseDouble(dims[0]), Double.parseDouble(dims[1]));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 14), 3000, null);
                    googleMap.addMarker(new MarkerOptions().position(target).title(mTxvDogName.getText().toString()));
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UtilProj.connectionPresent(ReportInfoActivity.this)) {
            mBtnDelete.setEnabled(false);
        } else {
            mBtnDelete.setEnabled(true);
        }
        try {
            map.onResume();
        } catch (Exception e) {};
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            map.onPause();
        } catch (Exception e) {};
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            map.onDestroy();
        } catch (Exception e) {};
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            map.onLowMemory();
        } catch (Exception e) {};
    }
}
