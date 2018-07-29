package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mastercypher.university.mobile.datdog.R;

public class ConfirmationReportActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView dogName;
    private TextView dogBreed;
    private TextView dogColour;
    private TextView ownerName;
    private TextView ownerPhone;
    private MapView map;
    private Button confirm;
    private TextView noPerm;
    private double lat;
    private double lon;

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

        mTextMessage = (TextView) findViewById(R.id.message);
        dogName = findViewById(R.id.textView54);
        dogBreed = findViewById(R.id.textView57);
        dogColour = findViewById(R.id.textView58);
        ownerName = findViewById(R.id.textView60);
        ownerPhone = findViewById(R.id.textView62);
        map = findViewById(R.id.mapView3);
        confirm = findViewById(R.id.button16);
        noPerm = findViewById(R.id.textView21);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_missing);

        if (lat < 0 || lon < 0) {
            //TODO: What to do when the map is not used.
            noPerm.setTextColor(Color.BLACK);
            noPerm.setBackgroundColor(Color.LTGRAY);
        } else {
            map.onCreate(savedInstanceState);
            map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng target = new LatLng(lat, lon);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 14), 3000, null);
                    googleMap.addMarker(new MarkerOptions().position(target).title(dogName.getText().toString()));
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
