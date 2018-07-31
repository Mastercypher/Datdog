package com.mastercypher.university.mobile.datdog.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.DlTask;
import com.mastercypher.university.mobile.datdog.util.RemoteReportTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ConfirmationReportActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView mTxvDogName;
    private TextView mTxvDogBreed;
    private TextView mTxvDogColour;
    private TextView mTxvOwnerName;
    private TextView mTxvOwnerPhone;
    private MapView map;
    private Button confirm;
    private TextView noPerm;
    private double lat;
    private double lon;
    private String nfc;
    private Dog dog;
    private User owner;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ConfirmationReportActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:

                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(ConfirmationReportActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(ConfirmationReportActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(ConfirmationReportActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_report);

        lat = getIntent().getDoubleExtra("lat", -1.0);
        lon = getIntent().getDoubleExtra("long", -1.0);
        nfc = getIntent().getStringExtra("nfc");

        mTextMessage = (TextView) findViewById(R.id.message);
        mTxvDogName = findViewById(R.id.txv_dog_name);
        mTxvDogBreed = findViewById(R.id.txv_breed);
        mTxvDogColour = findViewById(R.id.txv_colour);
        mTxvOwnerName = findViewById(R.id.txv_owner_name);
        mTxvOwnerPhone = findViewById(R.id.txv_phone);
        map = findViewById(R.id.mapView3);
        confirm = findViewById(R.id.button16);
        noPerm = findViewById(R.id.textView21);

        Collection<Map<String, String>> doggo = null;

        try {
            doggo = new DlTask().execute("http://datdog.altervista.org/dog.php?action=select-nfc&id_nfc_d=" + nfc).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Iterator<Map<String, String>> it = doggo.iterator();

        if (!it.hasNext()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmationReportActivity.this);
            builder.setTitle("No match");
            builder.setMessage("The NFC Tag does not belong to any dog.");
            builder.setCancelable(false);
            
            builder.setNeutralButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            builder.show();
        } else {
            try {
                dog = new Dog(it.next());
                new DogDbManager(getApplicationContext()).addDog(dog);
                doggo = new DlTask().execute("http://datdog.altervista.org/user.php?action=select-user&id=" + dog.getId_user()).get();
                it = doggo.iterator();
                owner = new User(it.next());
                new UserDbManager(getApplicationContext()).addUser(owner);

                String ownerName = owner.getName() + " " + owner.getSurname().substring(0, 1) + ".";
                mTxvDogName.setText(UtilProj.upperFirstChar(dog.getName()));
                mTxvDogBreed.setText(dog.getBreed());
                mTxvDogColour.setText(dog.getColour());
                mTxvOwnerName.setText(ownerName);
                mTxvOwnerPhone.setText(owner.getPhone());

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> reppy = new HashMap<>();
                String locat = lat == -1.0 ? UtilProj.NONE_VALUE : (lat + "---" + lon);

                String now = UtilProj.getDateNow();
                reppy.put("id", Report.createId(dog.getId(), AccountDirectory.getInstance().getUser().getId(), now));
                reppy.put("id_user_r", "" + AccountDirectory.getInstance().getUser().getId());
                reppy.put("id_dog_r", dog.getId());
                reppy.put("location_r", locat);
                reppy.put("date_create_r", now);
                reppy.put("date_update_r", now);
                reppy.put("date_found_r", UtilProj.NONE_VALUE);
                reppy.put("delete_r", "0");

                Report report;
                try {
                    report = new Report(reppy);
                    new ReportDbManager(getApplicationContext()).addReport(report);
                    new RemoteReportTask(ActionType.INSERT, report).execute();
                    Toast.makeText(getApplicationContext(), "Successfully reported!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ConfirmationReportActivity.this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_missing);

        if (lat < 0 || lon < 0) {
            //What to do when the map is not used.
            noPerm.setTextColor(Color.BLACK);
            noPerm.setBackgroundColor(Color.LTGRAY);
        } else {
            map.onCreate(savedInstanceState);
            map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng target = new LatLng(lat, lon);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 14), 3000, null);
                    googleMap.addMarker(new MarkerOptions().position(target).title(mTxvDogName.getText().toString()));
                }
            });
        }
    }

    @Override
    public void onResume() {
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
