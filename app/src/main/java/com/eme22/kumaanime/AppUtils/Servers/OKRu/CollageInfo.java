
package com.eme22.kumaanime.AppUtils.Servers.OKRu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollageInfo {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("frequency")
    @Expose
    private Integer frequency;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("count")
    @Expose
    private Integer count;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CollageInfo() {
    }

    /**
     * 
     * @param width
     * @param count
     * @param url
     * @param frequency
     * @param height
     */
    public CollageInfo(String url, Integer frequency, Integer height, Integer width, Integer count) {
        super();
        this.url = url;
        this.frequency = frequency;
        this.height = height;
        this.width = width;
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CollageInfo withUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public CollageInfo withFrequency(Integer frequency) {
        this.frequency = frequency;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public CollageInfo withHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public CollageInfo withWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public CollageInfo withCount(Integer count) {
        this.count = count;
        return this;
    }

}
