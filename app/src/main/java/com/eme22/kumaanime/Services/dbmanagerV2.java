package com.eme22.kumaanime.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.eme22.kumaanime.AppUtils.AnimeScrapper;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.R;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;

public class dbmanagerV2 extends Service {
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat mNotifyManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        TaskRunner taskRunner = new TaskRunner();
            mNotifyManager = NotificationManagerCompat.from(this);
            builder = new NotificationCompat.Builder(this, Objects.requireNonNull(createNotificationChannel(this)))
                    .setSmallIcon(R.drawable.ic_cloud_download)
                    .setTicker("TICKER") // use something from something from R.string
                    .setContentTitle("Creando Base de Datos Local") // use something from something from
                    .setContentText("Espere")
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);


            try {
                taskRunner.executeAsync(new FLVLinks(),null);
            } finally {
                stopForeground(true);
            }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();      super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AAA", "onCreate");
    }

    private class FLVLinks implements Callable<ArrayList<String>> {

        @Override
        public ArrayList<String> call() throws IOException {
            ArrayList<String> flv_list = new ArrayList<>();
            String baseurl = "https://www3.animeflv.net/browse?page=";
            int i = 1;
            while (true){
                builder.setContentText("Buscando links de FLV").setProgress(110, i*100/140, false);
                mNotifyManager.notify(0, builder.build());
                Document doc = Connection.getDocOk(baseurl+i);
                ArrayList<String> flv_list_placeholder = AnimeScrapper.scrape(doc,0, flv_list);
                if (!(flv_list_placeholder.size() == flv_list.size())){
                    flv_list = flv_list_placeholder;
                }
                else {
                    break;
                }
                i++;
                }
            return flv_list;

        }

    }

    public static String createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Channel_id";
            CharSequence channelName = "Application_name";
            String channelDescription = "Application_name Alert";
            int channelImportance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(true);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
            return channelId;
        } else {
            return null;
        }
    }


}