package com.eme22.kumaanime.AnimeActivity_fragments.Utils;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details.AnimeDetails;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_list.AnimeList;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.AppUtils.AnimeObjects.GeneralAnime;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.AppUtils.StringUtils;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;
import com.eme22.kumaanime.GeneralAnimeActivity;
import com.eme22.kumaanime.MainActivity_fragments.util.NewAnimeFetcher_v3;
import com.tingyik90.prefmanager.PrefManager;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;

public class AnimeFetcher_v2 implements Runnable{

    GeneralAnime THIS;
    MiniAnime anime;
    Call<AnimeDetails> details;
    MyAnimeListAPIService rss;
    Context context;
    Callback callback;
    MiniAnimeTable_Repo repo;
    ArrayList<NewAnimeFetcher_v3.Tuple<Document,String>> links;
    PrefManager prefManager;
    boolean readOnly = false;

    public AnimeFetcher_v2(MiniAnime anime, Context context, Callback callback) {
        this.anime = anime;
        this.context = context;
        this.callback = callback;
        this.rss = MyAnimeListAPIAdapter.getApiServiceWithAuth(context);
        this.repo = new MiniAnimeTable_Repo(context);
        this.prefManager = new PrefManager(context);
        this.THIS =new GeneralAnime();
    }

    void preInit(){
        links = new ArrayList<>();
        if (anime.getLink() != null){
            catchLinks();
        }
        else {
            MiniAnime anime2 = repo.getanime(anime.getTitle());
            Log.d("ANIME TO FIND: ", anime2.getTitle());
            if (anime2 == null || anime2.getTitle().equals("")){
                anime2 = repo.getanime(StringUtils.replacesforSQL(anime.getTitle()));
                if (anime2 == null){
                    Log.d("ANIME", "No Sources Found");
                    readOnly = true;
                }
                else {
                    Log.d("ANIME FOUND: ", anime2.getTitle());
                    anime = anime2;
                    catchLinks();
                    if (context instanceof GeneralAnimeActivity) ((GeneralAnimeActivity) context).setAnimeLinks(anime.getLink());
                }
            }
            else {
                Log.d("ANIME FOUND: ", anime2.getTitle());
                anime = anime2;
                catchLinks();
                if (context instanceof GeneralAnimeActivity) ((GeneralAnimeActivity) context).setAnimeLinks(anime.getLink());
            }
        }

    }

    void catchLinks(){
        for (String source: anime.getLink().split(",")) {
            Document data = null;
            try {
                data = Connection.getDocOk(source);
            }
            catch (IOException e) {
                callback.onError(e);
            }
            links.add(new NewAnimeFetcher_v3.Tuple<Document, String>().withFirst(data).withSecond(source));
        }
    }

    void init(){
        if (prefManager.getBoolean("isLogged",false)){
            MiniAnime TEMP = null;
            AnimeDetails TEMP_DETAILS = null;
            try {
                String title = anime.getTitle();
                title = (title.length() > 65) ? title.substring(0,65) : title;

                Log.d("Anime Title: ", title);

                AnimeList TEMP0 = rss.getanimelist(title, 1, 0).execute().body();

                if (TEMP0 != null) {
                    TEMP = TEMP0.getData().get(0).getNode();
                    /*
                    if (TEMP == null) {
                        nulldata();
                        return;
                    }
                     */
                }
                else
                    throw new NullPointerException();

            }
            catch (SocketTimeoutException e){
                nulldata();
            }

            catch (IndexOutOfBoundsException e){
            nulldata();
            return;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                TEMP_DETAILS = rss.getanimedetails(TEMP.getId(),"id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,recommendations,studios,").execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String desc2 = null;
            try {
                if (!readOnly)  desc2 = searchdesc(links.get(0));
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }



            THIS.setDetails(TEMP_DETAILS);
            if (!readOnly) THIS.setDesc_2(desc2);

        }

    }
    private void nulldata(){
        THIS.setDetails(new AnimeDetails(){
            @Override
            public Integer getId() {
                return -1;
            }
        });
        if (!readOnly) THIS.setDesc_2(searchdesc(links.get(0)));
    }

    private String searchdesc(NewAnimeFetcher_v3.Tuple<Document, String> documentStringTuple) {
        int type;
        if (documentStringTuple.getSecond().contains("jkanime")){
            type = 1;
        }
        else if (documentStringTuple.getSecond().contains("animeid")){
            type = 2;
        }
        else {
            type = 0;
        }
        return searchfordesc(documentStringTuple.getFirst(), type);
    }

    private String searchfordesc(Document page, int type) {
        switch (type){
            default: { return page.select("div[class=Description]").text(); }
            case 1:{return page.select("meta[name=description]").attr("content");}
            case 2:{return Parser.unescapeEntities(page.select("p[class=sinopsis]").text(),false);}
        }
    }


    @Override
    public void run() {
        preInit();
        init();
        callback.onSuccess(new AnimeDetailsDataset(THIS,links));
    }

    public static class AnimeDetailsDataset{
        GeneralAnime data1;
        ArrayList<NewAnimeFetcher_v3.Tuple<Document,String>> links;

        public AnimeDetailsDataset(GeneralAnime data1, ArrayList<NewAnimeFetcher_v3.Tuple<Document, String>> links) {
            this.data1 = data1;
            this.links = links;
        }

        public GeneralAnime getData1() {
            return data1;
        }

        public void setData1(GeneralAnime data1) {
            this.data1 = data1;
        }

        public ArrayList<NewAnimeFetcher_v3.Tuple<Document, String>> getLinks() {
            return links;
        }

        public void setLinks(ArrayList<NewAnimeFetcher_v3.Tuple<Document, String>> links) {
            this.links = links;
        }
    }

    public interface Callback{
        void onSuccess(AnimeDetailsDataset animeDetailsDataset);
        void onError(Exception e);
    }

}
