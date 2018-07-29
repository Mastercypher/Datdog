package com.mastercypher.university.mobile.datdog.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    Button mBtnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnContinue = findViewById(R.id.button);

        new DatabaseHelper(this).getWritableDatabase();

        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }


}
