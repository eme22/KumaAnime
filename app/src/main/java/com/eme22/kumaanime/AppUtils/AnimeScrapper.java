package com.eme22.kumaanime.AppUtils;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class AnimeScrapper {


    public static ArrayList<String> scrape(Document doc, int type, ArrayList<String> urllist){
        if (urllist == null) urllist = new ArrayList<>();
        int length = 0;
         int end = 0;

         switch (type){
             case 0:{end = 30; length =26;break;}
             case 1:{end = 60; length =24;break;}
             case 2:{end = 150; length =23;break;}
         }
         Elements links = doc.select("a");
                boolean esanime;
                for (Element todos : links) {
                        String actualUrl = todos.attr("href");
                        //Log.d("LINK_ALL", actualUrl);
                        if (type == 2) {
                            actualUrl = "https://www.animeid.tv"+actualUrl;
                            esanime = (!urllist.contains(actualUrl)) && !(actualUrl.contains("/letra/") || actualUrl.contains("/genero/") || actualUrl.contains("/especiales") || actualUrl.contains("facebook") || actualUrl.contains("youtube") || actualUrl.contains("google.com") || actualUrl.contains("/series") || actualUrl.contains("/ovas") || actualUrl.contains("/peliculas")) && !(actualUrl.length() <= "https://www.animeid.tv".length());
                        } else if (type == 1) {
                            esanime = (!urllist.contains(actualUrl)) && !(actualUrl.contains("/letra/") || actualUrl.contains("/genero/") || actualUrl.contains("/terminos") || actualUrl.contains("discord.gg") || actualUrl.contains("/horario/") || actualUrl.contains("tipo")) && !(actualUrl.length() <= "https://jkanime.net/".length());
                        } else {
                            actualUrl = "https://www3.animeflv.net"+actualUrl;
                            esanime = (!urllist.contains(actualUrl)) && (actualUrl.contains("/anime/") && !(actualUrl.length() <= "https://www3.animeflv.net/anime/".length()));
                        }

                        if (esanime) {
                            Log.d("LINK_THIS", actualUrl);
                            urllist.add(actualUrl);
                        }
                }
        //Log.d("SIZE", String.valueOf(urllist.size()));
            return urllist;
    }
}
