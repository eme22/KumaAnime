package com.eme22.kumaanime.MainActivity_fragments.util;

import android.app.Application;
import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_list.AnimeList;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;
import com.tingyik90.prefmanager.PrefManager;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Deprecated
public class NewAnimeMatcher {

    // FLV = 0, JKA = 1, ID = 2

    private final Application context;

    private Document newJK;// = Connection.getDocOk("https://jkanime.net/");
    private Document newFLV;// = Connection.getDocOk("https://www3.animeflv.net/");
    private Document newID;// = Connection.getDocOk("https://www.animeid.tv/");

    public NewAnimeMatcher(Application context) {
        this.context = context;
    }

    public ArrayList<MiniAnime>  getanimes(){
        PrefManager prefs = new PrefManager(context);
        ArrayList<MiniAnime> FLVAnimes = Connection.getnewanimes(newFLV, 0);
        ArrayList<MiniAnime> JKAnimes = Connection.getnewanimes(newJK, 1);
        ArrayList<MiniAnime> IDAnimes = Connection.getnewanimes(newID, 2);
        //if(prefs.getBoolean("isLogged",false)){ return comparelogedanimes(FLVAnimes,JKAnimes,IDAnimes);}
        //else { return compareanimes(FLVAnimes,JKAnimes,IDAnimes); }
        ArrayList<MiniAnime> all = compareanimes(FLVAnimes,JKAnimes,IDAnimes);
        return fetchtonDB(all);
    }

    private ArrayList<MiniAnime> fetchtonDB(ArrayList<MiniAnime> all) {
        for (int i = 0; i < all.size() ; i++) {
            MiniAnime anim2e = new SearchOnDB(all.get(i), context).call();
            if (anim2e == null) new SaveAnimeInDB(all.get(i),context).call();
            else all.set(i, anim2e);
        }
       return all;
    }

    private ArrayList<MiniAnime> comparelogedanimes(ArrayList<MiniAnime> flvAnimes, ArrayList<MiniAnime> jkAnimes, ArrayList<MiniAnime> idAnimes) {

        ArrayList<MiniAnime> main = compareanimes(flvAnimes,jkAnimes,idAnimes);

        MyAnimeListAPIService rss = MyAnimeListAPIAdapter.getApiServiceWithAuth(context);
        for (int i = 0; i <main.size() ; i++) {
            Call<AnimeList> animesquery = rss.getanimelist(main.get(i).getTitle(),1,0);
            int finalI = i;
            animesquery.enqueue(new Callback<AnimeList>() {
                @Override
                public void onResponse(@NotNull Call<AnimeList> call, @NotNull Response<AnimeList> response) {
                    try {
                        assert response.body() != null;
                        MiniAnime possible = response.body().getData().get(0).getNode();
                        if(main.get(finalI).getLink()!=null) {
                            String link =  main.get(finalI).getLink();
                            main.set(finalI,possible);
                            main.get(finalI).setLink(link);
                        }
                        else {
                            main.set(finalI,possible);
                        }
                        //Log.d("ANIME ID: ", String.valueOf(possible.getId()));
                    }
                    catch ( IndexOutOfBoundsException ignored){}
                }

                @Override
                public void onFailure(@NotNull Call<AnimeList> call, @NotNull Throwable t) {
                    Log.e("ERROR: ", "NOT FOUND");
                }
            });
        }

        return main;

    }
    /*

    private ArrayList<MiniAnime> compareanimes(ArrayList<MiniAnime> FLV, ArrayList<MiniAnime> JK, ArrayList<MiniAnime> ID){

        for (int i = 0; i < JK.size() ; i++) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()){
                MiniAnime idelem2 = idelem.next();
                int sim = LevenshteinDistance.getDefaultInstance().apply(idelem2.getTitle().toUpperCase(),JK.get(i).getTitle().toUpperCase());
                //Log.d("ANIME SIMIL: ", String.valueOf(sim));
                if(sim<5){
                    JK.get(i).setLink(JK.get(i).getLink()+","+idelem2.getLink());
                    idelem.remove();
                }
            }
        }

        for (MiniAnime jkelem: JK) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()){
                if(LevenshteinDistance.getDefaultInstance().apply(idelem.next().getTitle(),jkelem.getTitle())<5){
                    idelem.remove();
                }
            }


        for (int i = 0; i < FLV.size() ; i++) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()){
                MiniAnime idelem2 = idelem.next();
                int sim = LevenshteinDistance.getDefaultInstance().apply(idelem2.getTitle().toUpperCase(),FLV.get(i).getTitle().toUpperCase());
                if (idelem2.getTitle().contains("dungeon")){Log.d("ANIME SIMIL: ", String.valueOf(sim)); }

                if(sim<5){
                    FLV.get(i).setLink(FLV.get(i).getLink()+","+idelem2.getLink());
                    idelem.remove();
                }
            }
            Iterator<MiniAnime> jkelem = JK.iterator();
            while (jkelem.hasNext()){
                MiniAnime jkelem2 = jkelem.next();
                int sim = LevenshteinDistance.getDefaultInstance().apply(jkelem2.getTitle().toUpperCase(),FLV.get(i).getTitle().toUpperCase());
                //Log.d("ANIME SIMIL: ", String.valueOf(sim));
                if(sim<5){
                    FLV.get(i).setLink(FLV.get(i).getLink()+","+jkelem2.getLink());
                    jkelem.remove(); }
            }

        }

        for (MiniAnime flvelem: FLV) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()){
                if(LevenshteinDistance.getDefaultInstance().apply(idelem.next().getTitle(),flvelem.getTitle())<5){
                    flvelem.setLink(flvelem.getLink()+","+idelem.next().getLink());
                    idelem.remove(); }
                //if(idelem.next().getTitle().equals(flvelem.getTitle())) {idelem.remove(); }
            }
            Iterator<MiniAnime> jkelem = JK.iterator();
            while (jkelem.hasNext()){
                if(LevenshteinDistance.getDefaultInstance().apply(jkelem.next().getTitle(),flvelem.getTitle())<5){
                    flvelem.setLink(flvelem.getLink()+","+jkelem.next().getLink());
                    jkelem.remove(); }
                //if(jkelem.next().getTitle().equals(flvelem.getTitle())) {jkelem.remove(); }
            }
        }

        FLV.addAll(JK);
        FLV.addAll(ID);

        return FLV;
    }
 */

