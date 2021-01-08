package com.eme22.kumaanime.AppUtils.AnimeObjects;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details.AnimeDetails;

import java.io.Serializable;

public class GeneralAnime implements Serializable {

    // SOURCES 1 = MAL 1 = FLV 1 =  JK 1 = ID


    String sources;
    int malId;
    String desc_2;
    AnimeDetails details;

    public GeneralAnime() {

    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public int getMalId() {
        return malId;
    }

    public void setMalId(int malId) {
        this.malId = malId;
    }

    public AnimeDetails getDetails() {
        return details;
    }

    public void setDetails(AnimeDetails details) {
        this.details = details;
    }

    public String getDesc_2() {
        return desc_2;
    }

    public void setDesc_2(String desc_2) {
        this.desc_2 = desc_2;
    }
}
