package com.eme22.kumaanime.AnimeActivity_fragments;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.MainActivity;
import com.eme22.kumaanime.PermissionActivity;

public class Download extends Play{

    MiniEpisode episode;

    public Download(PermissionActivity context, FragmentManager manager, MiniEpisode episode) {
        super(context, manager ,episode);
        this.episode = episode;
    }

    @Override
    protected void startVideo(String file) {
        hidePD();
        DownloadManager.getInstance((PermissionActivity) context).downloadAnime(episode,file);
    }
}
