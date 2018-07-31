package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.adapter.LostAdapter;
import com.mastercypher.university.mobile.datdog.adapter.ReportAdapter;
import com.mastercypher.university.mobile.datdog.database.ReportDbManager;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LostActivity extends AppCompatActivity {

    private LostAdapter mReportAdapter;
    private ListView mListView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(LostActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:

                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(LostActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(LostActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(LostActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_missing);

        // ListView REPORTS
        // Prepare adapter for list of reports
        mListView = findViewById(R.id.lsv_losts);
        ArrayList<Report> reportsArray = new ArrayList<>();
        mReportAdapter = new LostAdapter(this, reportsArray);
        mListView.setAdapter(mReportAdapter);
        this.refreshReports();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Report reportClicked = (Report) mListView.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), LostInfoActivity.class);
                intent.putExtra("id", reportClicked.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListView != null) {
            this.refreshReports();
        }
    }

    private void refreshReports() {
        User user = AccountDirectory.getInstance().getUser();

        if (user != null) {
            List<Report> mReportsFound = new ArrayList<>();
            List<Report> mReportsNotFound = new ArrayList<>();
            List<Report> mReportsFinal = new ArrayList<>();
            int sizeFound = 0;
            int sizeNotFound = 0;
            mReportAdapter.clear();

            List<Report> reports = null;
            try {
                reports = new ReportDbManager(this).getLostReport(user.getId());
                for (Report report : reports) {
                    // Do something
                    if (report.getFound() == null) {
                        sizeNotFound++;
                    } else {
                        sizeFound++;
                    }
                }
                // Set section name and size
                Report sectionFound = new Report("Found: ", sizeFound);
                Report sectionNotFound = new Report("Not found: ", sizeNotFound);

                for (Report report : reports) {
                    // Do something
                    if (report.getFound() == null) {
                        mReportsNotFound.add(report);
                    } else {
                        mReportsFound.add(report);
                    }
                }

                mReportsFinal.add(sectionNotFound);
                mReportsFinal.addAll(mReportsNotFound);
                mReportsFinal.add(sectionFound);
                mReportsFinal.addAll(mReportsFound);

                mReportAdapter.addAll(mReportsFinal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            UtilProj.showToast(this, "User account problem, restart the application");
        }
    }

}
