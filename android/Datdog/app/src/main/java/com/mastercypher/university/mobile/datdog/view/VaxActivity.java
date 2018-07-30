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
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.adapter.ConnectAdapter;
import com.mastercypher.university.mobile.datdog.adapter.VaccinationAdapter;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.database.VaccinationDbManager;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.entities.Vaccination;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class VaxActivity extends AppCompatActivity {

    private Dog mDog;
    private VaccinationAdapter mVaxAdapter;
    private FloatingActionButton mFabAddVaccination;
    private ListView mListView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(VaxActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(VaxActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(VaxActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(VaxActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
        setContentView(R.layout.activity_vax);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dogs);

        this.initComponent();
    }

    private void initComponent() {
        String dogId = getIntent().getStringExtra("id");
        try {
            mDog = new DogDbManager(this).selectDog(dogId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mDog != null) {
            mFabAddVaccination = findViewById(R.id.fab_add_vax);
            mFabAddVaccination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), AddVaxActivity.class);
                    intent.putExtra("id", mDog.getId());
                    startActivity(intent);
                }
            });



            // ListView FRIENDS
            // Prepare adapter for list of dogs
            mListView = findViewById(R.id.ltv_vax);
            ArrayList<Vaccination> vaxs = new ArrayList<>();
            mVaxAdapter = new VaccinationAdapter(this, vaxs);
            mListView.setAdapter(mVaxAdapter);
            this.refreshConnects();

            mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Vaccination vaxClicked = (Vaccination) mListView.getItemAtPosition(position);
                    Intent intent = new Intent(getBaseContext(), VaxStatusActivity.class);
                    intent.putExtra("id", vaxClicked.getId());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListView != null) {
            this.refreshConnects();
        }
    }

    private void refreshConnects() {
        User user = AccountDirectory.getInstance().getUser();

        if (user != null) {
            List<Vaccination> mVaxTodo = new ArrayList<>();
            List<Vaccination> mVaxDone = new ArrayList<>();
            List<Vaccination> mDogsFinal = new ArrayList<>();
            int sizeToConnect = 0;
            int sizeConnected = 0;
            mVaxAdapter.clear();

            List<Vaccination> vaxs = new VaccinationDbManager(this).getAllVaxs(mDog.getId());
            for (Vaccination vax : vaxs) {
                // Do something
                if (vax.getCompleted() == null) {
                    sizeToConnect++;
                } else {
                    sizeConnected++;
                }
            }
            // Set section name and size
            Vaccination sectionTodo = new Vaccination("Todo: ", sizeToConnect);
            Vaccination sectionDone = new Vaccination("Done: ", sizeConnected);


            for (Vaccination vax : vaxs) {
                // Do something
                if (vax.getCompleted() == null) {
                    mVaxTodo.add(vax);
                } else {
                    mVaxDone.add(vax);
                }
            }

            mDogsFinal.add(sectionTodo);
            mDogsFinal.addAll(mVaxTodo);
            mDogsFinal.add(sectionDone);
            mDogsFinal.addAll(mVaxDone);

            mVaxAdapter.addAll(mDogsFinal);

        } else {
            UtilProj.showToast(this, "User account problem, restart the application");
        }
    }

}

