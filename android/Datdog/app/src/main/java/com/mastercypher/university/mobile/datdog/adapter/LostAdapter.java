package com.mastercypher.university.mobile.datdog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LostAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Report> mReports;

    public LostAdapter(Context context, ArrayList<Report> reports) {
        mContext = context;
        mReports = reports;
    }

    @Override
    public int getCount() {
        return mReports.size();
    }

    @Override
    public Object getItem(int position) {
        return mReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear(){
        mReports.clear();
    }

    public void addReport(final Report report) {
        mReports.add(report);
        notifyDataSetChanged();
    }

    public void addSection(final Report section) {
        mReports.add(section);
        notifyDataSetChanged();
    }

    public void addAll(List<Report> reports){
        mReports.addAll(reports);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        Report report = (Report) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (report.getId().equals("")) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_split_sections, parent, false);
            }

            String dogName = report.getLocation();
            int countValues = report.getDelete();
            TextView txvName = (TextView) convertView.findViewById(R.id.txv_name);
            TextView txvCount = (TextView) convertView.findViewById(R.id.txv_info);
            txvName.setText(dogName);
            txvCount.setText(countValues + "");
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_basic, parent, false);
            }
            try {
                Dog dogReported = new DogDbManager(mContext).selectDog(report.getDog());
                String dogName = dogReported.getName();
                String reportStatus = ("Status: " + report.getFound()).equals(UtilProj.NONE_VALUE) ?
                        Report.STATUS_NOT_FOUND : Report.STATUS_FOUND;
                TextView txvName = (TextView) convertView.findViewById(R.id.txv_name);
                TextView txvBirth = (TextView) convertView.findViewById(R.id.txv_info);
                txvName.setText(dogName);
                txvBirth.setText(reportStatus);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

}