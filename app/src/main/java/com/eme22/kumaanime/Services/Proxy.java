package com.eme22.kumaanime.Services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Proxy extends JobIntentService {

    static final int JOB_ID = 1024;



    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
}