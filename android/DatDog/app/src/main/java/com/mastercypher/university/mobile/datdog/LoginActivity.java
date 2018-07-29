package com.mastercypher.university.mobile.datdog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mastercypher.university.mobile.datdog.database.UserDbManager;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnToRegister;
    private EditText loginMail;
    private EditText loginPw;
    private Map<String, String> account;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        btnLogin = findViewById(R.id.btnLogin);
        btnToRegister = findViewById(R.id.btnToRegister);
        loginMail = findViewById(R.id.loginMail);
        loginPw = findViewById(R.id.loginPw);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userLogin = null;
                if (loginMail.getText().toString().equals("") || loginPw.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Compile both fields.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        account = new LoginTask().execute(
                                "http://datdog.altervista.org/user.php?action=select-login&email_u="
                                        + loginMail.getText().toString().toLowerCase()
                                        + "&password_u=" + loginPw.getText().toString()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (account != null) {
                        try {
                            userLogin = new User(account);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        AccountDirectory.getInstance().setUser(userLogin);
                        new UserDbManager(mContext).addUser(userLogin);
                        //TODO: Task with aim to download all db related to the user logged in.

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Email or password wrong", Toast.LENGTH_LONG).show();
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
