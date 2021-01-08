package com.eme22.kumaanime.AppUtils.AnimeObjects.episodes;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.MainPicture;

import java.io.Serializable;

public class MiniEpisode implements Serializable {
    private int animeID;
    private String name;
    private String episode;
    private MainPicture mainPicture;
    private String link;

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

    public int getAnimeID() { return animeID; }

    public void setAnimeID(int animeID) { this.animeID = animeID; }
}
