package com.eme22.kumaanime.AppUtils.NewsObject;

import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.Paging;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Connection;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsListFetcher_v2 implements Runnable{

    private final int PAGE;
    private final int MODE;
    private final Callback c;

    public NewsListFetcher_v2(int page, int mode, Callback c) {
        PAGE = page;
        MODE = mode;
        this.c = c;
    }


    public NewsList getanimes (Integer page) throws IOException {
        Document doc;

        Log.d("NEWS DEBUG", "PAGE: "+page+"MODE: "+MODE);

        switch (MODE) {
            default: {
                if (page == 1)
                    doc = Connection.getDocOk("https://somoskudasai.com/listado-noticias/");
                else
                    doc = Connection.getDocOk("https://somoskudasai.com/listado-noticias/page/" + page + "/");

                NewsList new2 = new NewsList();
                Paging paging = new Paging();
                paging.setNext("https://somoskudasai.com/listado-noticias/page/" + page + 1 + "/");
                new2.setData(fetchwithflv(doc));
                new2.setPaging(paging);
                return new2;
            }
            case 1: {
                if (page == 1) {
                    doc = Connection.getDocOk("https://myanimelist.net/news");
                } else {
                    doc = Connection.getDocOk("https://myanimelist.net/news?p=" + page);
                }

                return fetchwithmal(doc);
            }
            case 2: {
                if (page == 1) {
                    doc = Connection.getDocOk("http://misiontokyo.com/category/noticias/");
                } else {
                    doc = Connection.getDocOk("http://misiontokyo.com/category/noticias/page/" + page);
                }

                return fetchwithtokio(doc);
            }
            case 3: {
                if (page == 1) {
                    doc = Connection.getDocOk("https://ramenparados.com/category/noticias/anime/");
                } else {
                    doc = Connection.getDocOk("https://ramenparados.com/category/noticias/anime/page/" + page);
                }

                return fetchwithramen(doc);
            }
        }

    }

    private NewsList fetchwithramen(Document doc) {
        NewsList alist = new NewsList();
        String paging_next = doc.select("link[rel=next]").attr("href");
        String paging_prev = doc.select("link[rel=prev]").attr("href");
        Paging paging = new Paging();
        paging.setNext(paging_next);
        paging.setPrevious(paging_prev);
        alist.setPaging(paging);
        Elements eles = doc.select("li.infinite-post");
        ArrayList<Datum> list = new ArrayList<>();
        for (Element e : eles) {
            Datum d = new Datum();
            String link = e.select("a").attr("href");
            String img = e.select("img").attr("src");
            String title = e.select("a").text().replace("Leer más   ", "");
            String date = e.getElementsByClass("widget-post-date").first().text()+"\n Por: "+e.getElementsByClass("widget-post-author").first().text();
            d.setImage(img);
            d.setLink(link);
            d.setTitle(title);
            d.setDate(date);
            d.setType(3);
            list.add(d);
        }
        alist.setData(list);
        return alist;
    }

    List<Datum> fetchwithflv(Document doc){
        Elements eles = doc.select("article[class~=News b .*]");
        ArrayList<Datum> l = new ArrayList<>();

        for (Element e : eles) {
            Datum d = new Datum();
            String link = e.select("a").attr("href");
            String img = e.select("img").attr("src");
            String date = e.select("p").get(0).text().replaceFirst(" ", "\nPor: ");
            String title = e.select("h2[class=Title] > a").text();
            d.setImage(img);
            d.setLink(link);
            d.setTitle(title);
            d.setDate(date);
            d.setType(0);
            l.add(d);
        }
        return l;
    }

    NewsList fetchwithtokio(Document doc){
        NewsList alist = new NewsList();
        String paging_next = doc.select("link[rel=next]").attr("href");
        String paging_prev = doc.select("link[rel=prev]").attr("href");
        Paging paging = new Paging();
        paging.setNext(paging_next);
        paging.setPrevious(paging_prev);
        alist.setPaging(paging);
        ArrayList<Datum> list = new ArrayList<>();
        Elements eles = doc.selectFirst("section").getElementsByClass("row");
        Log.d("ELEMENTS NEWS", String.valueOf(eles.size()));
        for (Element e: eles) {
            Datum d = new Datum();
            Element e2 = e.selectFirst("a");
            String link = e2.attr("href");
            String img = e2.select("img").attr("data-src");
            String title = e2.attr("title");
            String date =  e.select("div.time").text()+"\n Por:"+e.select("strong[itemprop=author]").text();
            d.setTitle(title);
            d.setImage(img);
            d.setLink(link);
            d.setDate(date);
            d.setType(2);
            list.add(d);
        }
        alist.setData(list);
        return alist;

    }

    NewsList fetchwithmal(Document doc){
        NewsList alist = new NewsList();
        String paging_next = doc.select("link[rel=next]").attr("href");
        String paging_prev = doc.select("link[rel=prev]").attr("href");
        Paging paging = new Paging();
        paging.setNext(paging_next);
        paging.setPrevious(paging_prev);
        alist.setPaging(paging);
        ArrayList<Datum> list = new ArrayList<>();
        Elements eles = doc.select("div[class=news-unit clearfix rect]");
        for (Element e: eles) {
            Datum d = new Datum();
            String link = e.select("a").attr("href");
            String img = e.select("img").attr("src");
            String date = e.select("p[class=info di-ib]").text().replaceFirst("by ", "\nPor: ");
            String title = StringEscapeUtils.unescapeHtml4(e.select("a").get(1).text());
            d.setTitle(title);
            d.setImage(img);
            d.setLink(link);
            d.setDate(date);
            d.setType(1);
            list.add(d);
        }
        alist.setData(list);
        return alist;
    }


    @Override
    public void run() {
        try {
            NewsList l =  getanimes(PAGE);
            c.onSuccess(l);
        } catch (IOException e) {
            c.onError(e);
        }
    }
}
