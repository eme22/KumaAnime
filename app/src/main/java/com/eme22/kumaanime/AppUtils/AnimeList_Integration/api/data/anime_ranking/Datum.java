
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_ranking;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("node")
    @Expose
    private MiniAnime node;
    @SerializedName("ranking")
    @Expose
    private Ranking ranking;

    public MiniAnime getNode() {
        return node;
    }

    public void setNode(MiniAnime node) {
        this.node = node;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

}
