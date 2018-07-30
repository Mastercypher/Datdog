package com.mastercypher.university.mobile.datdog.view.addFriend;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

public class AddFriendFragment extends Fragment implements NfcAdapter.CreateNdefMessageCallback {

    private final static String TAG = "AddFriendFragment";

    private AddListener mListener;
    private Activity mActivity = null;
    private TextView mTextMessage;

    public interface AddListener {
        void onFriendDetected(int idFriend);
    }

    public AddFriendFragment() {
    }

    public static AddFriendFragment newInstance() {
        return new AddFriendFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_friend, container, false);
        mActivity = getActivity();

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddListener) {
            mListener = (AddListener) context;
        } else {
            Log.e("DownloadFragment", "not possible to initialize the listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getActivity().getIntent().getAction())) {
            try {
                processIntent(getActivity().getIntent());
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nfcAdapter.setNdefPushMessage(
                createNdefMessage("" + AccountDirectory.getInstance().getUser().getId()), getActivity());
        if (!nfcAdapter.isEnabled()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0,
                new Intent(getActivity(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(getActivity(), pendingIntent, null, null);
        nfcAdapter.setNdefPushMessageCallback(this, getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        nfcAdapter.disableForegroundDispatch(getActivity());
    }


    public NdefMessage createNdefMessage(String id) {
        String text = (id);
        NdefMessage msg = new NdefMessage(
                new NdefRecord[]{NdefRecord.createMime("application/vnd.com.example.android.beam", text.getBytes())
                });
        return msg;
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

    void processIntent(Intent intent) throws ParseException, ExecutionException, InterruptedException {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present

        String idFriendString = new String(msg.getRecords()[0].getPayload());
        Integer idFriend = null;
        try{
            idFriend = Integer.parseInt(idFriendString);
            mListener.onFriendDetected(idFriend);
        } catch(Exception err){
            Log.e(TAG, "Error parsing id friend");
            err.printStackTrace();
        }

    }

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }
}
