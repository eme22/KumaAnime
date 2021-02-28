package com.eme22.kumaanime.AppUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.authentication.AuthenticationResponse;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.authentication.RefreshRequest;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.tingyik90.prefmanager.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.eme22.kumaanime.BuildConfig.MAL_CLIENTID;

public class AuthUtil {

    private final PrefManager prefs;
    private SharedPreferences prefsenc;

    public AuthUtil(Context context) {
        this.prefs = new PrefManager(context);
        //this.prefsenc = new ObscuredPreferences(context, context.getSharedPreferences("MAL_USERDATA", 0));
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                this.prefsenc = new ObscuredPreferences_v2(context, "MAL_USERDATA").getPreferences();
            else prefsenc = new ObscuredPreferences(context, context.getSharedPreferences("MAL_USERDATA", 0) );
        } catch (GeneralSecurityException | IOException e)
        {
            this.prefsenc = null;
            e.printStackTrace();
        }
    }

    public void saveToken(String token, String refreshToken, long expiration){
        prefs.putBoolean("isLogged", true);
        prefsenc.edit().putString("REFRESH_TOKEN",refreshToken).apply();
        prefsenc.edit().putLong("TOKEN_EXPIRATION", expiration).apply();
        prefsenc.edit().putString("TOKEN",token).apply();
    }
    public void setMode(int MODE) {
        prefs.putInt("APP_MODE", MODE);
    }

    public String getRefreshToken(){
        return prefsenc.getString("REFRESH_TOKEN", null);
    }

    public boolean getLogin(){
        return prefs.getBoolean("isLogged",false);
    }

    public boolean isExpired(){
        long expiration = getTokenExpiration();
        long current = System.currentTimeMillis();
        return expiration<=current;
    }


    public String getToken(){
        return prefsenc.getString("TOKEN", null);
    }

    public long getTokenExpiration(){
        return prefsenc.getLong("TOKEN_EXPIRATION", -1);
    }

    public void getNewToken(MyAnimeListAPIService myAnimeListAPIService) throws IOException {

        RefreshRequest request = new RefreshRequest();
        request.setRefresh_token(getRefreshToken());

        if (getLogin()){
            retrofit2.Response<AuthenticationResponse> responseCall = myAnimeListAPIService.refresh("application/json", "application/x-www-form-urlencoded", MAL_CLIENTID,"refresh_token",getRefreshToken()).execute();
            AuthenticationResponse response = responseCall.body();
            if (responseCall.isSuccessful()){
                if (response != null) {
                    saveToken(response.getAccessToken(),response.getRefreshToken(),response.getExpiresIn());
                }
            }
            else {
                Log.d("ERROR", String.valueOf(responseCall.code()));
            }
        }
    }
}


