package com.eme22.kumaanime.MainActivity_fragments.util;

import com.eme22.kumaanime.AppUtils.Connection;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

public class AnimeSearchSources implements Callable<String> {

    String animetitle;

    public AnimeSearchSources(String animetitle) {
        this.animetitle = animetitle;
    }

    @Override
    public String call() throws IOException {
        StringBuilder sources = null;
        int i = 0;
        while (i<3){
            switch (i){
                default:{
                    String baseurl1 = "https://www3.animeflv.net";
                    String baseurl2 = "/browse?q=";
                    Document result = Connection.getDocOk(new URL(baseurl1+baseurl2+animetitle).toString());
                    Element animeslist = result.select("ul[class=ListAnimes ] > article").first();
                    if (animeslist == null) break;
                    if (sources != null) sources.append(",").append(baseurl1).append(animeslist.select("a").attr("href"));
                    else sources = new StringBuilder(baseurl1+animeslist.select("a").attr("href"));
                    break;
                }
                case 1:{
                    String baseurl1 =  "https://jkanime.net";
                    String baseurl2 = "/buscar/";
                    Document result = Connection.getDocOk(new URL(baseurl1+baseurl2+animetitle+"/1/").toString());
                    Element animeslist = result.select("div[class=full-container] > div[id=conb]").first();
                    if (animeslist == null) break;
                    if (sources != null) sources.append(",").append(animeslist.select("a").first().attr("href"));
                    else sources = new StringBuilder(animeslist.select("a").first().attr("href"));
                    break;}
                case 2:{
                    String baseurl1 = "https://www.animeid.tv/";
                    if (sources != null) sources.append(",").append(baseurl1).append(animetitle.replace(" ", "-"));
                    else sources = new StringBuilder(baseurl1+animetitle.replace(" ","-"));
                    break;}
            }
            i++;
        }


        assert sources != null;
        return sources.toString();
    }
}
