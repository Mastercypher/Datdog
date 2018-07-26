package com.mastercypher.university.mobile.datdog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnToRegister;
    private EditText loginMail;
    private EditText loginPw;
    private Map<String, String> account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnToRegister = findViewById(R.id.btnToRegister);
        loginMail = findViewById(R.id.loginMail);
        loginPw = findViewById(R.id.loginPw);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginMail.getText().toString().equals("") || loginPw.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Compile both fields.", Toast.LENGTH_LONG).show();
                } else {
                    //TODO: Check login credentials online.
                    try {
                        account = new LoginTask().execute(
                                "http://datdog.altervista.org/user.php?action=select&email_u="
                                        + loginMail.getText().toString().toLowerCase()
                                        + "&password_u=" + loginPw.getText().toString()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (account != null) {
                        try {
                            AccountDirectory.getInstance().setAccount(new Account(account));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong credentials.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
