package com.eme22.kumaanime;

import android.app.Application;
import android.os.Build;

import com.eme22.kumaanime.AppUtils.NotificationSettings;
import com.eme22.kumaanime.AppUtils.OtherUtils;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        NotificationSettings.initializeNotificationManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OtherUtils.createNotificationChannel(this, NotificationSettings.CHANNEL_DOWNLOADS, "Descargas", "Notificacion del canal de descargas");
        }
    }
}
