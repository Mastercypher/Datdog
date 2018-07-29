package com.mastercypher.university.mobile.datdog.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;

public class NfcReportActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView txtScan;
    private Switch swtLoc;
    private CheckBox cbPhone;
    private Button btnRepNfc;
    private LocationManager locManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(NfcReportActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:

                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(NfcReportActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(NfcReportActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(NfcReportActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_report);

        mTextMessage = (TextView) findViewById(R.id.message);
        txtScan = findViewById(R.id.txtScan);
        swtLoc = findViewById(R.id.swtLoc);
        cbPhone = findViewById(R.id.cbPhone);
        btnRepNfc = findViewById(R.id.btnRepNfc);

        btnRepNfc.setBackgroundColor(Color.parseColor("#BEBEBE"));
        btnRepNfc.setEnabled(false);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_missing);

        cbPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !txtScan.getText().toString().equals("Scanning")) {
                    btnRepNfc.setBackgroundColor(Color.parseColor("#e4af09"));
                    btnRepNfc.setEnabled(true);
                } else {
                    btnRepNfc.setBackgroundColor(Color.parseColor("#BEBEBE"));
                    btnRepNfc.setEnabled(false);
                }
            }
        });

        btnRepNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swtLoc.isChecked()) {
                    locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(NfcReportActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(NfcReportActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(NfcReportActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    } else {
                        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        System.out.println(location.getLatitude() + " --- " + location.getLongitude());
                        Intent it = new Intent(NfcReportActivity.this, ConfirmationReportActivity.class);
                        it.putExtra("lat", location.getLatitude());
                        it.putExtra("long", location.getLongitude());
                        startActivity(it);
                    }
                } else {
                    startActivity(new Intent(NfcReportActivity.this, ConfirmationReportActivity.class));
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
                    Intent it = new Intent(NfcReportActivity.this, ConfirmationReportActivity.class);
                    it.putExtra("lat", location.getLatitude());
                    it.putExtra("long", location.getLongitude());
                    startActivity(it);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(NfcReportActivity.this);
                    builder.setTitle("GPS permission not granted");
                    builder.setMessage("We will not use your location because you did not grant the permission.");
                    builder.setCancelable(true);
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            startActivity(new Intent(NfcReportActivity.this, ConfirmationReportActivity.class));
                        }
                    });
                    builder.show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!nfcAdapter.isEnabled()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(NfcReportActivity.this);
            builder.setTitle("Enable NFC");
            builder.setMessage("Your NFC is disabled. Enable it to make it work.");
            builder.setCancelable(true);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            String hexdump = "";
            byte[] byteArray = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            for (int i = 0; i < byteArray.length; i++) {
                String x = Integer.toHexString(((int) byteArray[i] & 0xff));
                if (x.length() == 1) {
                    x = '0' + x;
                }
                hexdump += x + ' ';
            }
            txtScan.setText(hexdump.replace(" ", ""));
            if (cbPhone.isChecked()) {
                btnRepNfc.setBackgroundColor(Color.parseColor("#e4af09"));
                btnRepNfc.setEnabled(true);
            }
        }
    }
}
