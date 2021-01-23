package com.eme22.kumaanime.AnimeActivity_fragments.Utils;

import android.app.Activity;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.PermissionActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EpisodeFetcherOffline implements Runnable{

    private MiniAnime ANIME;
    private EpisodeFetcherOfflineCallback callback;
    private ArrayList<MiniEpisode> data;
    private DownloadManager manager;

    public EpisodeFetcherOffline(PermissionActivity context, MiniAnime ANIME, EpisodeFetcherOfflineCallback callback) {
        this.manager = DownloadManager.getInstance(context);
        this.ANIME = ANIME;
        this.callback = callback;
        this.data = new ArrayList<>();
    }

    @Override
    public void run() {
        init();
    }

    private void init() {

        try {
            File[] filesIn = manager.getAnime(ANIME.getId()).listFiles();
            HashMap<Integer,MiniEpisodeOffline> episodeHashMap = new HashMap<>();
            MiniEpisodeOffline episodebase = new MiniEpisodeOffline();
            episodebase.setAnimeID(ANIME.getId());

            if (filesIn != null && filesIn.length > 1){
                for (File f: filesIn) {
                    findImage(f,episodeHashMap);
                    findEpisode(f,episodeHashMap);
                }
            }

            callback.onSuccess(new ArrayList<>(episodeHashMap.values()));
        }
        catch (Exception e){
            callback.onFailure(e);
        }



    }

    public void findImage(File f, HashMap<Integer,MiniEpisodeOffline> episodeHashMap){
        Pattern p = Pattern.compile(ANIME.getId()+"_(.*)\\.png");//. represents single character
        Matcher m = p.matcher(f.getName());
        if (m.find()){
            if (m.group(1) == null) return;
            MiniEpisodeOffline episodeTMP = episodeHashMap.get(Integer.parseInt(m.group(1)));
            if (episodeTMP == null) {
                episodeTMP = new MiniEpisodeOffline();
            }
            episodeTMP.setImage(f);
            episodeHashMap.put(Integer.parseInt(m.group(1)),episodeTMP);
        }
    }

    public void findEpisode(File f, HashMap<Integer,MiniEpisodeOffline> episodeHashMap){
        Pattern p = Pattern.compile(ANIME.getId()+"_(.*)\\.mp4");//. represents single character
        Matcher m = p.matcher(f.getName());
        if (m.find()){
            if (m.group(1) == null) return;
            MiniEpisodeOffline episodeTMP = episodeHashMap.get(Integer.parseInt(m.group(1)));
            if (episodeTMP == null) {
                episodeTMP = new MiniEpisodeOffline();
            }
            episodeTMP.setEpisode_file(f);
            episodeTMP.setEpisode(m.group(1));
            episodeHashMap.put(Integer.parseInt(m.group(1)),episodeTMP);
        }
    }

    public interface EpisodeFetcherOfflineCallback{
        void onSuccess(ArrayList<MiniEpisodeOffline> data);
        void onFailure(Exception e);
    }
}
