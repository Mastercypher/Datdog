package com.mastercypher.university.mobile.datdog.view;

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

import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.util.EditUserTask;
import com.mastercypher.university.mobile.datdog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class EditUserActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private EditText txtname;
    private EditText txtsurname;
    private EditText birth;
    private EditText phone;
    private Button update;
    private boolean successful;

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

        String name = AccountDirectory.getInstance().getUser().getName();
        String upName = name.substring(0,1).toUpperCase() + name.substring(1);
        String surname = AccountDirectory.getInstance().getUser().getSurname();
        String upSurname = surname.substring(0,1).toUpperCase() + surname.substring(1);

        txtname.setText(upName);
        txtsurname.setText(upSurname);
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        birth.setText(sdf.format(AccountDirectory.getInstance().getUser().getBirth()));
        phone.setText(AccountDirectory.getInstance().getUser().getPhone());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date now = new Date();
                if(txtname.getText().toString().equals("") || txtsurname.getText().toString().equals("")
                        || birth.getText().toString().equals("") || phone.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "You must compile each field.", Toast.LENGTH_LONG).show();
                } else if (!AccountDirectory.getInstance().checkDate(birth.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Date is not compliant to hint or representing the future.", Toast.LENGTH_LONG).show();
                }
                AccountDirectory.getInstance().getUser().setName(txtname.getText().toString().toLowerCase());
                AccountDirectory.getInstance().getUser().setSurname(txtsurname.getText().toString().toLowerCase());
                try {
                    AccountDirectory.getInstance().getUser().setBirth(sdf.parse(birth.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                AccountDirectory.getInstance().getUser().setPhone(phone.getText().toString());
                AccountDirectory.getInstance().getUser().setUpdate(now);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
                    SimpleDateFormat sdfBday = new SimpleDateFormat("dd/MM/yyyy");

                    successful = new EditUserTask().execute(
                            "http://datdog.altervista.org/user.php?action=update&" +
                                    "id=" + AccountDirectory.getInstance().getUser().getId() +
                                    "&name_u=" + AccountDirectory.getInstance().getUser().getName() +
                                    "&surname_u=" + AccountDirectory.getInstance().getUser().getSurname() +
                                    "&phone_u=" + AccountDirectory.getInstance().getUser().getPhone() +
                                    "&birth_u=" + sdfBday.format(AccountDirectory.getInstance().getUser().getBirth()) +
                                    "&email_u=" + AccountDirectory.getInstance().getUser().getMail() +
                                    "&password_u=" + AccountDirectory.getInstance().getUser().getPw() +
                                    "&date_create_u=" + sdf.format(AccountDirectory.getInstance().getUser().getCreate()) +
                                    "&date_update_u=" + sdf.format(AccountDirectory.getInstance().getUser().getUpdate()) +
                                    "&delete_u=0").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (successful) {
                    Toast.makeText(getApplicationContext(), "User info edited successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Edit unsuccessful.", Toast.LENGTH_LONG).show();
                }
                onBackPressed();
            }
        });
    }
}
