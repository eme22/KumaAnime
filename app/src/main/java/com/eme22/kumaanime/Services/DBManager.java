package com.eme22.kumaanime.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.eme22.kumaanime.R;

import java.util.Objects;

@Deprecated
public class DBManager extends JobIntentService {

    public static final String ACTION1 = "com.eme22.kumaanime.action.POPULATE_DB";
    static final int JOB_ID = 1001;
    NotificationCompat.Builder builder;
    private NotificationManagerCompat mNotifyManager;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DBManager.class, JOB_ID, work);
    }
    
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        final String action = intent.getAction();
        if (ACTION1.equals(action)) {

            mNotifyManager = NotificationManagerCompat.from(this);
            builder = new NotificationCompat.Builder(this, Objects.requireNonNull(createNotificationChannel(this)))
                    .setSmallIcon(R.drawable.ic_cloud_download)
                    .setTicker("TICKER") // use something from something from R.string
                    .setContentTitle("Creando Base de Datos Local") // use something from something from
                    .setContentText("Espere")
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH); // use something from something from
            // .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            /*
            builder = new Notification.Builder(getBaseContext())
                    .setSmallIcon(R.drawable.ic_cloud_download)
                    .setTicker("TICKER") // use something from something from R.string
                    .setContentTitle("Creando Base de Datos Local") // use something from something from
                    .setContentText("Espere"); // use something from something from
                 //   .setProgress(0, 0, true); // display indeterminate progress

             */

            //startForeground(1002, builder.build());
            try {
                populate();
            } finally {
                stopForeground(true);
            }
        }
    }

    private void populate(){







        new Thread(() -> {
            for (int i = 0; i < 10 ; i++) {
                builder.setProgress(10, i, false);
                mNotifyManager.notify(0, builder.build());
                // Displays the progress bar for the first time.
                //mNotifyManager.notify(0, mBuilder.build());
                // Sleeps the thread, simulating an operation
                // that takes time
                try {
                    // Sleep for 5 seconds
                    Thread.sleep((1000));
                } catch (InterruptedException e) {
                    Log.d("AAA", "sleep failure");
                }
            }
            mNotifyManager.cancel(0);
            //mNotifyManager.notify(0, builder.build());

        }).start();
    }

    public static String createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = "Channel_id";
            CharSequence channelName = "Application_name";
            String channelDescription = "Application_name Alert";
            int channelImportance = NotificationManager.IMPORTANCE_LOW;
            boolean channelEnableVibrate = true;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            //            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }
    
}
