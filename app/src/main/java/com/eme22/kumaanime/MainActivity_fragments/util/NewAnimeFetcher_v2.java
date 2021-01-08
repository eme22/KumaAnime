package com.eme22.kumaanime.MainActivity_fragments.util;

import android.content.Context;
import android.util.Log;

import com.eme22.kumaanime.App;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.AppUtils.StringUtils;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

public class NewAnimeFetcher_v2 implements Callable<NewAnimeFetcher_v2.Tuple<ArrayList<MiniAnime>, ArrayList<MiniEpisode>>> {

    private static MiniAnimeTable_Repo repo = new MiniAnimeTable_Repo(App.getInstance());
    ArrayList<MiniAnime> all;
    ArrayList<MiniEpisode> all2;

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

    public NewAnimeFetcher_v2(Context context) {
        repo = new MiniAnimeTable_Repo(context);
    }
    @Override
    public Tuple<ArrayList<MiniAnime>, ArrayList<MiniEpisode>> call() throws IOException {

        Document newJK = Connection.getDocOk("https://jkanime.net/");
        Document newFLV = Connection.getDocOk("https://www3.animeflv.net/");
        Document newID = Connection.getDocOk("https://www.animeid.tv/");

        ArrayList<MiniAnime> FLVAnimes = Connection.getnewanimes(newFLV, 0);
        ArrayList<MiniAnime> JKAnimes = Connection.getnewanimes(newJK, 1);
        ArrayList<MiniAnime> IDAnimes = Connection.getnewanimes(newID, 2);
        ArrayList<MiniEpisode> FLVEps = Connection.getneweps(newFLV,0);
        ArrayList<MiniEpisode> JKEps = Connection.getneweps(newJK, 1);
        ArrayList<MiniEpisode> IDEps = Connection.getneweps(newID, 2);

        all = compareanimes(FLVAnimes,JKAnimes,IDAnimes);
        all2 = compareepisodes(FLVEps,JKEps,IDEps);

        Log.d("SIZE NEW 1", String.valueOf(all.size()));
        Log.d("SIZE NEW 2", String.valueOf(all2.size()));
        fetchtonDB(all);

        NewAnimeFetcher_v2.Tuple<ArrayList<MiniAnime>,ArrayList<MiniEpisode>> result = new NewAnimeFetcher_v2.Tuple<>();
        result.setFirst(all);
        result.setSecond(all2);

        return result;
    }

    private ArrayList<MiniEpisode> compareepisodes(ArrayList<MiniEpisode> FLV, ArrayList<MiniEpisode> JK, ArrayList<MiniEpisode> ID) {

        Log.d("flv", String.valueOf(FLV.size()));
        Log.d("jk", String.valueOf(JK.size()));
        Log.d("id", String.valueOf(ID.size()));

        for (int i = 0; i < JK.size() ; i++) {
            Iterator<MiniEpisode> idelem = ID.iterator();
            while (idelem.hasNext()) {
                MiniEpisode idelem2 = idelem.next();
                if(StringUtils.compareAnimes(JK.get(i).getName(),idelem2.getName())&& StringUtils.compareEpisodes(JK.get(i).getEpisode(),idelem2.getEpisode())){
                    JK.get(i).setLink(JK.get(i).getLink() + "," + idelem2.getLink());
                    idelem.remove();
                }
            }
        }

        for (int i = 0; i < FLV.size() ; i++) {
            Iterator<MiniEpisode> idelem = ID.iterator();
            while (idelem.hasNext()) {
                MiniEpisode idelem2 = idelem.next();
                if(StringUtils.compareAnimes(FLV.get(i).getName(),idelem2.getName())&& StringUtils.compareEpisodes(FLV.get(i).getEpisode(),idelem2.getEpisode())){
                    FLV.get(i).setLink(FLV.get(i).getLink() + "," + idelem2.getLink());
                    idelem.remove();
                }
            }
            Iterator<MiniEpisode> jkelem = JK.iterator();
            while (jkelem.hasNext()) {
                MiniEpisode jkelem2 = jkelem.next();
                if (StringUtils.compareAnimes(FLV.get(i).getName(),jkelem2.getName())&& StringUtils.compareEpisodes(FLV.get(i).getEpisode(),jkelem2.getEpisode())) {
                    FLV.get(i).setLink(FLV.get(i).getLink() + "," + jkelem2.getLink());
                    jkelem.remove();
                }
            }
        }
        FLV.addAll(JK);
        FLV.addAll(ID);

        return FLV;
    }

    private void fetchtonDB(ArrayList<MiniAnime> all) {
        ArrayList<MiniAnime> unknown = new ArrayList<>();
        for (int i = 0; i < all.size() ; i++) {
            MiniAnime anim2e;

            anim2e = repo.getanime(all.get(i).getTitle());

            if (anim2e == null) unknown.add(all.get(i));
            else {
                Log.d("GAAAAA" , anim2e.getTitle());
                Log.d("GAAAAA" , String.valueOf(anim2e.getId()));
                all.set(i, anim2e);
            }
        }


        for (MiniAnime e: unknown) {
            try {
                repo.insert(e);
            }
            catch (NullPointerException ignore){}

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
                //int sim = LevenshteinDistance.getDefaultInstance().apply(idelem2.getTitle().toUpperCase(), JK.get(i).getTitle().toUpperCase());
                //Log.d("ANIME SIMIL: ", String.valueOf(sim));
                if (
                    //sim < 3 &&
                        idelem2.getShow_type() == JK.get(i).getShow_type() && StringUtils.compareAnimes(idelem2.getTitle(),JK.get(i).getTitle())) {
                    JK.get(i).setLink(JK.get(i).getLink() + "," + idelem2.getLink());
                    idelem.remove();
                }
            }
        }

        for (int i = 0; i < FLV.size(); i++) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()) {
                MiniAnime idelem2 = idelem.next();
                if (idelem2.getShow_type() == FLV.get(i).getShow_type() && StringUtils.compareAnimes(FLV.get(i).getTitle(),idelem2.getTitle())) {
                    FLV.get(i).setLink(FLV.get(i).getLink() + "," + idelem2.getLink());
                    idelem.remove();
                }
            }
            Iterator<MiniAnime> jkelem = JK.iterator();
            while (jkelem.hasNext()) {
                MiniAnime jkelem2 = jkelem.next();
                if (jkelem2.getShow_type() == FLV.get(i).getShow_type() && StringUtils.compareAnimes(FLV.get(i).getTitle(),jkelem2.getTitle())) {
                    FLV.get(i).setLink(FLV.get(i).getLink() + "," + jkelem2.getLink());
                    jkelem.remove();
                }
            }
        }
        FLV.addAll(JK);
        FLV.addAll(ID);

        return FLV;
    }
}
