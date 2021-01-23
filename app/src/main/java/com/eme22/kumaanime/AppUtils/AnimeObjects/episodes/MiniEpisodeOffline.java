package com.eme22.kumaanime.AppUtils.AnimeObjects.episodes;

import android.os.Parcel;
import android.os.Parcelable;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.MainPicture;

import java.io.File;

public class MiniEpisodeOffline extends MiniEpisode {

    public MiniEpisodeOffline() {
    }

    public MiniEpisodeOffline(int animeID, String name, String episode, MainPicture mainPicture, String link, File episode_file, File image) {
        super(animeID, name, episode, mainPicture, link);
        this.episode_file = episode_file;
        this.image = image;
    }

    public MiniEpisodeOffline(Parcel in) {
        super(in);
        in.writeSerializable(episode_file);
        in.writeSerializable(image);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(episode_file);
        dest.writeSerializable(image);
    }

    private File episode_file;

    private File image;


    public File getEpisode_file() {
        return episode_file;
    }

    public void setEpisode_file(File episode_file) {
        this.episode_file = episode_file;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
