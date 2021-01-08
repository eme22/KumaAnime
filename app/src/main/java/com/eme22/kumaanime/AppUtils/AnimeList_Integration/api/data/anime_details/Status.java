
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("watching")
    @Expose
    private String watching;
    @SerializedName("completed")
    @Expose
    private String completed;
    @SerializedName("on_hold")
    @Expose
    private String onHold;
    @SerializedName("dropped")
    @Expose
    private String dropped;
    @SerializedName("plan_to_watch")
    @Expose
    private String planToWatch;

    public String getWatching() {
        return watching;
    }

    public void setWatching(String watching) {
        this.watching = watching;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getOnHold() {
        return onHold;
    }

    public void setOnHold(String onHold) {
        this.onHold = onHold;
    }

    public String getDropped() {
        return dropped;
    }

    public void setDropped(String dropped) {
        this.dropped = dropped;
    }

    public String getPlanToWatch() {
        return planToWatch;
    }

    public void setPlanToWatch(String planToWatch) {
        this.planToWatch = planToWatch;
    }

}
