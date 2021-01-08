
package com.eme22.kumaanime.AppUtils.Servers.Fembed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Fembed {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("player")
    @Expose
    private Player player;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("captions")
    @Expose
    private List<Object> captions = null;
    @SerializedName("is_vr")
    @Expose
    private Boolean isVr;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Fembed withSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Fembed withPlayer(Player player) {
        this.player = player;
        return this;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Fembed withData(List<Datum> data) {
        this.data = data;
        return this;
    }

    public List<Object> getCaptions() {
        return captions;
    }

    public void setCaptions(List<Object> captions) {
        this.captions = captions;
    }

    public Fembed withCaptions(List<Object> captions) {
        this.captions = captions;
        return this;
    }

    public Boolean getIsVr() {
        return isVr;
    }

    public void setIsVr(Boolean isVr) {
        this.isVr = isVr;
    }

    public Fembed withIsVr(Boolean isVr) {
        this.isVr = isVr;
        return this;
    }

}
