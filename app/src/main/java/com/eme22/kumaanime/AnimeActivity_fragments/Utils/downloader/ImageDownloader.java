package com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader;

import android.content.Context;
import android.util.Log;

import com.eme22.kumaanime.AppUtils.FileUtils;
import com.eme22.kumaanime.AppUtils.StringUtils;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;
import com.eme22.kumaanime.PermissionActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ImageDownloader implements Runnable{

    Context context;
    MiniAnimeTable_Repo repo;
    private final int animeID;
    DownloadManager_v2 managerV2;
    ImageDownloaderCallback callback;

    public ImageDownloader(Context context, int animeID, ImageDownloaderCallback callback) {
    this.context = context;
    this.repo = new MiniAnimeTable_Repo(context.getApplicationContext());
    this.managerV2 = new DownloadManager_v2(context);
    this.animeID = animeID;
    this.callback = callback;
    }

    @Override
    public void run() {
        downloadImage(repo.getanime(animeID).getMainPicture().getMedium());
    }


    private void downloadImage(String url){
        try {
            File tmpFile = new File(context.getCacheDir() , "CACHE");
            OkHttpClient client = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().url(url).build();
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

            String finalname = StringUtils.FormatFile(animeID,"png");

            File finallocation = new File(managerV2.getAnime(animeID) , finalname);

            // rename file but cut off .tmp

            boolean success = FileUtils.moveFile(tmpFile,finallocation);
            if (!success) {
                Log.e(DownloadManager_v2.TAG,"IMAGE DOWNLOAD FAILED");
                throw new Exception();
            }
            else callback.onSuccess();


        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public interface ImageDownloaderCallback{
        void onSuccess();
        void onFailure(Exception e);
    }
}
