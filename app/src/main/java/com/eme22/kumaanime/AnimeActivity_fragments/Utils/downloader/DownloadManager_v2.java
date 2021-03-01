package com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.AppUtils.OtherUtils;
import com.eme22.kumaanime.AppUtils.StringUtils;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.PermissionActivity;
import com.eme22.kumaanime.Services.Downloads.DownloadService_v2;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.eme22.kumaanime.AppUtils.AppConstant.APP_FOLDER;

public class DownloadManager_v2 {

    private static final Map<String,String> headers = Collections.singletonMap("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.66");
    private PermissionActivity activity;
    private final Context context;
    public static final String TAG = "DOWNLOAD MANAGER";
    private static final TaskRunner taskRunner = new TaskRunner();
    private File mainDir;

    public DownloadManager_v2(Context context) {
        this.context = context;
        init();
    }

    private boolean checkStorageRequest() {
        if (ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission granted");
            return true;
        }
        return false;
    }


    public void init(){
        if (checkStorageRequest()){
            mainDir = new File(Environment.getExternalStorageDirectory(), APP_FOLDER);
            if(!(mainDir.exists() && mainDir.isDirectory())) {
                boolean m = mainDir.mkdirs();
                if (!m) {
                    Log.e(TAG, "Directory creation failed.");
                }
                init();
            }
        }
        else {
            if (context instanceof PermissionActivity){
                activity = (PermissionActivity) context;
                activity.addPermissionCallback(0, (permissions, grantResults) -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        OtherUtils.toast("Permiso necesario para el uso offline");
                        init();
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 0);
                    OtherUtils.toast("Permiso necesario para el uso offline");
                }

            }
            else throw new RuntimeException();
        }
    }

    private boolean checkAnimeFolder(int animeID){
        File anime = getAnime(animeID);
        File anime_image = new File(getAnime(animeID), StringUtils.FormatFile(animeID,"png"));

        boolean anime_exists = anime.exists() && anime.isDirectory();
        boolean image_exists = anime_image.exists();

        if (image_exists && !anime_exists){
            boolean m = anime.mkdirs();
            if (!m) {
                Log.e(TAG, "Directory creation failed.");
            }
            return m;
        }

        return anime_exists && image_exists;
    }

    public boolean isAnimeDownloaded(MiniEpisode episode){
        return getEpisodeFile(episode,true).exists() && getEpisodeFile(episode, false).exists();
    }

    public boolean deleteAnime(MiniEpisode episode){
        try
        {
            return getEpisodeFile(episode,true).delete() && getEpisodeFile(episode, false).delete();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public File getEpisodeFile(MiniEpisode episode, boolean video){
        return new File(getAnime(episode.getAnimeID()), StringUtils.FormatFile(episode.getAnimeID(),episode.getEpisode(), video ? "mp4" : "png"));
    }

    public File getAnime(Integer anime_id){
        if (anime_id<=0) return null;
        File anime = new File(mainDir, String.valueOf(anime_id));
        Log.d(TAG, anime.getPath());
        if (!anime.exists() || !anime.isDirectory()) {
            if (!anime.mkdirs()) {
                Log.e(TAG, anime.getPath());
                Log.e(TAG, "Error Creando Anime");
            }
        }
        return anime;
    }

    public File getImage(MiniAnime anime){
        return getImage(anime.getId());
    }

    public File getImage(Integer animeID){
        if (animeID >0) return new File(getAnime(animeID), StringUtils.FormatFile(animeID,"png"));
        else return null;
    }

    public File[] getMainFileTree(){
        return mainDir.listFiles();
    }

    public Uri getFileUri(MiniEpisode episode){
        return getFileUri(getEpisodeFile(episode,true));
    }
    public Uri getFileUri(File episode){
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", episode);
    }

    public void downloadAnime(MiniEpisodeOffline offline){
        String link = offline.getLink();
        if (link.contains("streamtape")) downloadAnime(offline,headers);
        else downloadAnime(offline,null);
    }

    public void downloadAnime(MiniEpisodeOffline offline, Map<String, String> headers){
        init();
        if (checkAnimeFolder(offline.getAnimeID())){
            Intent intent = new Intent(context, DownloadService_v2.class);
            intent.putExtra(DownloadService_v2.EPISODE, offline);
            if (headers != null) {
                intent.putExtra(DownloadService_v2.DOWNLOAD_HEADERS, (Serializable) headers);
            }
            DownloadService_v2.enqueueWork(context,intent);
        }
        else taskRunner.executeAsync(new ImageDownloader(context,offline.getAnimeID(), new ImageDownloader.ImageDownloaderCallback() {
            @Override
            public void onSuccess() {
                Log.d("ANIME EXISTS?", String.valueOf(checkAnimeFolder(offline.getAnimeID())));
                if (checkAnimeFolder(offline.getAnimeID())){
                    Log.d("ANIME EXISTS?", String.valueOf(checkAnimeFolder(offline.getAnimeID())));

                    Intent intent = new Intent(context, DownloadService_v2.class);
                    intent.putExtra(DownloadService_v2.EPISODE, offline);
                    if (headers != null) {
                        intent.putExtra(DownloadService_v2.DOWNLOAD_HEADERS, (Parcelable) headers);
                    }
                    DownloadService_v2.enqueueWork(context,intent);
                }
                else Log.e(TAG, "CANNOT DOWNLOAD ANIME");

            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "CANNOT DOWNLOAD ANIME");
                Log.e(TAG,e.getLocalizedMessage());
            }

        }));

    }



}
