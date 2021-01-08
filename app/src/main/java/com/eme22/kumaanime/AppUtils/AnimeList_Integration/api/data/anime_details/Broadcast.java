
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Broadcast {

    @SerializedName("day_of_the_week")
    @Expose
    private String dayOfTheWeek;
    @SerializedName("start_time")
    @Expose
    private String startTime;

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
