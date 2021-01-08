
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistics {

    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("num_list_users")
    @Expose
    private Integer numListUsers;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getNumListUsers() {
        return numListUsers;
    }

    public void setNumListUsers(Integer numListUsers) {
        this.numListUsers = numListUsers;
    }

}
