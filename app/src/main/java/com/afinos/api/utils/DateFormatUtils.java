package com.afinos.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Copy Right 2012-2016, Afinos, Inc., or its affiliates
 * @Author: Afinos Team
 **/
public class DateFormatUtils {
    public static String format(long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        return format.format(new Date(millisecond));
    }

    public static String formatLocale(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        Date date1 = null;
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format.setTimeZone(TimeZone.getDefault());
        return format.format(date1);
    }

    public static String currentTime() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        return format.format(new Date());
    }

    public static boolean compare(String startDate, String endDate) {
        boolean before = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        try {
            before = sdf.parse(startDate).after(sdf.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            before = false;
        }
        return before;
    }
}
