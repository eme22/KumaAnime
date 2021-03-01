package com.eme22.kumaanime.Services.Downloads;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager_v2;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.AppUtils.AppConstant;
import com.eme22.kumaanime.AppUtils.FileUtils;
import com.eme22.kumaanime.AppUtils.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class DownloadService_v2 extends JobIntentService {

    public static final String EPISODE = "com.eme22.kumaanime.extra.EPISODE";
    public static final String DOWNLOAD_HEADERS = "com.eme22.kumaanime.extra.DOWNLOAD_HEADERS";

    public static volatile boolean shouldContinue = true;
    private static final int JOB_ID = 9999;
    private int totalFileSize;
    private MiniEpisodeOffline episode;
    private File tempFile;
    private NotificationUtils notification;
    private DownloadManager_v2 manager;
    private boolean success;
    private boolean isCanceled = false;
    private Map<String, String> headers;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DownloadService_v2.class, JOB_ID, work);
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getFlags() == PendingIntent.FLAG_CANCEL_CURRENT) {
            Log.d(AppConstant.TAG, "Stop pressed");
            stopSelf();
            shouldContinue = false;
            return Service.START_NOT_STICKY;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleWork(@NonNull @NotNull Intent intent) {
        check(intent);
    }

    private void check(Intent intent) {
        if (intent.getExtras() != null) {
            episode = intent.getParcelableExtra(EPISODE);
            headers = (Map<String, String>) intent.getSerializableExtra(DOWNLOAD_HEADERS);
            notification = new NotificationUtils(this,episode);
            manager = new DownloadManager_v2(this);
        }
        else throw new NullPointerException();
        notification.generateNotifcation(intent);
        initDownload();
    }

    private void initDownload() {
        try {
            ResponseBody request;
            if (!manager.getEpisodeFile(episode,false).exists()){
                request = request(episode.getMainPicture().getMedium());
                downloadFile(request);
                moveFile(false);
            }
            if (headers != null) {
                request = request(episode.getLink(), headers);
            } else {
                request = request(episode.getLink());
            }
            downloadFile(request);
            moveFile(true);
            if (!isCanceled) onDownloadComplete();
            else shouldContinue = true;

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte[] data = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        //File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), HttpParams.API_END_POINT);
        tempFile = new File(this.getCacheDir() , AppConstant.CACHE_NAME);
        OutputStream output = new FileOutputStream(tempFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendIntent(download);
                timeCount++;
            }

            output.write(data, 0, count);

            if (!shouldContinue) {
                stopSelf();
                isCanceled = true;
                output.flush();
                output.close();
                bis.close();
                return;
            }
        }
        output.flush();
        output.close();
        bis.close();

    }

    private void moveFile(boolean video) throws IOException {
        if (isCanceled) {
            notification.cancelNotification();
            return;
        }
        if (tempFile.exists()){
            success = FileUtils.moveFile(tempFile, manager.getEpisodeFile(episode,video));
        }
        else throw new NullPointerException();
    }
    private ResponseBody request(String link, Map<String, String> headers) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request;
        if (headers == null) request = new Request.Builder().url(link).build();
        else
        {
            Request.Builder b = new Request.Builder().url(link);
            for (String key: headers.keySet()) {
                if (!(key== null || headers.get(key) == null))
                    b.addHeader(key,headers.get(key));
            }
            request = b.build();
        }
        return client.newCall(request).execute().body();
    }

    private ResponseBody request(String link) throws IOException {
        return request(link,null);
    }

    private void sendIntent(Download download) {
        notification.sendNotification(download,totalFileSize);
        Intent intent = new Intent(AppConstant.ANIME_DOWNLOAD_MESSAGE_PROGRESS);
        intent.setAction("download");
        intent.putExtra("id", episode.getAnimeID());
        intent.putExtra("episode", episode.getEpisode());
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(DownloadService_v2.this).sendBroadcast(intent);
    }

    private void onDownloadComplete() {
        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);
        notification.onDownloadComplete(success);

    }

}
