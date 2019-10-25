package com.example.nunezjonathan_poc.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.ui.activities.MainActivity;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.TimeUtils;

import java.util.Calendar;

public class SleepTimerService extends Service {

    private static final String CHANNEL_ID = "SleepTimerServiceChannel";
    public static final String EXTRA_TIME_MILLIS = "com.example.nunezjonathan_poc.EXTRA_TIME_MILLIS";
    public static final String ACTION_UPDATE_TIMER = "com.example.nunezjonathan_poc.UPDATE_SLEEP_TIMER";

    public static boolean isRunning = false;
    public static Calendar datetime;

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    private final Intent receiverIntent = new Intent(ACTION_UPDATE_TIMER);
    private long millis;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        createTimerNotification();

        millis = intent.getLongExtra(EXTRA_TIME_MILLIS, 0);
        datetime = Calendar.getInstance();
        runnable.run();
        isRunning = true;
        return START_NOT_STICKY;
    }

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millis += 1000;

            receiverIntent.putExtra(EXTRA_TIME_MILLIS, millis);
            sendBroadcast(receiverIntent);

            notificationBuilder.setContentText(TimeUtils.timerHMS(millis));

            notificationManager.notify(1, notificationBuilder.build());

            handler.postDelayed(this, 1000);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        receiverIntent.putExtra(EXTRA_TIME_MILLIS, millis);
        sendBroadcast(receiverIntent);

        handler.removeCallbacks(runnable);
        isRunning = false;

        stopForeground(true);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Sleep Timer Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void createTimerNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //notificationIntent.putExtra(EXTRA_TIME_MILLIS, millis);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1, notificationIntent, 0);

        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Sleep Timer")
                .setContentText(getString(R.string.zeroed_HMS_timer))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(1, notificationBuilder.build());
    }
}
