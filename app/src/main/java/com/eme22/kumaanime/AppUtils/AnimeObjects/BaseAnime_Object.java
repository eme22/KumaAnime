package com.eme22.kumaanime.AppUtils.AnimeObjects;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.IOException;

import static com.eme22.kumaanime.AppUtils.Connection.getDocOk;

public class BaseAnime_Object {

    @PrimaryKey
    public Integer id;
    @Ignore
    protected static Document maindoc;
    public String url;
    public String title;
    public String type;
    public String prefix;
    public String description;
    public String image;
    public JSONArray miniatures;
    public JSONObject episodelist;
    public int episodes;


    public BaseAnime_Object(String url) throws IOException {
        this.url = url;
        maindoc = getDocOk(url);
        this.id = url.hashCode();
    }


}
