
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelatedAnime {

    @SerializedName("node")
    @Expose
    private MiniAnime node;
    @SerializedName("relation_type")
    @Expose
    private String relationType;
    @SerializedName("relation_type_formatted")
    @Expose
    private String relationTypeFormatted;

    public MiniAnime getNode() {
        return node;
    }

    public void setNode(MiniAnime node) {
        this.node = node;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getRelationTypeFormatted() {
        return relationTypeFormatted;
    }

    public void setRelationTypeFormatted(String relationTypeFormatted) {
        this.relationTypeFormatted = relationTypeFormatted;
    }

}
