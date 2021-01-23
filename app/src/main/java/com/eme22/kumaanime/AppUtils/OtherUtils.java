package com.eme22.kumaanime.AppUtils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;

import com.eme22.kumaanime.App;

public class OtherUtils {

    public static void toast(@StringRes int message){
       toast(App.getInstance().getText(message).toString());
    }
    public static void toast(String message){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(App.getInstance(), message, duration);
        toast.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    public static String createNotificationChannel(Context context, String channel_id, String Channel_name, String channel_description) {

        // NotificationChannels are required for Notifications on O (API 26) and above.


            // The id of the channel.
            //String channelId = "Channel_id";

            // The user-visible name of the channel.
            //CharSequence channelName = "Application_name";
            // The user-visible description of the channel.
            //String channelDescription = "Application_name Alert";
            int channelImportance = NotificationManager.IMPORTANCE_LOW;
            //            int channelLockscreenVisibility = Notification.;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, Channel_name, channelImportance);
            notificationChannel.setDescription(channel_description);
            //notificationChannel.setVibrationPattern(new long[]{ 0 });
            notificationChannel.enableVibration(false);
            //            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channel_id;

    }


}
