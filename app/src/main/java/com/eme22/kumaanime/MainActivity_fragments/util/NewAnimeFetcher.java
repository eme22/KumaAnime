package com.eme22.kumaanime.MainActivity_fragments.util;

import android.app.Application;
import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

public class NewAnimeFetcher implements Callable<NewAnimeFetcher.Tuple<ArrayList<MiniAnime>, ArrayList<MiniEpisode>>> {

    MiniAnimeTable_Repo repo;
    private Document newJK;
    private Document newFLV;
    private Document newID;

    public NewAnimeFetcher(Application context) {
        this.repo = new MiniAnimeTable_Repo(context);
    }

    public static class Tuple<A,B>{
        A first;
        B second;

        public void setFirst(A first) {
            this.first = first;
        }

        public void setSecond(B second) {
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }

    @Override
    public Tuple<ArrayList<MiniAnime>, ArrayList<MiniEpisode>> call() throws IOException {
        newJK = Connection.getDocOk("https://jkanime.net/");
        newFLV = Connection.getDocOk("https://www3.animeflv.net/");
        newID = Connection.getDocOk("https://www.animeid.tv/");

        ArrayList<MiniAnime> animes = fetchOffline();
        Log.d("HELLO", String.valueOf(animes.size()));
        //ArrayList<MiniEpisode> episodes = null;

        Tuple<ArrayList<MiniAnime>,ArrayList<MiniEpisode>> result = new Tuple<>();
        result.setFirst(animes);
        result.setSecond(null);

        return result;
    }

    private ArrayList<MiniAnime> fetchOffline(){
        ArrayList<MiniAnime> FLVAnimes = Connection.getnewanimes(newFLV, 0);
        ArrayList<MiniAnime> JKAnimes = Connection.getnewanimes(newJK, 1);
        ArrayList<MiniAnime> IDAnimes = Connection.getnewanimes(newID, 2);
        ArrayList<MiniAnime> all = compareanimes(FLVAnimes,JKAnimes,IDAnimes);
        return fetchtonDB(all);
    }

    private ArrayList<MiniAnime> fetchtonDB(ArrayList<MiniAnime> all) {
        for (int i = 0; i < all.size() ; i++) {
            MiniAnime anim2e = null;

            try {
                anim2e = repo.getanime(all.get(i).getTitle());
            } catch (Exception ignored) {
            }

            if (anim2e == null) { repo.insert(all.get(i));
            }
            else all.set(i, anim2e);
        }
        return all;
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
