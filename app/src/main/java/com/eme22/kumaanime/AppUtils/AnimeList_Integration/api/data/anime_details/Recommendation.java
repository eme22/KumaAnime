
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recommendation {

    @SerializedName("node")
    @Expose
    private MiniAnime node;
    @SerializedName("num_recommendations")
    @Expose
    private Integer numRecommendations;

    public MiniAnime getNode() {
        return node;
    }

    public void setNode(MiniAnime node) {
        this.node = node;
    }

    public Integer getNumRecommendations() {
        return numRecommendations;
    }

    public void setNumRecommendations(Integer numRecommendations) {
        this.numRecommendations = numRecommendations;
    }

}
