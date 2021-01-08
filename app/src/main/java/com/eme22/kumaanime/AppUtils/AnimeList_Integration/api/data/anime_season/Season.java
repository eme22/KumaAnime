
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_season;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("season")
    @Expose
    private String season;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

}
