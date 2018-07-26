package com.mastercypher.university.mobile.datdog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private EditText regName;
    private EditText regSurname;
    private EditText regDate;
    private EditText regPhone;
    private EditText regMail;
    private EditText regPw;
    private EditText regPwRpt;
    private Button btnRegister;

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
                } else if (checkDate(regDate.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Date is not compliant to hint.", Toast.LENGTH_LONG).show();
                } else if (regPw.getText().toString().length() < 5) {
                    Toast.makeText(getApplicationContext(), "Password too short.", Toast.LENGTH_LONG).show();
                } else if (regPwRpt.getText().toString().equals(regPw.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords don't match.", Toast.LENGTH_LONG).show();
                } else {
                    //TODO: Check remotely if the registration could be done.
                }
            }
        });
    }



    private boolean checkDate(String strDate) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject;

        try{

            dateObject = formatter.parse(strDate);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);

            if (dateObject.after(today.getTime())) {
                return false;
            }
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
    }
}
