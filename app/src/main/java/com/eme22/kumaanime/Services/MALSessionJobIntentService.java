package com.eme22.kumaanime.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.eme22.kumaanime.AppUtils.AuthUtil;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.AppUtils.ObscuredPreferences;
import com.eme22.kumaanime.AppUtils.ObscuredPreferences_v2;
import com.eme22.kumaanime.MainActivity;
import com.tingyik90.prefmanager.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.eme22.kumaanime.BuildConfig.MAL_CLIENTID;
import static com.eme22.kumaanime.SplashActivity.ACTION_CLOSE;

public class MALSessionJobIntentService extends JobIntentService {

    private final String clientid = MAL_CLIENTID;
    static final int JOB_ID = 1000;
    public static final String ACTION = "com.eme22.kumaanime.extra.ACTION";
    public static final String ACTION_1 = "com.eme22.kumaanime.action.LOGIN";
    public static final String OAUTH_EXTRA = "OAUTH";
    public static final String VERIFIER_EXTRA = "VERIFIER";
    public static final String ACTION_3 = "com.eme22.kumaanime.action.REFRESH";

    private SharedPreferences prefsenc;
    private PrefManager prefs;
    private Bundle extras;
    private static final String baseurl = "https://myanimelist.net/v1/oauth2/token";

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        //prefsenc = new ObscuredPreferences(this, this.getSharedPreferences("MAL_USERDATA", 0) );
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) prefsenc = new ObscuredPreferences_v2(this, "MAL_USERDATA").getPreferences();
            else prefsenc = new ObscuredPreferences(this, this.getSharedPreferences("MAL_USERDATA", 0) );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        prefs = new PrefManager(this);
        extras  = intent.getExtras();
        bundlehandler(extras.getString(ACTION));
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MALSessionJobIntentService.class, JOB_ID, work);
    }


    private void bundlehandler(String string) {
        switch (string){
            case ACTION_1: login();break;
            case ACTION_3: refresh();break;
        }
    }

    private void refresh() {
        String userdata2 = Connection.gettoken(this);
        refreshtoken(userdata2);
    }

    private void login() {
        String verifier = extras.getString(VERIFIER_EXTRA);
        String oauth = extras.getString(OAUTH_EXTRA);
        gettoken(verifier,oauth,this);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        localBroadcastManager.sendBroadcast(new Intent(
                ACTION_CLOSE));


    }

    private void refreshtoken(String refresh){


        try
        {
            RequestBody formBody = new FormBody.Builder()
                    .add("client_id", clientid)
                    .add("grant_type", "refresh_token")
                    .add("refresh_token", refresh)
                    .build();

            JSONObject data2 = Connection.postConnection(baseurl,formBody);

            long expiration = TimeUnit.SECONDS.toMillis(data2.getInt("expires_in"));
            long current = System.currentTimeMillis();
            Calendar next = Calendar.getInstance();
            next.setTimeInMillis(current+expiration);

            prefsenc.edit().putString("USERJSON", data2.toString()).apply();
            prefs.putString("expiration", String.valueOf(next.getTimeInMillis()));
            prefs.putBoolean("isLogged",true);

            stopSelf();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


    }


    public static void gettoken(String verifier2, String OAuth, Context context){

        try {

            RequestBody formBody = new FormBody.Builder()
                    .add("client_id", MAL_CLIENTID)
                    .add("grant_type", "authorization_code")
                    .add("code", OAuth)
                    .add("code_verifier", verifier2)
                    .build();

            JSONObject data2 = Connection.postConnection(baseurl,formBody);

            long expiration = TimeUnit.SECONDS.toMillis(data2.getInt("expires_in"));
            long current = System.currentTimeMillis();
            Calendar next = Calendar.getInstance();
            next.setTimeInMillis(current+expiration);
            new AuthUtil(context).saveToken(data2.getString("access_token"),data2.getString("refresh_token"), next.getTimeInMillis());

        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }











}
