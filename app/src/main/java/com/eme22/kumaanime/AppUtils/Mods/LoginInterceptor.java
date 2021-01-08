package com.eme22.kumaanime.AppUtils.Mods;

import android.content.Context;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AuthUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoginInterceptor implements Interceptor {
    private Map<String, String> headers;

    private AuthUtil authUtil;


    public LoginInterceptor(Context context) {
        this.authUtil = new AuthUtil(context);
    }

    /**
     * Intercept customer header
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (authUtil.isExpired()){
            authUtil.getNewToken(MyAnimeListAPIAdapter.getApiServiceSingle());
        }
        builder.header("Authorization","Bearer "+authUtil.getToken());
        return chain.proceed(builder.build());
    }

}