package com.mastercypher.university.mobile.datdog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.database.DogDbManager;
import com.mastercypher.university.mobile.datdog.entities.Dog;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.util.UtilProj;

import java.text.ParseException;
import java.util.ArrayList;

public class ReportAdapter extends ArrayAdapter<Report> {

    public ReportAdapter(Context context, ArrayList<Report> reports) {
        super(context, 0, reports);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        Report report = (Report) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_basic, parent, false);
        }

        try {
            Dog dogReported = new DogDbManager(getContext()).selectDog(report.getDog());
            String dogName = dogReported.getName();
            String reportStatus = "Status: " + report.getFound() == null ?
                    Report.STATUS_NOT_FOUND : Report.STATUS_FOUND;
            TextView txvName = (TextView) convertView.findViewById(R.id.txv_name);
            TextView txvBirth = (TextView) convertView.findViewById(R.id.txv_info);
            txvName.setText(dogName);
            txvBirth.setText(reportStatus);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}