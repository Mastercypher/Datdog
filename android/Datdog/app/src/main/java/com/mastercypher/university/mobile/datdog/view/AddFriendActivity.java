package com.mastercypher.university.mobile.datdog.view;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.FriendshipDbManager;
import com.mastercypher.university.mobile.datdog.database.UserDbManager;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.DlTask;
import com.mastercypher.university.mobile.datdog.util.RemoteFriendshipTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.nfc.NdefRecord.createMime;

public class AddFriendActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private TextView mTextMessage;

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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_friends);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
        nfcAdapter.setNdefPushMessage(
                createNdefMessage("" + AccountDirectory.getInstance().getUser().getId()), AddFriendActivity.this);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        /*if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            String hexdump = "";
            byte[] byteArray = intent.getByteArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            for (int i = 0; i < byteArray.length; i++) {
                String x = Integer.toHexString(((int) byteArray[i] & 0xff));
                if (x.length() == 1) {
                    x = '0' + x;
                }
                hexdump += x + ' ';
            }

            Collection<Map<String, String>> frendo = null;
            try {
                frendo = new DlTask()
                        .execute("http://datdog.altervista.org/user.php?action=select-user&id=" + hexdump).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Iterator<Map<String, String>> it = frendo.iterator();
            if (it.hasNext()) {
                try {
                    User frendy = new User(it.next());
                    new UserDbManager(AddFriendActivity.this).addUser(frendy);

                    String now = UtilProj.getDateNow();
                    Map<String, String> group = new HashMap<>();
                    group.put("id", Friendship.createId(AccountDirectory.getInstance().getUser().getId(), frendy.getId(), now));
                    group.put("id_user_f", "" + AccountDirectory.getInstance().getUser().getId());
                    group.put("id_friend_f", "" + frendy.getId());
                    group.put("date_create_f", now);
                    group.put("date_update_f", now);
                    group.put("delete_f", "0");

                    Friendship frenshy = new Friendship(group);
                    new FriendshipDbManager(AddFriendActivity.this).addFriendship(frenshy);
                    new RemoteFriendshipTask(ActionType.INSERT, frenshy).execute();
                    Toast.makeText(getApplicationContext(), "Friendship successfully added!", Toast.LENGTH_LONG).show();
                    finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AddFriendActivity.this, "User not registered.", Toast.LENGTH_LONG).show();
                finish();
            }
        }*/
    }

    public NdefMessage createNdefMessage(String id) {
        String text = (id);
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { NdefRecord.createMime("application/vnd.com.example.android.beam", text.getBytes())
                });
        return msg;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = ("" + AccountDirectory.getInstance().getUser().getId());
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMimeRecord(
                        "application/com.example.android.beam", text.getBytes())
                        /**
                         * The Android Application Record (AAR) is commented out. When a device
                         * receives a push with an AAR in it, the application specified in the AAR
                         * is guaranteed to run. The AAR overrides the tag dispatch system.
                         * You can add it back in to guarantee that this
                         * activity starts when receiving a beamed message. For now, this code
                         * uses the tag dispatch system.
                        */
                        //,NdefRecord.createApplicationRecord("com.example.android.beam")
                });
        return msg;
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        Toast.makeText(AddFriendActivity.this,(new String(msg.getRecords()[0].getPayload())), Toast.LENGTH_LONG).show();
    }

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }
}
