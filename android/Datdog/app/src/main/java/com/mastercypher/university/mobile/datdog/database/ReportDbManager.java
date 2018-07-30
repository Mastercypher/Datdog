package com.mastercypher.university.mobile.datdog.database;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.mastercypher.university.mobile.datdog.R;
import com.mastercypher.university.mobile.datdog.entities.AccountDirectory;
import com.mastercypher.university.mobile.datdog.entities.Report;
import com.mastercypher.university.mobile.datdog.util.ActionType;
import com.mastercypher.university.mobile.datdog.util.RemoteReportTask;
import com.mastercypher.university.mobile.datdog.util.UtilProj;
import com.mastercypher.university.mobile.datdog.view.FriendsActivity;
import com.mastercypher.university.mobile.datdog.view.ReportActivity;

import java.security.AccessControlContext;
import java.text.ParseException;
import java.util.Date;

public class ReportDbManager {

    private DatabaseHelper databaseHelper;
    private Context context;

    public ReportDbManager(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addReport(Report report) throws ParseException {
        Report inDb = selectReport(report.getId());
        if (inDb == null) {
            if (!report.getUser().equals("" + AccountDirectory.getInstance().getUser().getId())) {
                Intent tapIntent = new Intent(context, ReportActivity.class);
                PendingIntent tapPendingIntent = PendingIntent.getActivity(context, 0, tapIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder noti = new NotificationCompat.Builder(context, "Reported")
                        .setContentTitle(UtilProj.upperFirstChar(new DogDbManager(context).selectDog(report.getDog()).getName()) + " reported!")
                        //.setContentText()
                        .setSmallIcon(R.drawable.icon_lost_dog)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(UtilProj.upperFirstChar(new UserDbManager(context).selectUser(Integer.parseInt(report.getUser()))
                                .getName()) + " found your dog and is helping you to get it back. Tap to get the necessary information!"))
                        .setContentIntent(tapPendingIntent)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true);
                NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nManager.notify(1,noti.build());
            }
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long row = db.insert(Report.TABLE_NAME, null, report.getContentValues());
            return row > 0;
        } else if (report.getUpdate().after(inDb.getUpdate())) {
            return updateReport(report);
        } else if (report.getUpdate().before(inDb.getUpdate())) {
            new RemoteReportTask(ActionType.UPDATE, inDb);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateReport(Report report) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long row = db.update(Report.TABLE_NAME, report.getContentValues(),
                Report.COLUMN_ID + " = ?", new String[]{String.valueOf(report.getId())});
        return row > 0;
    }

    public boolean deleteReport(Report report) {
        return updateReport(report);
    }

    public boolean foundReport(Report report) {
        Date date = new Date();
        report.setFound(date);
        report.setUpdate(date);
        return updateReport(report);
    }

    public Report selectReport(String id) throws ParseException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c;
        String query = "SELECT *" +
                "FROM " + Report.TABLE_NAME +
                " WHERE id = ?";
        c = db.rawQuery(query, new String[]{String.valueOf(id)});
        if (c.getCount() == 0) {
            return null;
        } else {
            c.moveToNext();
            return new Report(c);
        }
    }
}
