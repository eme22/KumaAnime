
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlternativeTitles {

    @SerializedName("synonyms")
    @Expose
    private List<String> synonyms = null;
    @SerializedName("en")
    @Expose
    private String en;
    @SerializedName("ja")
    @Expose
    private String ja;

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getJa() {
        return ja;
    }

    public void setJa(String ja) {
        this.ja = ja;
    }

}
