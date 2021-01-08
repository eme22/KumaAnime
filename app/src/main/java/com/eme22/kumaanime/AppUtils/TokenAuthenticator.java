package com.eme22.kumaanime.AppUtils;

import android.content.Context;
import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {

    private Integer notLoggedResponseCode = 401;
    private Integer successResponseCode = 200;
    private final AuthUtil authUtil;

    public TokenAuthenticator(Context context) {
        this.authUtil = new AuthUtil(context);
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {

        if (responseCount(response) >= 3) {
            return null;
        }

        if (response == null) return null;

        if (response.code() == notLoggedResponseCode){
            authUtil.getNewToken(MyAnimeListAPIAdapter.getApiServiceSingle());
            return response.request().newBuilder().header("Authorization", "Bearer "+authUtil.getToken()).build();
        }
        else if (response.code() == successResponseCode){
            return null;
        }
        else {
            Log.d("RESPONSE CODE", String.valueOf(response.code()));
            return null;
        }



    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}

