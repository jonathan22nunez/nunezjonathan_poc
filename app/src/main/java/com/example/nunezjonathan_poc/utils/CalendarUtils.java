package com.example.nunezjonathan_poc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtils {

    private static final String dateFormat = "MM/dd/yyyy";
    private static final String dateTimeFormat = "MM/dd/yyyy hh:mm a";
    private static final String timeFormatHMS = "hh:mm:ss a";
    private static final String timeFormatHM = "hh:mm a";
    private static final String timeFormatMS = "mm:ss";

    private CalendarUtils(){}

    public static String toDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return sdf.format(date);
    }

    public static String toDatetimeString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.getDefault());
        return sdf.format(date);
    }

    public static String toTimeHMSString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormatHMS, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public static String toTimeHMString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormatHM, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public static Calendar stringToCalendar(String calendarString) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.getDefault());
        try {
            calendar.setTime(sdf.parse(calendarString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String getEndDatetime(String startDatetimeString, long duration) {
        Calendar start = CalendarUtils.stringToCalendar(startDatetimeString);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(start.getTimeInMillis() + duration);
        return CalendarUtils.toTimeHMString(end);
    }
}
