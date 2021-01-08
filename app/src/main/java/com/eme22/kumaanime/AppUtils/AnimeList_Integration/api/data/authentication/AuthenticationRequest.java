package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.eme22.kumaanime.BuildConfig.MAL_CLIENTID;

public class AuthenticationRequest {

    @SerializedName("client_id")
    @Expose
    final String clientid = MAL_CLIENTID;

    @SerializedName("grant_type")
    @Expose
    final String grant_type = "authorization_code";

    @SerializedName("code")
    @Expose
    String OAuth;


    @SerializedName("code_verifier")
    @Expose
    String verifier2;

    public String getOAuth() {
        return OAuth;
    }

    public void setOAuth(String OAuth) {
        this.OAuth = OAuth;
    }

    public String getVerifier2() {
        return verifier2;
    }

    public void setVerifier2(String verifier2) {
        this.verifier2 = verifier2;
    }
}
