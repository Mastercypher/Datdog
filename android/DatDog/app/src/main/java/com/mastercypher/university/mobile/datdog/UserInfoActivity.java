package com.mastercypher.university.mobile.datdog;

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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class UserInfoActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Button btnDeleteAcc;
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
        btnDeleteAcc = findViewById(R.id.btnLogout);
        txtComplete = findViewById(R.id.txtComplete);
        txtName = findViewById(R.id.textView38);
        txtSurname = findViewById(R.id.textView39);
        txtPhone = findViewById(R.id.textView40);
        txtBirth = findViewById(R.id.textView41);
        txtEmail = findViewById(R.id.textView42);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, EditUserActivity.class));
            }
        });

        btnDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Do you really want to logout?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Date now = new Date();
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
                                                    "&date_update_u=" + sdf.format(now) +
                                                    "&delete_u=1").get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                                if (successful) {
                                    Toast.makeText(getApplicationContext(), "User deleted successfully.", Toast.LENGTH_LONG).show();
                                    AccountDirectory.getInstance().getUser().setUpdate(now);
                                    AccountDirectory.getInstance().getUser().setDelete(1);
                                    startActivity(new Intent(UserInfoActivity.this, LoginActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Deletion unsuccessful.", Toast.LENGTH_LONG).show();
                                }
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
}
