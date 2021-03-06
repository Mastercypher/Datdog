package com.mastercypher.university.mobile.datdog.view;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.contract.FriendshipContract;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.FriendshipTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.nio.charset.Charset;

public class AddFriendActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, FriendshipContract {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(AddFriendActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_missing:
                    startActivity(new Intent(AddFriendActivity.this, MissingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_friends:

                    return true;
                case R.id.navigation_connect:
                    startActivity(new Intent(AddFriendActivity.this, ConnectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
                case R.id.navigation_dogs:
                    startActivity(new Intent(AddFriendActivity.this, DogsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_friends);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                processIntent(getIntent());
            }

            if (!nfcAdapter.isEnabled()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendActivity.this);
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
            nfcAdapter.setNdefPushMessageCallback(this, this);
        } catch (NullPointerException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendActivity.this);
            builder.setTitle("NFC not working");
            builder.setMessage("Your device do not support NFC feature.");
            builder.setCancelable(true);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcAdapter.disableForegroundDispatch(this);
        } catch (NullPointerException e) {
            // Nfc not supported
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = ("" + AccountDirectory.getInstance().getUser().getId());
        NdefMessage msg = new NdefMessage(
                new NdefRecord[]{createMimeRecord(
                        "application/com.example.android.beam", text.getBytes())

                });
        return msg;
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String idFriendString = new String(msg.getRecords()[0].getPayload());
        int idFriend = Integer.parseInt(idFriendString);

        new FriendshipTask(this, this, idFriend).execute();
    }

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }

    @Override
    public void onFinishCreation(User user) {
        UtilProj.showToast(this, "You have a new friend: " + user.getName());
        finish();
    }
}