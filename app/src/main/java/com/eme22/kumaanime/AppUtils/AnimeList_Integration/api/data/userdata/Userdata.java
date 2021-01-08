
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userdata {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("joined_at")
    @Expose
    private String joinedAt;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("anime_statistics")
    @Expose
    private AnimeStatistics animeStatistics;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public AnimeStatistics getAnimeStatistics() {
        return animeStatistics;
    }

    public void setAnimeStatistics(AnimeStatistics animeStatistics) {
        this.animeStatistics = animeStatistics;
    }

}
