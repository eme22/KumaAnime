package com.eme22.kumaanime.AppUtils.AnimeObjects;

import android.content.Context;
import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details.AnimeDetails;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

import retrofit2.Call;

public class AnimFetcher implements Callable<GeneralAnime> {

    boolean mal;

    MiniAnime anime;
    Call<AnimeDetails> details;
    MyAnimeListAPIService rss;
    Context context;

    public AnimFetcher(Context context, MiniAnime anime) {
        this.context = context;
        this.anime = anime;
    }

    public GeneralAnime fetchanime() throws IOException {
       GeneralAnime anime_2 = new GeneralAnime();
        final String sources = anime.getLink();

        Integer id = anime.getId();
        if(id!=null) {anime_2.setMalId(anime.getId()); mal = true;}
        else mal = false;
       if (mal){
           rss = MyAnimeListAPIAdapter.getApiServiceWithAuth(context);
           details = rss.getanimedetails(anime.getId(),"id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,recommendations,studios,");
           anime_2.setDetails(details.execute().body());
           if (sources == null){
               String sources_2 = animesearchsources(anime.getTitle());
               String desc2 = searchdesc(sources_2);
               anime_2.setDesc_2(desc2);
           }
           else {
               String desc2 = searchdesc(sources);
               anime_2.setDesc_2(desc2);
           }
       }
       else{
           if (sources == null){
               String sources_2 = animesearchsources(anime.getTitle());
               String desc2 = searchdesc(sources_2);
               anime_2.setDesc_2(desc2);
           }
           else {
               String desc2 = searchdesc(sources);
               anime_2.setDesc_2(desc2);
           }
       }

       //Log.d("DATA", String.valueOf(anime_2.getDetails().getNumEpisodes()));
       return anime_2;
    }

    @Override
    public GeneralAnime call() throws Exception {
        return fetchanime();
    }

    //TODO: AGREGAR GENERO

    String searchdesc(String d) throws IOException {
        String[] sources = d.split(",");
        int type;

        if (sources[0].contains("animeflv")){
            type = 0;
        }
        else if (sources[0].contains("jkanime")){
            type = 1;
        }
        else if (sources[0].contains("animeid")){
            type = 2;
        }
        else return null;

        Document page = Connection.getDocOk(sources[0]);

        return searchfordesc(page, type);
    }

    private String searchfordesc(Document page, int type) {
        switch (type){
            default: { return page.select("div[class=Description]").text(); }
            case 1:{return page.select("meta[name=description]").attr("content");}
            case 2:{return Parser.unescapeEntities(page.select("p[class=sinopsis]").text(),false);}
        }
    }

    String animesearchsources(String animetitle) throws IOException {
        StringBuilder sources = null;
        MiniAnimeTable_Repo repo  = new MiniAnimeTable_Repo(context);
        LevenshteinDistance comparor = LevenshteinDistance.getDefaultInstance();
        int i = 0;
        while (i<3){
            switch (i){
                default:{
                    String baseurl1 = "https://www3.animeflv.net";
                    String baseurl2 = "/browse?q=";
                    Document result = Connection.getDocOk(new URL(baseurl1+baseurl2+animetitle).toString());
                    String animeslist;
                    int sim;
                    try {
                        animeslist =  result.select("ul.ListAnimes ").select("a[href]").first().attr("href");
                        sim = comparor.apply(animetitle,animeslist.replace("/anime/","").replace("-"," "));
                    } catch (NullPointerException e){
                        break;
                    }
                    if(sim<5){
                    if (sources != null) sources.append(",").append(baseurl1).append(animeslist);
                    else sources = new StringBuilder(baseurl1+animeslist);}
                    break;
                }
                case 1:{
                    String baseurl1 =  "https://jkanime.net";
                    String baseurl2 = "/buscar/";
                    Document result = Connection.getDocOk(new URL(baseurl1+baseurl2+animetitle+"/1/").toString());
                    String animeslist;
                    int sim;
                    try {
                        animeslist = result.select("div.full-container").select("a[href]").first().attr("href");
                        sim = comparor.apply(animetitle,animeslist.replace("https://jkanime.net/", "").replace("/",""));
                    }
                    catch (NullPointerException e){
                        break;
                    }
                    if(sim<5){
                    if (sources != null) sources.append(",").append(animeslist);
                    else sources = new StringBuilder(animeslist);}
                    break;
                }
                case 2:{
                    String baseurl1 = "https://www.animeid.tv/";
                    if (sources != null) sources.append(",").append(baseurl1).append(animetitle.replaceAll("[^a-zA-Z0-9 ]","").replace(" ","-"));
                    else sources = new StringBuilder(baseurl1+animetitle.replaceAll("[^a-zA-Z0-9 ]","").replace(" ","-"));
                    break;}
            }
            i++;
        }
        Log.d("SOURCES: ", sources.toString());
        assert sources != null;
        return sources.toString();
    }


}
