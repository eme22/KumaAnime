
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_ranking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ranking {

    @SerializedName("rank")
    @Expose
    private Integer rank;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

}
