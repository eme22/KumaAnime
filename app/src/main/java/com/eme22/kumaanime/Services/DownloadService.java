package com.eme22.kumaanime.Services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager_v2;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.FileUtils;
import com.eme22.kumaanime.AppUtils.NotificationSettings;
import com.eme22.kumaanime.AppUtils.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
@Deprecated
public class DownloadService extends JobIntentService {

    static final int JOB_ID = 9999;
    private static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    private static final String ACTION_NORMAL = "ACTION_NORMAL";
    private static int id;

    public static final String EPISODE_LINK = "com.eme22.kumaanime.extra.EPISODE_LINK";
    public static final String EPISODE_LOCATION = "com.eme22.kumaanime.extra.EPISODE_LOCATION";
    public static final String EPISODE = "com.eme22.kumaanime.extra.EPISODE";
    public static final String DOWNLOAD_HEADERS = "com.eme22.kumaanime.extra.DOWNLOAD_HEADERS";

    private String episodeLink;
    private MiniEpisode episode;
    private Headers headers;
    private File location;

    private NotificationManagerCompat mNotifyManager;
    private NotificationCompat.Builder builder;
    private File finallocation;
    private Long startTime;


    public static void enqueueWork(Context context, Intent work) {
        work.setAction(ACTION_NORMAL);
        enqueueWork(context, DownloadService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(@NonNull @NotNull Intent intent) {

        if (intent.getAction().equals(ACTION_STOP_SERVICE)) stopForegroundService();
        else {
            initRandom();
            initData(intent);
            initNotification();
            initImage();
            initDownload();
        }



    }

    private void initRandom() {
       id = StringUtils.createRandomCode(5);
    }

    private void initData(Intent intent) {
        if (intent.getExtras() != null) {
            episode = (MiniEpisode) intent.getSerializableExtra(EPISODE);
            episodeLink = intent.getStringExtra(EPISODE_LINK);
            headers = intent.getParcelableExtra(DOWNLOAD_HEADERS);
            location = (File) intent.getSerializableExtra(EPISODE_LOCATION);
        }
        else throw new NullPointerException();
    }

    @SuppressWarnings("deprecation")
    private void initNotification() {
        startTime = System.nanoTime();
        mNotifyManager = NotificationManagerCompat.from(this);

        Intent stopIntent = new Intent(this, DownloadService.class);
        //Intent stopSelf = intent.setAction(ACTION_STOP_SERVICE);
        //PendingIntent pStopSelf = PendingIntent.getService(this, 0, stopSelf,PendingIntent.FLAG_CANCEL_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //String NOTIFICATION_CHANNEL = OtherUtils.createNotificationChannel(this, "download_id", "Descargas", "Notificacion del canal de descargas");
            builder = new NotificationCompat.Builder(this, NotificationSettings.CHANNEL_DOWNLOADS)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setTicker("Descarga de "+episode.getName()) // use something from something from R.string
                    .setContentTitle("Iniciando Descarga de " + episode.getName()) // use something from something from
                    .setOngoing(true)
                    .setProgress(100, 0, false)
                    .setAutoCancel(false)
                    //.addAction(R.drawable.kanna, "Cancelar", pStopSelf)
                    .setPriority(NotificationCompat.PRIORITY_LOW);

        }
        else
            builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setTicker("Descarga de "+episode.getName())
                    .setContentTitle("Iniciando Descarga de " + episode.getName())// use something from something from
                    .setOngoing(true)
                    .setProgress(100, 0, false)
                    .setAutoCancel(false)
                    //.addAction(R.drawable.kanna, "Cancelar", pStopSelf)
                    .setPriority(NotificationCompat.PRIORITY_LOW);

        mNotifyManager.notify(id, builder.build());
    }

    private void initImage() {
        try {
            File tmpFile = new File(this.getCacheDir() , "CACHE");
            OkHttpClient client = new OkHttpClient.Builder().build();
            Request request;
            if (headers == null) request = new Request.Builder().url(episode.getMainPicture().getMedium()).build();
            else request = new Request.Builder().url(episode.getMainPicture().getMedium()).headers(headers).build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body == null) {
                return;
            }

            // input stream to read file - with 8k buffer
            InputStream input = body.byteStream();

            // Output stream to write file
            OutputStream output = new FileOutputStream(tmpFile);

            byte[] data = new byte[1024];

            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            //String finalname = StringUtils.FormatFile(episode.getAnimeID(),episode.getEpisode(),"png");
            //File finallocation = new File(location , finalname);

            finallocation = new File(location , StringUtils.FormatFile(episode.getAnimeID(),episode.getEpisode(),"png"));

            // rename file but cut off .tmp

            boolean success = FileUtils.moveFile(tmpFile,finallocation);
            if (!success) Log.e(DownloadManager_v2.TAG,"IMAGE DOWNLOAD FAILED");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDownload() {
        boolean success;
        try {
            File tmpFile = new File(this.getCacheDir() , "CACHE");
            OkHttpClient client = new OkHttpClient.Builder().build();
            Request request;
            if (headers == null) request = new Request.Builder().url(episodeLink).build();
            else request = new Request.Builder().url(episodeLink).headers(headers).build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body == null) {
                throw new NullPointerException();
            }
            int fileLength = (int) body.contentLength();

            // input stream to read file - with 8k buffer
            InputStream input = body.byteStream();

            // Output stream to write file
            OutputStream output = new FileOutputStream(tmpFile);

            byte[] data = new byte[1024];

            long total = 0;
            int count, tmpPercentage = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
                int percentage = (int) ((total * 100) / fileLength);
                if (percentage > tmpPercentage) {

                    long elapsedTime = System.nanoTime() - startTime;
                    long allTimeForDownloading = (elapsedTime * fileLength / total);
                    long remainingTime = allTimeForDownloading - elapsedTime;

                    builder.setSubText(total/(1024 * 1024)+" MB /"+fileLength/(1024 * 1024)+" MB")
                            .setContentText("Quedan: "+remainingTime/1000+" s.")
                            .setContentTitle(episode.getName()+" "+episode.getEpisode())
                            .setProgress(100, percentage, false);
                    mNotifyManager.notify(id, builder.build());
                    tmpPercentage = percentage;
                }
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            finallocation = new File(location , StringUtils.FormatFile(episode.getAnimeID(),episode.getEpisode(),"mp4"));

            // rename file but cut off .tmp

            success = FileUtils.moveFile(tmpFile, finallocation);

        } catch (Exception e) {
            Log.e("[Download Error]: " , e.getMessage());
            success = false;
        }
        Log.d(DownloadManager_v2.TAG,"Download finished");
        String statusText = success ? "Descarga Completa" : "Descarga Fallida";
        Log.d(DownloadManager_v2.TAG, String.valueOf(success));
        int resId = success ? android.R.drawable.stat_sys_download_done : android.R.drawable.stat_notify_error;
        Log.d(DownloadManager_v2.TAG, "ERROR 1?");
        builder.setSmallIcon(resId)
                .setOngoing(false)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText(statusText)
                .setProgress(0, 0, false);
        Log.d(DownloadManager_v2.TAG, "ERROR 2?");
        if (success) {

            try {
                //Use reflection clean up old actions
                Field f = builder.getClass().getDeclaredField("mActions");
                f.setAccessible(true);
                f.set(builder, new ArrayList<NotificationCompat.Action>());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }


            try {
                Log.d(DownloadManager_v2.TAG, "ERROR 3?");
                Uri uriForFile = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", finallocation);
                Log.d(DownloadManager_v2.TAG, "ERROR 4?");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Log.d(DownloadManager_v2.TAG, "ERROR 5?");
                intent.setDataAndType(uriForFile, "video/mp4");
                Log.d(DownloadManager_v2.TAG, "ERROR 6?");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.d(DownloadManager_v2.TAG, "ERROR 7?");
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Log.d(DownloadManager_v2.TAG, "ERROR 8?");
                builder.setContentIntent(pendingIntent);
                Log.d(DownloadManager_v2.TAG, "ERROR 9?");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Log.d(DownloadManager_v2.TAG, "ERROR 10?");
        try {
            mNotifyManager.notify(id, builder.build());
        }
        catch (Exception e){e.printStackTrace();}
    }

    private void stopForegroundService() {

        stopForeground(true);
        stopSelf();
    }

}