package com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.eme22.kumaanime.AnimeActivity_fragments.Download;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.OtherUtils;
import com.eme22.kumaanime.AppUtils.StringUtils;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.PermissionActivity;
import com.eme22.kumaanime.Services.DownloadService;
import com.tingyik90.prefmanager.PrefManager;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;

import okhttp3.Headers;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DownloadManager {

    public static final String TAG = "DOWNLOAD MANAGER";
    private static final TaskRunner taskRunner = new TaskRunner();
    private static DownloadManager INSTANCE;
    private final PrefManager prefManager;
    private final PermissionActivity activity;
    private File MAIN_DIR;
    private boolean initialized = false;


    public DownloadManager(PermissionActivity permissionActivity) {
        this.prefManager = new PrefManager(permissionActivity);
        this.activity = permissionActivity;
    }

    public static synchronized DownloadManager getInstance(PermissionActivity permissionActivity){
        if (INSTANCE==null) INSTANCE=new DownloadManager(permissionActivity);
        return INSTANCE;
    }

    @Deprecated
    public static synchronized DownloadManager getInstance(){
        if (INSTANCE==null) return null;
        return INSTANCE;
    }

    private boolean checkStorageRequest(){
        if (ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission granted");
            return true;
        }
        return false;
    }

    public void init(){
        if (checkStorageRequest()){
            MAIN_DIR = new File(Environment.getExternalStorageDirectory() + "/KumaAnime");
            if(!(MAIN_DIR.exists() && MAIN_DIR.isDirectory())) {
                boolean m = MAIN_DIR.mkdirs();
                if (!m) {
                    Log.e(TAG, "Directory creation failed.");
                }
                init();
            }
            else initialized = true;
        }
        else {activity.addPermissionCallback(0, (permissions, grantResults) -> {
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

    public File getMAIN_DIR() {
        return MAIN_DIR;
    }

    public void downloadAnime(MiniEpisode episode , String file, Headers headers){
        if (!initialized) {
            Log.e(TAG, "Not Initialized");
            return;
        }

        if (checkAnimeFolder(episode.getAnimeID())){
            Log.d("ANIME EXISTS?", String.valueOf(checkAnimeFolder(episode.getAnimeID())));
            File anime = new File(MAIN_DIR, String.valueOf(episode.getAnimeID()));

            Intent intent = new Intent(activity, DownloadService.class);
            intent.putExtra(DownloadService.EPISODE_LOCATION,anime);
            intent.putExtra(DownloadService.EPISODE_LINK,file);
            intent.putExtra(DownloadService.EPISODE, (Parcelable) episode);
            intent.putExtra(DownloadService.DOWNLOAD_HEADERS, String.valueOf(headers));
            DownloadService.enqueueWork(activity,intent);

            /*
            taskRunner.executeAsync(new EpisodeDownloader(activity, file, episode, anime, new Callback() {
                @Override
                public void onSuccess(Object o) {

                }

                @Override
                public void onError(Exception e) {

                }
            }));
            */
        }
        else taskRunner.executeAsync(new ImageDownloader(activity,episode.getAnimeID(), new ImageDownloader.ImageDownloaderCallback() {
            @Override
            public void onSuccess() {
                Log.d("ANIME EXISTS?", String.valueOf(checkAnimeFolder(episode.getAnimeID())));
                if (checkAnimeFolder(episode.getAnimeID())){
                    Log.d("ANIME EXISTS?", String.valueOf(checkAnimeFolder(episode.getAnimeID())));
                    File anime = new File(MAIN_DIR, String.valueOf(episode.getAnimeID()));

                    Intent intent = new Intent(activity, DownloadService.class);
                    intent.putExtra(DownloadService.EPISODE_LOCATION,anime);
                    intent.putExtra(DownloadService.EPISODE_LINK,file);
                    intent.putExtra(DownloadService.EPISODE, (Parcelable) episode);
                    DownloadService.enqueueWork(activity,intent);

                    /*
                    taskRunner.executeAsync(new EpisodeDownloader(activity, file, episode, anime, new Callback() {
                        @Override
                        public void onSuccess(Object o) {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    }));
                    */
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

    public void downloadAnime(MiniEpisode episode , String file){
        if (!initialized) {
            Log.e(TAG, "Not Initialized");
            return;
        }

        if (checkAnimeFolder(episode.getAnimeID())){
            Log.d("ANIME EXISTS?", String.valueOf(checkAnimeFolder(episode.getAnimeID())));
            File anime = new File(MAIN_DIR, String.valueOf(episode.getAnimeID()));

            Intent intent = new Intent(activity, DownloadService.class);
            intent.putExtra(DownloadService.EPISODE_LOCATION,anime);
            intent.putExtra(DownloadService.EPISODE_LINK,file);
            intent.putExtra(DownloadService.EPISODE, (Parcelable) episode);
            DownloadService.enqueueWork(activity,intent);

            /*
            taskRunner.executeAsync(new EpisodeDownloader(activity, file, episode, anime, new Callback() {
                @Override
                public void onSuccess(Object o) {

                }

                @Override
                public void onError(Exception e) {

                }
            }));
            */
        }
        else taskRunner.executeAsync(new ImageDownloader(activity,episode.getAnimeID(), new ImageDownloader.ImageDownloaderCallback() {
            @Override
            public void onSuccess() {
                Log.d("ANIME EXISTS?", String.valueOf(checkAnimeFolder(episode.getAnimeID())));
                if (checkAnimeFolder(episode.getAnimeID())){
                    Log.d("ANIME EXISTS?", String.valueOf(checkAnimeFolder(episode.getAnimeID())));
                    File anime = new File(MAIN_DIR, String.valueOf(episode.getAnimeID()));

                    Intent intent = new Intent(activity, DownloadService.class);
                    intent.putExtra(DownloadService.EPISODE_LOCATION,anime);
                    intent.putExtra(DownloadService.EPISODE_LINK,file);
                    intent.putExtra(DownloadService.EPISODE, (Parcelable) episode);
                    DownloadService.enqueueWork(activity,intent);

                    /*
                    taskRunner.executeAsync(new EpisodeDownloader(activity, file, episode, anime, new Callback() {
                        @Override
                        public void onSuccess(Object o) {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    }));
                    */
                }
                else Log.e(TAG, "CANNOT DOWNLOAD ANIME");

            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                Log.e(TAG, "CANNOT DOWNLOAD ANIME");
            }

        }));
    }

    public boolean isAnimeDownloaded(MiniEpisode episode){

        return getFile(episode).exists();
    }

    public File getFile(MiniEpisode episode){
        return new File(getAnime(episode.getAnimeID()), StringUtils.FormatFile(episode.getAnimeID(),episode.getEpisode(),"mp4"));
    }

    public File getAnime(Integer anime_id){
        if (anime_id<=0) return null;
        File anime = new File(MAIN_DIR, String.valueOf(anime_id));
        Log.d(TAG, anime.getPath());
        if (anime.exists() && anime.isDirectory()){
            return anime;
        }
        else {
            if (anime.mkdirs()){
                return anime;
            }
            else {
                Log.e(TAG, anime.getPath());
                Log.e(TAG, "Error Creando Anime");
                return anime;
            }
        }
    }

    public File getImage(MiniAnime anime){
        if (anime.getId() >0) return new File(getAnime(anime.getId()), StringUtils.FormatFile(anime.getId(),"png"));
        else return null;
    }

    public File[] getMainFileTree(){
        return getMAIN_DIR().listFiles();
    }

    public Uri getFileUri(MiniEpisode episode){
        return FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", getFile(episode));
    }
    public Uri getFileUri(File episode){
        return FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", episode);
    }









}
