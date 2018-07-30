package com.mastercypher.university.mobile.datdog.view.addFriend;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.FriendshipDbManager;
import com.mastercypher.university.mobile.datdog.database.UserDbManager;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.DlTask;
import com.mastercypher.university.mobile.datdog.util.FriendshipTask;
import com.mastercypher.university.mobile.datdog.util.RemoteFriendshipTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CreateFriendFragment extends Fragment {

    private CreateListener mListener;
    private Activity mActivity = null;

    public interface CreateListener {
        int getIdFriend();

        void onFinishCreation(User friend);
    }

    public CreateFriendFragment() {
    }

    public static CreateFriendFragment newInstance() {
        return new CreateFriendFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download, container, false);
        mActivity = getActivity();
        int idFriend = mListener.getIdFriend();
        new FriendshipTask(getActivity(), mListener, idFriend).execute();
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateListener) {
            mListener = (CreateListener) context;
        } else {
            Log.e("DownloadFragment", "not possible to initialize the listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

/*
    private void creationFriendship(){
        Collection<Map<String, String>> friendis;
        friendis = new DlTask().execute("http://datdog.altervista.org/user.php?action=select-user&id=" + idFriend).get();

        Iterator<Map<String, String>> it = friendis.iterator();
        User friend = null;
        if (it.hasNext()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Invalid user");
            builder.setMessage("User ID you got is not valid.");
            builder.setCancelable(true);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            new UserDbManager(getContext()).addUser(friend);
            String now = UtilProj.getDateNow();
            Map<String, String> fr1 = new HashMap<>();
            fr1.put("id", Friendship.createId(AccountDirectory.getInstance().getUser().getId(), friend.getId(), now));
            fr1.put("id_user_f", "" + AccountDirectory.getInstance().getUser().getId());
            fr1.put("id_friend_f", "" + friend.getId());
            fr1.put("date_create_f", now);
            fr1.put("date_create_f", now);
            fr1.put("delete_f", "0");

            Friendship usFr = new Friendship(fr1);
            new FriendshipDbManager(getContext()).addFriendship(usFr);
            new RemoteFriendshipTask(ActionType.INSERT, usFr);

            fr1 = new HashMap<>();
            fr1.put("id", Friendship.createId(friend.getId(), AccountDirectory.getInstance().getUser().getId(), now));
            fr1.put("id_user_f", "" + friend.getId());
            fr1.put("id_friend_f", "" + AccountDirectory.getInstance().getUser().getId());
            fr1.put("date_create_f", now);
            fr1.put("date_create_f", now);
            fr1.put("delete_f", "0");

            usFr = new Friendship(fr1);
            new RemoteFriendshipTask(ActionType.INSERT, usFr);
        }
    }
    */
}
