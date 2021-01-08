package com.eme22.kumaanime.PlayActivityFragments.Util;

import com.eme22.kumaanime.AppUtils.AnimeObjects.server.Server;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.AppUtils.NoSourceFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceFetcher implements Runnable {

    int TYPE;
    String url;
    Callback callback;

    public SourceFetcher(int TYPE, String url, Callback callback) {
        this.TYPE = TYPE;
        this.url = url;
        this.callback = callback;
    }

    @Override
    public void run() {
        Document doc = null;
        try {
           doc = Connection.getDocOk(url);
        } catch (IOException e) {
            callback.onError(e);
            return;
        }
        switch (TYPE){
            default:
            case 0: flvsource(doc);break;
            case 1: jksource(doc); break;
            case 2: idsource(doc);break;
        }

    }

    private void idsource(Document doc) {
    }

    private void jksource(Document doc) {

    }

    private void flvsource(Document doc) {
        Elements scripts =  doc.getElementsByTag("script");
        for (Element todos : scripts) {
            if (todos.data().contains("var videos =")) {
                Pattern pattern = Pattern.compile(".*var videos = \\{\"SUB\":([^;]*)\\};");
                Matcher matcher = pattern.matcher(todos.data());
                if (matcher.find()) {
                    JSONArray jsonArr = null;
                    try {
                        jsonArr = new JSONArray(matcher.group(1));
                        callback.onSuccess(jsonArr);
                    } catch (JSONException e) {
                        callback.onError(e);
                        break;
                    }

                }
                else callback.onError(new NoSourceFoundException());
                break;
            }
        }
    }

    public class ServerFetcher implements Runnable {

        ArrayList<Server> arrayList;
        Callback callback;

        public ServerFetcher(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {

        }
    }


}
