package com.eme22.kumaanime.Services.Downloads;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager_v2;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.AppUtils.NotificationSettings;
import com.eme22.kumaanime.R;

public class NotificationUtils {
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManager;
    private final Context context;
    private final MiniEpisodeOffline episode;
    private final int id;
    private DownloadManager_v2 managerV2;

    public NotificationUtils(Context context, MiniEpisodeOffline episode) {
        this.context = context;
        this.episode = episode;
        this.id  = NotificationSettings.getNotificationId();
        this.managerV2 = new DownloadManager_v2(context);
    }

    public void generateNotifcation(Intent intent){

        PendingIntent pStopSelf = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager = NotificationManagerCompat.from(context);
        notificationBuilder = new NotificationCompat.Builder(context, NotificationSettings.CHANNEL_DOWNLOADS)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentText("Iniciando Descarga de "+episode.getName()) // use something from something from R.string
                .setContentTitle(episode.getName() +" "+episode.getEpisode()) // use something from something from
                .setOngoing(true)
                .setProgress(100, 0, false)
                .setAutoCancel(false)
                .addAction(R.drawable.kanna, "Cancelar", pStopSelf)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        notificationManager.notify(id, notificationBuilder.build());
    }

    public void sendNotification(Download download, int totalFileSize){
        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText("Descargando "+ download.getCurrentFileSize() +"/"+totalFileSize +" MB");
        notificationManager.notify(id, notificationBuilder.build());
    }

    @SuppressLint("RestrictedApi")
    public void onDownloadComplete(boolean success){
        String statusText = success ? "Descarga Completa" : "Descarga Fallida";
        int resId = success ? android.R.drawable.stat_sys_download_done : android.R.drawable.stat_notify_error;
        Uri uriForFile = new DownloadManager_v2(context).getFileUri(episode);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uriForFile, "video/mp4");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder
                .setSmallIcon(resId)
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentText(statusText)
                .setProgress(0, 0, false);
        notificationBuilder.mActions.clear();
        notificationManager.notify(id, notificationBuilder.build());
    }

    public void cancelNotification(){
        notificationManager.cancel(id);
    }
}
