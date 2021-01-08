
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userdata_1 {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("joined_at")
    @Expose
    private String joinedAt;
    @SerializedName("anime_statistics")
    @Expose
    private AnimeStatistics_1 animeStatistics;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }

    public AnimeStatistics_1 getAnimeStatistics() {
        return animeStatistics;
    }

    public void setAnimeStatistics(AnimeStatistics_1 animeStatistics) {
        this.animeStatistics = animeStatistics;
    }

}
