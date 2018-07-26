package com.mastercypher.university.mobile.datdog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class NfcReportActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView txtScan;
    private Switch swtLoc;
    private CheckBox cbPhone;
    private Button btnRepNfc;

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
                if (isChecked) {
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
                startActivity(new Intent(NfcReportActivity.this, ConfirmationReportActivity.class));
            }
        });

    }

}
