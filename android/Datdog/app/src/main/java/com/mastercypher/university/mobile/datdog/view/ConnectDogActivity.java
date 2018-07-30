package com.mastercypher.university.mobile.datdog.view;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.DlTask;
import com.mastercypher.university.mobile.datdog.util.RemoteDogTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ConnectDogActivity extends AppCompatActivity {

    private Dog mDog;
    private TextView mTxvName;
    private Button mBtnSeeDog;
    private String dogId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ConnectDogActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(ConnectDogActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:
                    startActivity(new Intent(ConnectDogActivity.this, FriendsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_connect:

                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(ConnectDogActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_dog);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_connect);

        this.initComponent();
    }

    private void initComponent(){
        dogId = getIntent().getStringExtra("id");
        try {
            mDog = new DogDbManager(this).selectDog(dogId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mDog != null) {
            mTxvName = findViewById(R.id.txv_title_name);
            mBtnSeeDog = findViewById(R.id.btn_see_dog);

            mTxvName.setText(mDog.getName());
            mBtnSeeDog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DogInfoActivity.class);
                    intent.putExtra("id", mDog.getId());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!nfcAdapter.isEnabled()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ConnectDogActivity.this);
            builder.setTitle("Enable NFC");
            builder.setMessage("Your NFC is disabled. Enable it to make it work.");
            builder.setCancelable(true);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            String hexdump = "";
            byte[] byteArray = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            for (int i = 0; i < byteArray.length; i++) {
                String x = Integer.toHexString(((int) byteArray[i] & 0xff));
                if (x.length() == 1) {
                    x = '0' + x;
                }
                hexdump += x + ' ';
            }

            try {
                Collection<Map<String, String>> isDog;
                isDog = new DlTask().execute("http://datdog.altervista.org/dog.php?action=select-nfc&id_nfc_d="
                        + hexdump.replace(" ", "")).get();

                Iterator<Map<String, String>> it = isDog.iterator();
                if (it.hasNext()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ConnectDogActivity.this);
                    builder.setTitle("NFC already used");
                    builder.setMessage("Your NFC Tag belongs to another dog yet.");
                    builder.setCancelable(true);
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    DogDbManager dogDbManager = new DogDbManager(ConnectDogActivity.this);
                    Dog doggo = dogDbManager.selectDog(dogId);
                    doggo.setId_nfc(hexdump.replace(" ", ""));
                    doggo.setUpdate(new Date());
                    dogDbManager.updateDog(doggo);
                    new RemoteDogTask(ActionType.UPDATE, doggo).execute();
                    finish();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
