package com.app.ivoke.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

public class DateTimeHelper {

    public static final String DEFAULT_WEBSERVER_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @SuppressLint("SimpleDateFormat")
    public static Date parseToDate(String pDataString)
    {
        try {
            return new SimpleDateFormat(DEFAULT_WEBSERVER_DATETIME_FORMAT).parse(pDataString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getDaysBetween(Date now, java.util.Date dt)
    {
        long dif = now.getTime() - dt.getMinutes();
        return Math.round(dif / 86400000);
    }

    public static long getHoursBetween(Date pDateNew, Date pDateOlder)
    {
        long dif = pDateNew.getTime() - pDateOlder.getMinutes();
        return Math.round((dif % 86400000) / 3600000);
    }

    public static long getMinutesBetween(Date pDateNew, Date pDateOlder)
    {
        Log.d("DateTimeHelper.getMinutesBetween", "pDateNew "+pDateNew.toString()+" pDateOlder:"+pDateOlder.toString());
        long dif = pDateNew.getTime() - pDateOlder.getMinutes();
        Log.d("DateTimeHelper.getMinutesBetween","dif:"+dif);
        return Math.round(((dif/1000)/60));
    }

    public static long getDaysFromMinutes(long pMinutes)
    {
        return (pMinutes/60)/24;
    }

    public static long getHoursFromMinutes(long pMinutes)
    {
        return (pMinutes/60);
    }
} 
