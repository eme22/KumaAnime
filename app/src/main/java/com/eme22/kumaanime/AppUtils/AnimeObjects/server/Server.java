package com.eme22.kumaanime.AppUtils.AnimeObjects.server;

import java.io.Serializable;

public class Server implements Serializable {

    String name;
    String source;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Server withName(String name) {
        this.name = name;
        return this;
    }

    public Server withSource(String source) {
        this.source = source;
        return this;
    }
}
