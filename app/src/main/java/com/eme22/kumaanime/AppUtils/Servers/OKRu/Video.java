
package com.eme22.kumaanime.AppUtils.Servers.OKRu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("seekSchema")
    @Expose
    private Integer seekSchema;
    @SerializedName("disallowed")
    @Expose
    private Boolean disallowed;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Video() {
    }

    /**
     * 
     * @param name
     * @param seekSchema
     * @param disallowed
     * @param url
     */
    public Video(String name, String url, Integer seekSchema, Boolean disallowed) {
        super();
        this.name = name;
        this.url = url;
        this.seekSchema = seekSchema;
        this.disallowed = disallowed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Video withName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Video withUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getSeekSchema() {
        return seekSchema;
    }

    public void setSeekSchema(Integer seekSchema) {
        this.seekSchema = seekSchema;
    }

    public Video withSeekSchema(Integer seekSchema) {
        this.seekSchema = seekSchema;
        return this;
    }

    public Boolean getDisallowed() {
        return disallowed;
    }

    public void setDisallowed(Boolean disallowed) {
        this.disallowed = disallowed;
    }

    public Video withDisallowed(Boolean disallowed) {
        this.disallowed = disallowed;
        return this;
    }

}
