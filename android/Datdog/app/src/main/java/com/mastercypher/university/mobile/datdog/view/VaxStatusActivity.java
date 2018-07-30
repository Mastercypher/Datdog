package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.VaccinationDbManager;
import com.mastercypher.university.mobile.datdog.entities.Vaccination;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;

public class VaxStatusActivity extends AppCompatActivity {

    private Vaccination mVax;
    private TextView txvName;
    private TextView txvStatus;
    private TextView txvWhen;
    private FloatingActionButton fabDone;
    private ConstraintLayout ctlStatus;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(VaxStatusActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(VaxStatusActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(VaxStatusActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(VaxStatusActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
        setContentView(R.layout.activity_vax_status);

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
        if (mVax != null) {
            txvName = findViewById(R.id.txv_name);
            txvStatus = findViewById(R.id.txv_status);
            txvWhen = findViewById(R.id.txv_when);
            fabDone = findViewById(R.id.fab_add_vax);
            ctlStatus = findViewById(R.id.ctl_status);

            if(mVax.getCompleted() != null) {
                // COMPLETED

                String state = Vaccination.SATE_COMPLETED;
                String when = UtilProj.formatData(mVax.getWhen());
                txvName.setText(mVax.getName());
                txvStatus.setText(state);
                txvWhen.setText(when);

                ctlStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.stateSuccess));
                fabDone.setVisibility(View.GONE);
            } else {
                // NOT COMPLETED

                String state = Vaccination.SATE_TODO;
                String when = UtilProj.formatData(mVax.getWhen());
                txvName.setText(mVax.getName());
                txvStatus.setText(state);
                txvWhen.setText(when);

                ctlStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.stateWarning));
                fabDone.setVisibility(View.VISIBLE);
            }
        }

    }

}
