package com.eme22.kumaanime.AppUtils.Servers.Yu;

import com.eme22.kumaanime.AppUtils.Servers.Common.CommonServer;

public class Yu extends CommonServer {

    private String referer;

    public Yu(String file, String referer) {
        super(file);
        this.referer = referer;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public CommonServer withReferer(String referer) {
        this.referer = referer;
        return this;
    }
}
