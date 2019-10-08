package com.example.nunezjonathan_poc.utils;

import java.util.Locale;

public class TimeUtils {

    private static final long DAY_TO_MILLIS = 86400000;
    private static final long HOURS_AS_MILLIS = 3600000;
    private static final long MINUTES_TO_MILLIS = 60000;
    private static final long MILLIS = 1000;

    private TimeUtils() {
    }

    public static String timerHMS(long milliseconds) {
        long hours = milliseconds / HOURS_AS_MILLIS;
        long minutes = (milliseconds - (hours * HOURS_AS_MILLIS)) / MINUTES_TO_MILLIS;
        long seconds = (milliseconds - (hours * HOURS_AS_MILLIS) - (minutes * MINUTES_TO_MILLIS)) / MILLIS;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                hours, minutes, seconds);
    }

    public static String timerHM(long milliseconds) {
        long hours = milliseconds / HOURS_AS_MILLIS;
        long minutes = (milliseconds - (hours * HOURS_AS_MILLIS)) / MINUTES_TO_MILLIS;

        return String.format(Locale.getDefault(), "%02d:%02d",
                hours, minutes);
    }

    public static String timerMS(long milliseconds) {
        long hours = milliseconds / HOURS_AS_MILLIS;
        long minutes = (milliseconds - (hours * HOURS_AS_MILLIS)) / MINUTES_TO_MILLIS;
        long seconds = (milliseconds - (hours * HOURS_AS_MILLIS) - (minutes * MINUTES_TO_MILLIS)) / MILLIS;

        return String.format(Locale.getDefault(), "%02d:%02d",
                minutes, seconds);
    }

    public static long convertToMillis(int hour, int minute) {
        return (hour * HOURS_AS_MILLIS) + (minute * MINUTES_TO_MILLIS);
    }

    public static long add24HourMillis(long milliseconds) {
        return milliseconds + DAY_TO_MILLIS;
    }
}
