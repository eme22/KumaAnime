package com.eme22.kumaanime.AnimeActivity_fragments.Utils;

import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.AppUtils.StringUtils;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class EpisodeFetcher implements Runnable{

    MiniAnime ANIME;
    Callback callback;
    private  ArrayList<MiniEpisode> FLVEps = new ArrayList<>();
    private  ArrayList<MiniEpisode> JKEps = new ArrayList<>();
    private  ArrayList<MiniEpisode> IDEps = new ArrayList<>();

    public EpisodeFetcher(MiniAnime ANIME, Callback callback) {
        this.ANIME = ANIME;
        try {
            Log.d("ANIME LOCAL ID:", String.valueOf(ANIME.getId()));
        }
        catch (Exception e){e.printStackTrace();}
        this.callback = callback;
    }


    @Override
    public void run() {

    init();

    }

    private void init() {

        for (String data: StringUtils.getLinks(ANIME.getLink())) {
            Log.d("DATA", data);
            try {
                Document data2 = Connection.getDocOk(data);
                if (data.contains("animeflv"))  FLVEps = Connection.geteps(data2,0, ANIME,data);
                else if (data.contains("jkanime")) JKEps = Connection.geteps(data2, 1, ANIME,data);
                else if (data.contains("animeid")) IDEps = Connection.geteps(data2, 2, ANIME, data);
                else callback.onError(new NullPointerException());

            } catch (IOException e) {
                e.printStackTrace();
                callback.onError(e);
            } catch (Exception e){ e.printStackTrace(); }


        }

        ArrayList<MiniEpisode> all2 = compareepisodes();
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            all2.sort(Comparator.comparing(MiniEpisode::getEpisode));
        else
        */
        Collections.sort(all2, new Comparator<MiniEpisode>() {
            @Override
            public int compare(MiniEpisode o1, MiniEpisode o2) {
                return extractInt(o1.getEpisode()) - extractInt(o2.getEpisode());
            }
            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        Collections.reverse(all2);

        callback.onSuccess(all2);


    }

    private ArrayList<MiniEpisode> compareepisodes() {

        Log.d("flv", String.valueOf(FLVEps.size()));
        Log.d("jk", String.valueOf(JKEps.size()));
        Log.d("id", String.valueOf(IDEps.size()));

        for (int i = 0; i < JKEps.size(); i++) {
            Iterator<MiniEpisode> idelem = IDEps.iterator();
            while (idelem.hasNext()) {
                MiniEpisode idelem2 = idelem.next();
                if (idelem2.getEpisode().equals(JKEps.get(i).getEpisode())){
                    JKEps.get(i).setLink(JKEps.get(i).getLink() + "," + idelem2.getLink());
                    idelem.remove();
                }
            }
        }

        for (int i = 0; i < FLVEps.size() ; i++) {
            Iterator<MiniEpisode> idelem = IDEps.iterator();
            while (idelem.hasNext()) {
                MiniEpisode idelem2 = idelem.next();
                if(idelem2.getEpisode().equals(FLVEps.get(i).getEpisode())){
                    FLVEps.get(i).setLink(FLVEps.get(i).getLink() + "," + idelem2.getLink());
                    idelem.remove();
                }
            }
            Iterator<MiniEpisode> jkelem = JKEps.iterator();
            while (jkelem.hasNext()) {
                MiniEpisode jkelem2 = jkelem.next();
                if (jkelem2.getEpisode().equals(FLVEps.get(i).getEpisode())) {
                    FLVEps.get(i).setLink(FLVEps.get(i).getLink() + "," + jkelem2.getLink());
                    jkelem.remove();
                }
            }
        }
        FLVEps.addAll(JKEps);
        FLVEps.addAll(IDEps);

        return FLVEps;
    }
}
