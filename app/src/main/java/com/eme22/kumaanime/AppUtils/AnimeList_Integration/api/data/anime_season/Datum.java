
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_season;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("node")
    @Expose
    private MiniAnime node;

    public MiniAnime getNode() {
        return node;
    }

    public void setNode(MiniAnime node) {
        this.node = node;
    }

}
