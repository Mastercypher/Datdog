package com.mastercypher.university.mobile.datdog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditUserActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private EditText txtname;
    private EditText txtsurname;
    private EditText birth;
    private EditText phone;
    private Button update;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(EditUserActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(EditUserActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(EditUserActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(EditUserActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        mTextMessage = (TextView) findViewById(R.id.message);
        txtname = findViewById(R.id.editText5);
        txtsurname = findViewById(R.id.editText15);
        birth = findViewById(R.id.editText16);
        phone = findViewById(R.id.editText17);
        update = findViewById(R.id.button15);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        String name = AccountDirectory.getInstance().getAccount().getName();
        String upName = name.substring(0,1).toUpperCase() + name.substring(1);
        String surname = AccountDirectory.getInstance().getAccount().getSurname();
        String upSurname = surname.substring(0,1).toUpperCase() + surname.substring(1);

        txtname.setText(upName);
        txtsurname.setText(upSurname);
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        birth.setText(sdf.format(AccountDirectory.getInstance().getAccount().getBirth()));
        phone.setText(AccountDirectory.getInstance().getAccount().getPhone());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtname.getText().toString().equals("") || txtsurname.getText().toString().equals("")
                        || birth.getText().toString().equals("") || phone.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "You must compile each field.", Toast.LENGTH_LONG).show();
                } else if (!AccountDirectory.getInstance().checkDate(birth.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Date is not compliant to hint or representing the future.", Toast.LENGTH_LONG).show();
                }
                AccountDirectory.getInstance().getAccount().setName(txtname.getText().toString().toLowerCase());
                AccountDirectory.getInstance().getAccount().setSurname(txtsurname.getText().toString().toLowerCase());
                try {
                    AccountDirectory.getInstance().getAccount().setBirth(sdf.parse(birth.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                AccountDirectory.getInstance().getAccount().setPhone(phone.getText().toString());
                onBackPressed();
            }
        });
    }
}
