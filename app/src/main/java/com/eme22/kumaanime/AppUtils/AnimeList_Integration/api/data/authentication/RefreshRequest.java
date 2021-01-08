package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.eme22.kumaanime.BuildConfig.MAL_CLIENTID;

public class RefreshRequest {

    @SerializedName("client_id")
    @Expose
    final String clientid = MAL_CLIENTID;

    @SerializedName("grant_type")
    @Expose
    final String grant_type = "refresh_token";

    @SerializedName("refresh_token")
    @Expose
    String refresh_token;

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
