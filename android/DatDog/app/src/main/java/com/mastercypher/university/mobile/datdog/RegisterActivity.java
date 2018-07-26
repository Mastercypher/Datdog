package com.mastercypher.university.mobile.datdog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    private EditText regName;
    private EditText regSurname;
    private EditText regDate;
    private EditText regPhone;
    private EditText regMail;
    private EditText regPw;
    private EditText regPwRpt;
    private Button btnRegister;
    private Boolean account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regName = findViewById(R.id.regName);
        regSurname = findViewById(R.id.regSurname);
        regDate = findViewById(R.id.regDate);
        regPhone = findViewById(R.id.regPhone);
        regMail = findViewById(R.id.regMail);
        regPw = findViewById(R.id.regPw);
        regPwRpt = findViewById(R.id.regPwRpt);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (regName.getText().toString().equals("") || regSurname.getText().toString().equals("")
                        || regDate.getText().toString().equals("") || regPhone.getText().toString().equals("")
                        || regMail.getText().toString().equals("") || regPw.getText().toString().equals("")
                        || regPwRpt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "You must compile each field.", Toast.LENGTH_LONG).show();
                } else if (!AccountDirectory.getInstance().checkDate(regDate.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Date is not compliant to hint or representing the future.", Toast.LENGTH_LONG).show();
                } else if (regPw.getText().toString().length() < 4) {
                    Toast.makeText(getApplicationContext(), "Password too short.", Toast.LENGTH_LONG).show();
                } else if (!regPwRpt.getText().toString().equals(regPw.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords don't match.", Toast.LENGTH_LONG).show();
                } else {
                    //TODO: Check remotely if the registration could be done.
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
                        Date now = new Date();

                        account = new RegisterTask().execute(
                                "http://datdog.altervista.org/user.php?action=insert&name_u="
                                + regName.getText().toString().toLowerCase() + "&surname_u="
                                + regSurname.getText().toString().toLowerCase() + "&phone_u="
                                + regPhone.getText().toString() + "&birth_u="
                                + regDate.getText().toString() + "&email_u="
                                + regMail.getText().toString().toLowerCase() + "&password_u="
                                + regPw.getText().toString() + "&date_create_u="
                                + sdf.format(now) + "&date_update_u="
                                + sdf.format(now) + "&delete_u=0").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (account) {
                        Toast.makeText(getApplicationContext(), "Successfully registerd.", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email already used in the system.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
