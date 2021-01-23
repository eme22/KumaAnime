package com.eme22.kumaanime.AnimeActivity_fragments.Utils;

import android.content.Context;
import android.util.Log;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Dao;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class OfflineFetcher implements Runnable{

    Context context;
    DownloadManager downloadManager;
    MiniAnimeTable_Repo repo;
    OfflineFetcherCallback callback;

    ArrayList<MiniAnime> data = new ArrayList<>();

    public OfflineFetcher(Context context, DownloadManager downloadManager, OfflineFetcherCallback callback) {
        this.repo = new MiniAnimeTable_Repo(context);
        this.downloadManager = downloadManager;
        this.callback = callback;
    }

    @Override
    public void run() {
        init();
    }

    private void init() {
        for (File f: downloadManager.getMainFileTree()) {
            //Log.d("DATOS OFFLINE", f.getName());
            if (f.isDirectory() && Objects.requireNonNull(f.listFiles()).length>1) {
                try {
                    MiniAnime anime = repo.getanime(Integer.parseInt(f.getName()));
                    if (anime.getTitle() != null){
                        data.add(anime);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        //downloadImages(data);
        callback.onSuccess(data);

    }

    private void downloadImages(ArrayList<MiniAnime> data) {
        //downloadManager.downloadAnime();
    }

    public interface OfflineFetcherCallback{
        void onSuccess(ArrayList<MiniAnime> animes);
        void onFailure(Exception e);
    }
}
