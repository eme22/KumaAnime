package com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader;

import android.accounts.NetworkErrorException;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.FileProvider;

import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.FileUtils;
import com.eme22.kumaanime.AppUtils.OtherUtils;
import com.eme22.kumaanime.AppUtils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EpisodeDownloader implements Runnable {

    NotificationCompat.Builder builder;
    Context context;
    MiniEpisode episode;
    String url;
    File location;
    Headers headers;
    Callback callback;
    NotificationManagerCompat mNotifyManager;
    private File finallocation;

    public EpisodeDownloader(Context context, String url, MiniEpisode episode,File location, Callback callback) {
        this.context = context;
        this.episode = episode;
        this.url = url;
        this.location = location;
        this.callback = callback;
        this.headers = null;
    }

    public EpisodeDownloader(Context context, String url, MiniEpisode episode,File location, Headers headers, Callback callback) {
        this.context = context;
        this.episode = episode;
        this.url = url;
        this.location = location;
        this.headers = headers;
        this.callback = callback;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        mNotifyManager = NotificationManagerCompat.from(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL = OtherUtils.createNotificationChannel(context, "download_id", "Descargas", "Notificacion del canal de descargas");
            builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setTicker("Descarga de "+episode.getName()) // use something from something from R.string
                    .setContentTitle("Iniciando Descarga de " + episode.getName()) // use something from something from
                    .setContentText("0%")
                    .setOngoing(true)
                    .setProgress(100, 0, false)
                    .setAutoCancel(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW);

        }
        else
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
//                  .setTicker("TICKER") // use something from something from R.string
                    .setContentTitle("Iniciando Descarga de " + episode.getName())// use something from something from
                    .setOngoing(true)
                    .setProgress(100, 0, false)
                    .setAutoCancel(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW);
        builder.build();
        downloadImage();
        downloadEpisode();

    }

    private void downloadEpisode(){
        boolean success;
        try {
        File tmpFile = new File(context.getCacheDir() , "CACHE");
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request;
        if (headers == null) request = new Request.Builder().url(url).build();
        else request = new Request.Builder().url(url).headers(headers).build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null) {
            callback.onError(new NullPointerException());
            return;
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
                builder.setContentText(total/(1024 * 1024)+" MB /"+fileLength/(1024 * 1024)+" MB")
                        .setContentTitle("Descargando "+ episode.getName())
                        .setProgress(100, percentage, false);
                mNotifyManager.notify(0, builder.build());
                tmpPercentage = percentage;
            }
        }

        // flushing output
        output.flush();

        // closing streams
        output.close();
        input.close();



        //String finalname = StringUtils.FormatFile(episode.getAnimeID(),episode.getEpisode(),"mp4");
            if (DownloadManager.getInstance() != null) {
                finallocation = DownloadManager.getInstance().getFile(episode);
            }
            else {
                String finalname = StringUtils.FormatFile(episode.getAnimeID(),episode.getEpisode(),"mp4");
                finallocation = new File(location , finalname);
            }

            // rename file but cut off .tmp

            success = FileUtils.moveFile(tmpFile, finallocation);
    } catch (Exception e) {
        Log.e("[Download Error]: " , e.getMessage());
        success = false;
    }
    Log.d(DownloadManager.TAG,"Download finished");
    String statusText = success ? "Descarga Completa" : "Descarga Fallida";
    Log.d(DownloadManager.TAG, String.valueOf(success));
    int resId = success ? android.R.drawable.stat_sys_download_done : android.R.drawable.stat_notify_error;
        builder.setSmallIcon(resId)
                .setOngoing(false)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText(statusText)
                .setProgress(0, 0, false);

        if (success) {
            Uri uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", finallocation);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriForFile, "video/mp4");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
        }

        mNotifyManager.notify(0, builder.build());

    }

    private void downloadImage(){
        try {
            File tmpFile = new File(context.getCacheDir() , "CACHE");
            OkHttpClient client = new OkHttpClient.Builder().build();
            Request request;
            if (headers == null) request = new Request.Builder().url(episode.getMainPicture().getMedium()).build();
            else request = new Request.Builder().url(url).headers(headers).build();
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

            String finalname = StringUtils.FormatFile(episode.getAnimeID(),episode.getEpisode(),"png");
            File finallocation = new File(location , finalname);

            // rename file but cut off .tmp

            boolean success = FileUtils.moveFile(tmpFile,finallocation);
            if (!success) Log.e(DownloadManager.TAG,"IMAGE DOWNLOAD FAILED");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
