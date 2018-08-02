package com.mastercypher.university.mobile.datdog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.UserDbManager;
import com.mastercypher.university.mobile.datdog.entities.Friendship;
import com.mastercypher.university.mobile.datdog.entities.User;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<Friendship> {

    Context mContext;

    public FriendAdapter(Context context, ArrayList<Friendship> friends) {
        super(context, 0, friends);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        Friendship friendship = (Friendship) getItem(position);
        User friend = null;
        try {
            friend = new UserDbManager(mContext).selectUser(friendship.getFriend());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_basic, parent, false);
        }

        if (friend != null) {
            String dogName = friend.getName();
            String dogBirth = "Birth: " + UtilProj.formatDataNoTime(friendship.getCreate());
            TextView txvName = (TextView) convertView.findViewById(R.id.txv_name);
            TextView txvBirth = (TextView) convertView.findViewById(R.id.txv_info);
            txvName.setText(dogName);
            txvBirth.setText(dogBirth);
        }
        return convertView;
    }
}
