package com.eme22.kumaanime.Services;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class AnimeManager extends JobIntentService {

    static final int JOB_ID = 2000;


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MALSessionJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
}