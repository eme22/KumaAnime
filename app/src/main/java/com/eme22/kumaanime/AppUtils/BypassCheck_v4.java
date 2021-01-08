package com.eme22.kumaanime.AppUtils;

import android.util.Log;

import org.jsoup.nodes.Document;

import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class BypassCheck_v4 implements Callable<boolean[]>{

    Document a,b,c;
    boolean as = false, bs = false, cs = false;

    @Override
    public boolean[] call() {
        try {
            a = Connection.getDocOkWithCustomClient(customclient(), "https://www3.animeflv.net/anime/jojos-bizarre-adventure-the-animation");
            String dataa = a.getElementsByTag("meta").attr("content");
            Log.d("BYPASS CHECK 2:", dataa);
            as = dataa.length() < 1;
        } catch (Exception e) {
            Log.e("ERROR TYPE BYPASS: ", e.getLocalizedMessage());
            if (e instanceof NullPointerException) as = true;
            else if (e instanceof UnknownHostException) as = false;
            else if (e instanceof InterruptedIOException) as = false;
            else e.printStackTrace();
        }
        try {
            b = Connection.getDocOkWithCustomClient(customclient(), "https://jkanime.net/jojos-bizarre-adventure-2012/");
            String datab = b.getElementsByClass("sinopsis-box").select("p").first().text();
            Log.d("BYPASS CHECK 2:", datab);
            bs = datab.length() < 1;
        } catch (Exception e) {
            Log.e("ERROR TYPE BYPASS: ", e.getLocalizedMessage());
            if (e instanceof NullPointerException) bs = true;
            else if (e instanceof UnknownHostException) bs = false;
            else if (e instanceof InterruptedIOException) bs = false;
            else e.printStackTrace();
        }
        c = null;
        Log.d("SERVER CHECK: ", ""+as + bs+ cs);
        return new boolean[]{as, bs, cs};
        //return new boolean[]{true, true, true};
    }

    private static OkHttpClient customclient() {
        return new OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).connectTimeout(5,TimeUnit.SECONDS).writeTimeout(5,TimeUnit.SECONDS).retryOnConnectionFailure(false).build();
    }

}
