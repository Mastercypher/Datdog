package com.mastercypher.university.mobile.datdog.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Vaccination;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.util.ArrayList;
import java.util.List;

public class VaccinationAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Vaccination> mVax;

    public VaccinationAdapter(Context context, ArrayList<Vaccination> vaxs) {
        mContext = context;
        mVax = vaxs;
    }

    @Override
    public int getCount() {
        return mVax.size();
    }

    @Override
    public Object getItem(int position) {
        return mVax.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        mVax.clear();
    }

    public void addDog(final Vaccination vax) {
        mVax.add(vax);
        notifyDataSetChanged();
    }

    public void addSection(final Vaccination section) {
        mVax.add(section);
        notifyDataSetChanged();
    }

    public void addAll(List<Vaccination> vaxs) {
        mVax.addAll(vaxs);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        Vaccination vax = (Vaccination) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (vax.getId().equals(UtilProj.NONE_VALUE)) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_split_sections, parent, false);
            String dogSection = vax.getName();
            int dogNumItems = vax.getDelete();
            TextView txvName = (TextView) convertView.findViewById(R.id.txv_name);
            TextView txvBirth = (TextView) convertView.findViewById(R.id.txv_info);
            txvName.setText(dogSection);
            txvBirth.setText(String.valueOf(dogNumItems));

            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            return convertView;
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_basic, parent, false);

            String vaxName = vax.getName();
            String vaxWhen = "When: " + UtilProj.formatDataNoTime(vax.getWhen());
            TextView txvName = (TextView) convertView.findViewById(R.id.txv_name);
            TextView txvBirth = (TextView) convertView.findViewById(R.id.txv_info);
            txvName.setText(vaxName);
            txvBirth.setText(vaxWhen);
            return convertView;
        }
    }

}