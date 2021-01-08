package com.eme22.kumaanime.AppUtils.AnimeList_Integration;

import android.content.Context;
import android.widget.Toast;

import com.eme22.kumaanime.R;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import static com.eme22.kumaanime.BuildConfig.MAL_CLIENTID;

public class AnimeList_Auth {

    public boolean isLogged;

    private static final String baseUrl = "https://api.myanimelist.net/v2/";
    private final String baseurl1 = "https://www.myanimelist.net/v1/oauth2/authorize";
    private final String baseurl2 = " https://myanimelist.net/v1/oauth2/token";
    private final String responsetype = "code";
    private final String clientid = MAL_CLIENTID;
    private final String grantype = "authorization_code";

    public String getGeneral1(String challenge) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException {

        String general = baseurl1 +"?response_type="+responsetype+"&client_id="+clientid+"&code_challenge="+challenge;
        return general;
    }

    public String getoauthpost(Context context,String response) {

        if (response.contains("denied") & response.contains("request")) {
            String error = context.getString(R.string.mal_login_denied);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, error, duration);
            toast.show();
            return null;
        } else {
            String response1 = response;
            String[] response2 = response1.split("=");
            response1 = response2[1];
            return response1;
        }
    }

}





