package com.eme22.kumaanime.AnimeActivity_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.FragmentManager;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager_v2;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.PermissionActivity;

import java.util.HashMap;

public class Download extends Play{

    DownloadManager_v2 managerV2;

    public Download(Context context, FragmentManager manager, MiniEpisode episode) {
        super(context, manager ,episode);
        this.managerV2 = new DownloadManager_v2(context);
    }

    @Override
    protected void startVideo(String file) {
        startVideo(file, null);
    }

    @Override
    protected void startVideo(String file, HashMap<String, String> headers) {
        hidePD();

        if (file.contains("mega.nz")) {
            megaload(file);
            return;
        }

        MiniEpisodeOffline episode2 = new MiniEpisodeOffline(episode_anim,managerV2.getEpisodeFile(episode_anim,true),managerV2.getEpisodeFile(episode_anim,false));
        episode2.setLink(file);
        if (headers != null) managerV2.downloadAnime(episode2, headers);
        else managerV2.downloadAnime(episode2);
    }
}
