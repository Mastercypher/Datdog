package com.mastercypher.university.mobile.datdog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnToRegister;
    private EditText loginMail;
    private EditText loginPw;

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
                /*if (loginMail.getText().toString().equals("") || loginPw.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Compile both fields.", Toast.LENGTH_LONG).show();
                } else {
                    //TODO: Check login credentials online.
                    if (*//* Credentials are good *//*) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong credentials.", Toast.LENGTH_LONG).show();
                    }
                }*/
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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
