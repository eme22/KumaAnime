package com.eme22.kumaanime.AppUtils;

import android.annotation.SuppressLint;

import com.eme22.kumaanime.App;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

public class ImageUtils {

    @SuppressLint("StaticFieldLeak")
    private static Picasso instance;

    public static Picasso getSharedInstance()
    {
        if(instance == null)
        {
            instance = new Picasso.Builder(App.getInstance()).executor(Executors.newSingleThreadExecutor()).memoryCache(Cache.NONE).indicatorsEnabled(true).build();
        }
        return instance;
    }

}
