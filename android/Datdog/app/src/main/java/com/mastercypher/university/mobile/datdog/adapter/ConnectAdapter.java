package com.mastercypher.university.mobile.datdog.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.util.ArrayList;
import java.util.List;

public class ConnectAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Dog> mDogs;

    public ConnectAdapter(Context context, ArrayList<Dog> dogs) {
        mContext = context;
        mDogs = dogs;
    }

    @Override
    public int getCount() {
        return mDogs.size();
    }

    @Override
    public Object getItem(int position) {
        return mDogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear(){
        mDogs.clear();
    }

    public void addDog(final Dog dog) {
        mDogs.add(dog);
        notifyDataSetChanged();
    }

    public void addSection(final Dog section) {
        mDogs.add(section);
        notifyDataSetChanged();
    }

    public void addAll(List<Dog> dogs){
        mDogs.addAll(dogs);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        Dog dog = (Dog) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (dog.getId().equals(UtilProj.NONE_VALUE)) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_split_sections, parent, false);
            }
            String dogSection = dog.getName();
            int dogNumItems = dog.getSize();
            TextView txvName = (TextView) convertView.findViewById(R.id.txv_name);
            TextView txvBirth = (TextView) convertView.findViewById(R.id.txv_info);
            txvName.setText(dogSection);
            txvBirth.setText(String.valueOf(dogNumItems));

            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            return convertView;
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_basic, parent, false);
            }
            String dogName = dog.getName();
            String dogBirth = "Birth: " + UtilProj.formatDataNoTime(dog.getBirth());
            TextView txvName = (TextView) convertView.findViewById(R.id.txv_name);
            TextView txvBirth = (TextView) convertView.findViewById(R.id.txv_info);
            txvName.setText(dogName);
            txvBirth.setText(dogBirth);
            return convertView;
        }
    }

}