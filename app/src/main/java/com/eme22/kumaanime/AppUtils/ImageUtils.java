package com.eme22.kumaanime.AppUtils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager;
import com.eme22.kumaanime.App;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ImageUtils {

    @SuppressLint("StaticFieldLeak")
    private static Picasso instance;

    public static Picasso getSharedInstance()
    {
        if(instance == null)
        {
            instance = new Picasso.Builder(App.getInstance()).executor(Executors.newSingleThreadExecutor()).indicatorsEnabled(true).build();
        }
        return instance;
    }

    public static boolean DownloadImage(String image, File location) throws IOException {
        File tmpFile = new File(App.getInstance().getCacheDir() , "CACHE");

        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request;
        request = new Request.Builder().url(image).build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null) {
            return false;
        }

        InputStream input = body.byteStream();

        OutputStream output = new FileOutputStream(tmpFile);

        byte[] data = new byte[1024];

        int count;
        while ((count = input.read(data)) != -1) {
            output.write(data, 0, count);
        }

        output.flush();

        output.close();
        input.close();

        return FileUtils.moveFile(tmpFile,location);

    }

}
