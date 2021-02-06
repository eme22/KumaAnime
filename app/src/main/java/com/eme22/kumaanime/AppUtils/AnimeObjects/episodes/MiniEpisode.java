package com.eme22.kumaanime.AppUtils.AnimeObjects.episodes;

import android.os.Parcel;
import android.os.Parcelable;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.MainPicture;

public class MiniEpisode implements Parcelable {
    private int animeID;
    private String name;
    private String episode;
    private MainPicture mainPicture;
    private String link;
    private boolean viewed;

    public MiniEpisode() {
    }

    public MiniEpisode(int animeID, String name, String episode, MainPicture mainPicture, String link, boolean viewed) {
        this.animeID = animeID;
        this.name = name;
        this.episode = episode;
        this.mainPicture = mainPicture;
        this.link = link;
        this.viewed = viewed;
    }

    public int getAnimeID() {
        return animeID;
    }

    public void setAnimeID(int animeID) {
        this.animeID = animeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public MainPicture getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(MainPicture mainPicture) {
        this.mainPicture = mainPicture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    protected MiniEpisode(Parcel in) {
        animeID = in.readInt();
        name = in.readString();
        episode = in.readString();
        mainPicture = in.readParcelable(MainPicture.class.getClassLoader());
        link = in.readString();
        viewed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(animeID);
        dest.writeString(name);
        dest.writeString(episode);
        dest.writeParcelable(mainPicture, flags);
        dest.writeString(link);
        dest.writeByte((byte) (viewed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MiniEpisode> CREATOR = new Creator<MiniEpisode>() {
        @Override
        public MiniEpisode createFromParcel(Parcel in) {
            return new MiniEpisode(in);
        }

        @Override
        public MiniEpisode[] newArray(int size) {
            return new MiniEpisode[size];
        }
    };
}
