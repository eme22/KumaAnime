
package com.eme22.kumaanime.AppUtils.Servers.OKRu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StunServer {

    @SerializedName("urls")
    @Expose
    private List<String> urls = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public StunServer() {
    }

    /**
     * 
     * @param urls
     */
    public StunServer(List<String> urls) {
        super();
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public StunServer withUrls(List<String> urls) {
        this.urls = urls;
        return this;
    }

}
