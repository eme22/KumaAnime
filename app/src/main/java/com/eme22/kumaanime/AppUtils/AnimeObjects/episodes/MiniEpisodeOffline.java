package com.eme22.kumaanime.AppUtils.AnimeObjects.episodes;

import android.os.Parcel;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.MainPicture;

import java.io.File;

public class MiniEpisodeOffline extends MiniEpisode {

    private File episode_file;
    private File image;


    public MiniEpisodeOffline() {
    }

    public MiniEpisodeOffline(int animeID, int online, String name, String episode, MainPicture mainPicture, String link, File episode_file, File image) {
        super(animeID, online, name, episode, mainPicture, link, false);
        this.episode_file = episode_file;
        this.image = image;
    }

    public MiniEpisodeOffline(Parcel in) {
        super(in);
        in.writeSerializable(episode_file);
        in.writeSerializable(image);
    }

    public MiniEpisodeOffline(MiniEpisode episode, File episode_file, File image){
        super(episode.getAnimeID(),episode.getOnlineID(),episode.getName(),episode.getEpisode(),episode.getMainPicture(),episode.getLink(),episode.isViewed());
        this.episode_file = episode_file;
        this.image = image;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(episode_file);
        dest.writeSerializable(image);
    }

    public static final Creator<MiniEpisodeOffline> CREATOR = new Creator<MiniEpisodeOffline>() {
        @Override
        public MiniEpisodeOffline createFromParcel(Parcel in) {
            return new MiniEpisodeOffline(in);
        }

        @Override
        public MiniEpisodeOffline[] newArray(int size) {
            return new MiniEpisodeOffline[size];
        }
    };

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
