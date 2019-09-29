package com.example.nunezjonathan_poc.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimerUtils {

    private TimerUtils() {}

    public static String timerHMS(long milliseconds) {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds));
    }
}
