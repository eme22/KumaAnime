package com.eme22.kumaanime.AnimeActivity_fragments;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager_v2;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.PermissionActivity;

public class Download extends Play{

    MiniEpisode episode;
    DownloadManager_v2 managerV2;

    public Download(Context context, FragmentManager manager, MiniEpisode episode) {
        super(context, manager ,episode);
        this.managerV2 = new DownloadManager_v2(context);
        this.episode = episode;
    }

    @Override
    protected void startVideo(String file) {
        hidePD();
        //MiniEpisodeOffline episode2 = new MiniEpisodeOffline(episode, managerV2.getEpisodeFile(episode,true),managerV2.getEpisodeFile(episode,false));
        MiniEpisodeOffline episode2 = new MiniEpisodeOffline(episode,managerV2.getEpisodeFile(episode,true),managerV2.getEpisodeFile(episode,false));
        episode2.setLink(file);
        managerV2.downloadAnime(episode2);

    }
}
