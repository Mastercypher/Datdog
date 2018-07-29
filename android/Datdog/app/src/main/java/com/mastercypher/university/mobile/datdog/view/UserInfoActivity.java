package com.mastercypher.university.mobile.datdog.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.database.UserDbManager;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.SimpleDateFormat;

public class UserInfoActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Button btnLogoutAcc;
    private Button btnEdit;
    private TextView txtComplete;
    private TextView txtName;
    private TextView txtSurname;
    private TextView txtPhone;
    private TextView txtBirth;
    private TextView txtEmail;
    private boolean successful;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(UserInfoActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(UserInfoActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(UserInfoActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(UserInfoActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        btnEdit = findViewById(R.id.btnEditAcc);
        btnLogoutAcc = findViewById(R.id.btnLogout);
        txtComplete = findViewById(R.id.txtComplete);
        txtName = findViewById(R.id.txv_breed);
        txtSurname = findViewById(R.id.txv_colour);
        txtPhone = findViewById(R.id.txv_birth);
        txtBirth = findViewById(R.id.txv_sex);
        txtEmail = findViewById(R.id.txv_size);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, EditUserActivity.class));
            }
        });

        btnLogoutAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        String name = AccountDirectory.getInstance().getUser().getName();
        String upName = name.substring(0,1).toUpperCase() + name.substring(1);
        String surname = AccountDirectory.getInstance().getUser().getSurname();
        String upSurname = surname.substring(0,1).toUpperCase() + surname.substring(1);

        txtName.setText(upName);
        txtSurname.setText(upSurname);
        txtComplete.setText(upName + " " + upSurname);
        txtPhone.setText(AccountDirectory.getInstance().getUser().getPhone());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtBirth.setText(sdf.format(AccountDirectory.getInstance().getUser().getBirth()));
        txtEmail.setText(AccountDirectory.getInstance().getUser().getMail());
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = AccountDirectory.getInstance().getUser();
        String name = user.getName();
        String upName = name.substring(0,1).toUpperCase() + name.substring(1);
        String surname = user.getSurname();
        String upSurname = surname.substring(0,1).toUpperCase() + surname.substring(1);

        txtName.setText(upName);
        txtSurname.setText(upSurname);
        txtComplete.setText(upName + " " + upSurname);
        txtPhone.setText(AccountDirectory.getInstance().getUser().getPhone());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtBirth.setText(sdf.format(AccountDirectory.getInstance().getUser().getBirth()));
        txtEmail.setText(AccountDirectory.getInstance().getUser().getMail());
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setTitle("Logout");
        builder.setMessage("Do you really want to logout?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        User userLogout = AccountDirectory.getInstance().getUser();
                        userLogout.setCurrent(UtilProj.LOGOUT);
                        new UserDbManager(getApplicationContext()).updateUser(userLogout);
                        startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }
}
