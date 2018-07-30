package com.mastercypher.university.mobile.datdog.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;

public class CodeReportActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private EditText nfc;
    private Switch swtLoc;
    private CheckBox cbPhone;
    private Button btnAdd;
    private LocationManager locManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(CodeReportActivity.this, HomeActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    return true;
                case R.id.navigation_missing:

                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(CodeReportActivity.this, FriendsActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(CodeReportActivity.this, ConnectActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(CodeReportActivity.this, DogsActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_report);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_missing);

        nfc = findViewById(R.id.txtType);
        swtLoc = findViewById(R.id.swtLocy);
        cbPhone = findViewById(R.id.cbPhoney);
        btnAdd = findViewById(R.id.btnConty);

        btnAdd.setEnabled(false);
        btnAdd.setBackgroundColor(Color.parseColor("#BEBEBE"));
        nfc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nfc.getText().toString().length() != 16) {
                    btnAdd.setBackgroundColor(Color.parseColor("#BEBEBE"));
                    btnAdd.setEnabled(false);
                } else {
                    if (cbPhone.isChecked()) {
                        btnAdd.setBackgroundColor(Color.parseColor("#e4af09"));
                        btnAdd.setEnabled(true);
                    }
                }
            }
        });

        cbPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && nfc.getText().toString().length() == 16) {
                    btnAdd.setBackgroundColor(Color.parseColor("#e4af09"));
                    btnAdd.setEnabled(true);
                } else {
                    btnAdd.setBackgroundColor(Color.parseColor("#BEBEBE"));
                    btnAdd.setEnabled(false);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swtLoc.isChecked()) {
                    locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(CodeReportActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(CodeReportActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(CodeReportActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    } else {
                        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        System.out.println(location.getLatitude() + " --- " + location.getLongitude());
                        Intent it = new Intent(CodeReportActivity.this, ConfirmationReportActivity.class);
                        it.putExtra("lat", location.getLatitude());
                        it.putExtra("long", location.getLongitude());
                        it.putExtra("nfc", nfc.getText().toString());
                        startActivity(it);
                    }
                } else {
                    startActivity(new Intent(CodeReportActivity.this, ConfirmationReportActivity.class)
                            .putExtra("nfc", nfc.getText().toString()));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //All's good, HE SAID YES!
                    @SuppressLint("MissingPermission")
                    Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Intent it = new Intent(CodeReportActivity.this, ConfirmationReportActivity.class);
                    it.putExtra("lat", location.getLatitude());
                    it.putExtra("long", location.getLongitude());
                    startActivity(it);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CodeReportActivity.this);
                    builder.setTitle("GPS permission not granted");
                    builder.setMessage("We will not use your location because you did not grant the permission.");
                    builder.setCancelable(true);
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            startActivity(new Intent(CodeReportActivity.this, ConfirmationReportActivity.class));
                        }
                    });
                    builder.show();
                }
            }
        }
    }

}
