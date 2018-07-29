package com.mastercypher.university.mobile.datdog.util;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UtilProj {
    public static final String ERR_RESTART = "Problem encountered, restart the application";

    public static final String PATTERN_DATE = "dd/MM/yyyy-HH:mm:ss";
    public static final String PATTERN_DATE_NO_TIME = "dd/MM/yyyy";
    private static SimpleDateFormat formatData = new SimpleDateFormat(PATTERN_DATE);
    private static SimpleDateFormat formatDataNoTime = new SimpleDateFormat(PATTERN_DATE_NO_TIME);

    public static final int DB_ROW_AVAILABLE = 0;
    public static final int DB_ROW_DELETE = 0;

    public static final int STRING_SIZE_OK = 0;
    public static final int STRING_SIZE_SMALL = 1;
    public static final int STRING_SIZE_BIG = 2;


    public static int checkValues(List<String> strToCheck) {
        boolean small = false;
        boolean big = false;

        for (String str : strToCheck) {
            if (!(str.length() > 0)) {
                small = true;
                break;
            }

            if (!(str.length() < 50)) {
                big = true;
                break;
            }
        }

        if (small) {
            return STRING_SIZE_SMALL;
        } else if (big) {
            return STRING_SIZE_BIG;
        } else {
            return STRING_SIZE_OK;
        }
    }

    public static String getDateNow() {
        Date dateNow = Calendar.getInstance().getTime();
        return formatData.format(dateNow);
    }

    /**
     * Format data to data-string
     *
     * @param date
     * @return
     */
    public static String formatData(Date date) {
        return formatData.format(date);
    }

    /**
     * Format data to data-string (NO TIME)
     *
     * @param date
     * @return
     */
    public static String formatDataNoTime(Date date) {
        return formatDataNoTime.format(date);
    }

    public static Date parseDate(String dataString) {
        try {
            return formatData.parse(dataString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
