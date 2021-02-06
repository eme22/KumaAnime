package com.eme22.kumaanime;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.eme22.kumaanime.AnimeActivity_fragments.AnimeInfo;
import com.eme22.kumaanime.AnimeActivity_fragments.Utils.AnimeFetcher_v2;
import com.eme22.kumaanime.MainActivity_fragments.util.NewAnimeFetcher_v3;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class AnimeActivity extends GeneralAnimeActivity_v2 {

    int episode = -1;
    int malid = 0;
    private com.eme22.kumaanime.AppUtils.AnimeObjects.GeneralAnime result;
    private ArrayList<NewAnimeFetcher_v3.Tuple<Document, String>> links;
    boolean loaded = false;
    protected AnimeActivityLoad animeinfo;
    protected AnimeActivityLoad animeeps;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        getAnime();
        initViewOnline();
        initViewPager();
        initData();
    }


    private void initData() {
        if(prefs.getBoolean("isLogged",false)){
            taskRunner.executeAsync(new AnimeFetcher_v2(getGeneralAnime(), this, new AnimeFetcher_v2.Callback() {

                @Override
                public void onSuccess(AnimeFetcher_v2.AnimeDetailsDataset animeDetailsDataset) {
                    setResult(animeDetailsDataset.getData1());
                    setLinks(animeDetailsDataset.getLinks());

                    try {
                        episode = animeDetailsDataset.getData1().getDetails().getMyListStatus().getNumEpisodesWatched();
                    }
                    catch (NullPointerException ignored){ }

                    try {
                        malid = animeDetailsDataset.getData1().getDetails().getId();
                    }
                    catch (NullPointerException ignored){ }

                    runOnUiThread(() -> {
                        //refreshLayout.setEnabled(false);
                        loaded = true;
                        try {
                            animeinfo.loadData();
                        }
                        catch (NullPointerException ignored){}
                        try {
                            animeeps.loadData();
                        }
                        catch (NullPointerException ignored){}



                    });
                }

                @Override
                public void onError(Exception e) {

                }
            }));
        }
    }

    public com.eme22.kumaanime.AppUtils.AnimeObjects.GeneralAnime getResult() {
        return result;
    }

    public void setResult(com.eme22.kumaanime.AppUtils.AnimeObjects.GeneralAnime result) {
        this.result = result;
    }

    public ArrayList<NewAnimeFetcher_v3.Tuple<Document, String>> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<NewAnimeFetcher_v3.Tuple<Document, String>> links) {
        this.links = links;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setEpisodeAndUpdate(int episode) {
        this.episode = episode;
        //TODO: UPDATE EPISODE IN FRAGMENT METHOD
    }

    public int getMalid() {
        return malid;
    }

    public long getEpisode() {
        return episode;
    }

    public void setAnimeinfo(AnimeActivityLoad animeinfo) {
        this.animeinfo = animeinfo;
    }

    public void setAnimeeps(AnimeActivityLoad animeeps) {
        this.animeeps = animeeps;
    }

    public interface AnimeActivityLoad{
        void loadData();
    }
}