    private class offlineFetch implements Callable<ArrayList<MiniAnime>> {

        ArrayList<MiniAnime> animes1;
        ArrayList<MiniAnime> animes2;
        ArrayList<MiniAnime> animes3;
        //ArrayList<MiniAnime> animes4;


        public offlineFetch(ArrayList<MiniAnime> animes1, ArrayList<MiniAnime> animes2, ArrayList<MiniAnime> animes3) {
            this.animes1 = animes1;
            this.animes2 = animes2;
            this.animes3 = animes3;
            //this.animes4 = animes4;
        }

        @Override
        public ArrayList<MiniAnime> call() {
            return compareanimes(animes1,animes2,animes3);
        }

    }

    private ArrayList<MiniAnime> compareanimes(ArrayList<MiniAnime> FLV, ArrayList<MiniAnime> JK, ArrayList<MiniAnime> ID) {


        Log.d("flv", String.valueOf(FLV.size()));
        Log.d("jk", String.valueOf(JK.size()));
        Log.d("id", String.valueOf(ID.size()));

        for (int i = 0; i < JK.size(); i++) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()) {
                MiniAnime idelem2 = idelem.next();
                int sim = LevenshteinDistance.getDefaultInstance().apply(idelem2.getTitle().toUpperCase(), JK.get(i).getTitle().toUpperCase());
                //Log.d("ANIME SIMIL: ", String.valueOf(sim));
                if (sim < 3 && idelem2.getShow_type() == JK.get(i).getShow_type()) {
                    JK.get(i).setLink(JK.get(i).getLink() + "," + idelem2.getLink());
                    idelem.remove();
                }
            }
        }

        for (int i = 0; i < FLV.size(); i++) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()) {
                MiniAnime idelem2 = idelem.next();
                int sim = LevenshteinDistance.getDefaultInstance().apply(idelem2.getTitle().toUpperCase(), FLV.get(i).getTitle().toUpperCase());
                //if (idelem2.getTitle().contains("dungeon")){Log.d("ANIME SIMIL: ", String.valueOf(sim)); }

                if (sim < 3 && idelem2.getShow_type() == FLV.get(i).getShow_type()) {
                    FLV.get(i).setLink(FLV.get(i).getLink() + "," + idelem2.getLink());
                    idelem.remove();
                }
            }
            Iterator<MiniAnime> jkelem = JK.iterator();
            while (jkelem.hasNext()) {
                MiniAnime jkelem2 = jkelem.next();
                int sim = LevenshteinDistance.getDefaultInstance().apply(jkelem2.getTitle().toUpperCase(), FLV.get(i).getTitle().toUpperCase());
                //Log.d("ANIME SIMIL: ", String.valueOf(sim));
                if (sim < 3 && jkelem2.getShow_type() == FLV.get(i).getShow_type()) {
                    FLV.get(i).setLink(FLV.get(i).getLink() + "," + jkelem2.getLink());
                    jkelem.remove();
                }
            }
        }
        FLV.addAll(JK);
        FLV.addAll(ID);

        return FLV;
    }

    private class SearchOnDB implements  Callable<MiniAnime>{

        MiniAnime anime;
        Application application;

        public SearchOnDB(MiniAnime anime, Application application) {
            this.anime = anime;
            this.application = application;
        }

        @Override
        public MiniAnime call() {
            MiniAnimeTable_Repo repo = new MiniAnimeTable_Repo(application);
            try {
                return repo.getanime(anime.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class SaveAnimeInDB implements  Callable<Void>{

        MiniAnime anime;
        Application application;

        public SaveAnimeInDB(MiniAnime anime, Application application) {
            this.anime = anime;
            this.application = application;
        }

        @Override
        public Void call() {
            MiniAnimeTable_Repo repo = new MiniAnimeTable_Repo(application);
            repo.insert(anime);
            return null;
        }
    }



}
