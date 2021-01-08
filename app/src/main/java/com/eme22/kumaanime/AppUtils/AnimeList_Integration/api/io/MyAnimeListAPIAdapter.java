package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io;

import android.content.Context;

import com.eme22.kumaanime.AppUtils.Mods.LoginInterceptor;
import com.eme22.kumaanime.AppUtils.TokenAuthenticator;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyAnimeListAPIAdapter {

    private static MyAnimeListAPIService API_SERVICE;

    public MyAnimeListAPIAdapter() {

    }

    public static MyAnimeListAPIService getApiServiceWithAuth(Context context) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(context);
        LoginInterceptor loginInterceptor = new LoginInterceptor(context);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(loginInterceptor).addInterceptor(logging).authenticator(tokenAuthenticator);



        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.myanimelist.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- usamos el log level
                    .build();
            API_SERVICE = retrofit.create(MyAnimeListAPIService.class);
        }

        return API_SERVICE;
    }

    public static MyAnimeListAPIService getApiServiceSingle() {

        MyAnimeListAPIService API_SERVICE_2;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(logging);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://myanimelist.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build()) // <-- usamos el log level
                .build();

        API_SERVICE_2 = retrofit.create(MyAnimeListAPIService.class);

        return API_SERVICE_2;
    }

}
