package com.eme22.kumaanime.AppUtils.NewsObject;

import com.eme22.kumaanime.AppUtils.Connection;

import org.jsoup.nodes.Document;

import java.io.IOException;

public class NewsObject {

    Document doc;

    public NewsObject(String url) throws IOException {
        this.doc = Connection.getDocOk(url);
    }
}
