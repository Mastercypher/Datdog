package com.mastercypher.university.mobile.datdog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.Dog;
import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.util.ArrayList;

public class DogAdapter extends ArrayAdapter<Dog> {

    public DogAdapter(Context context, ArrayList<Dog> dogs) {
        super(context, 0, dogs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        Dog dog = (Dog) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_dogs, parent, false);
        }
        TextView txvName = (TextView) convertView.findViewById(R.id.txv_name);
        TextView txvBirth = (TextView) convertView.findViewById(R.id.txv_birth);
        txvName.setText(dog.getName());
        txvBirth.setText(UtilProj.formatData(dog.getBirth()));
        return convertView;
    }
}
