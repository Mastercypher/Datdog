package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.adapter.DogAdapter;
import com.mastercypher.university.mobile.datdog.adapter.ReportAdapter;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.database.ReportDbManager;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private ReportAdapter mReportAdapter;
    private FloatingActionButton mBtnAddReport;
    private ListView mListView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ReportsActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:

                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(ReportsActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(ReportsActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(ReportsActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_missing);

        // Button ADD
        mBtnAddReport = findViewById(R.id.fab_add_report);
        mBtnAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsActivity.this, ReportChoseActivity.class));
            }
        });

        // ListView REPORTS
        // Prepare adapter for list of reports
        mListView = findViewById(R.id.lsvReports);
        ArrayList<Report> reportsArray = new ArrayList<>();
        mReportAdapter = new ReportAdapter(this, reportsArray);
        mListView.setAdapter(mReportAdapter);
        this.refreshDogs();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*
                Report reportClicked = (Report) mListView.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), ReportInfoActivity.class);
                intent.putExtra("id", reportClicked.getId());
                startActivity(intent);
                */
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListView != null) {
            this.refreshDogs();
        }
    }

    private void refreshDogs() {
        User user = AccountDirectory.getInstance().getUser();
        if (user != null) {
            mReportAdapter.clear();
            List<Report> reports = new ReportDbManager(this).getUserReports(user.getId());
            Iterator<Report> i = reports.iterator();
            while (i.hasNext()) {
                Report report = i.next(); // must be called before you can call i.remove()
                // Do something
                if (report.getDelete() == UtilProj.DB_ROW_DELETE) {
                    i.remove();
                }
            }
            mReportAdapter.addAll(reports);
        } else {
            UtilProj.showToast(this, "User account problem, restart the application");
        }
    }
}
