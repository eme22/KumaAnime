package com.eme22.kumaanime.AppUtils.Servers.Common;

import java.util.HashMap;

public class HeadersServer extends CommonServer{

    private HashMap<String,String> headers;

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HeadersServer withHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }
}
