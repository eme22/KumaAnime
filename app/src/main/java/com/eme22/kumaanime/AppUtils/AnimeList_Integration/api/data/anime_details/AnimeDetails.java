
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.MainPicture;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnimeDetails {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("main_picture")
    @Expose
    private MainPicture mainPicture;
    @SerializedName("alternative_titles")
    @Expose
    private AlternativeTitles alternativeTitles;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("synopsis")
    @Expose
    private String synopsis;
    @SerializedName("mean")
    @Expose
    private Double mean;
    @SerializedName("rank")
    @Expose
    private Integer rank;
    @SerializedName("popularity")
    @Expose
    private Integer popularity;
    @SerializedName("num_list_users")
    @Expose
    private Integer numListUsers;
    @SerializedName("num_scoring_users")
    @Expose
    private Integer numScoringUsers;
    @SerializedName("nsfw")
    @Expose
    private String nsfw;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
    @SerializedName("my_list_status")
    @Expose
    private MyListStatus myListStatus;
    @SerializedName("num_episodes")
    @Expose
    private Integer numEpisodes;
    @SerializedName("start_season")
    @Expose
    private StartSeason startSeason;
    @SerializedName("broadcast")
    @Expose
    private Broadcast broadcast;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("average_episode_duration")
    @Expose
    private Integer averageEpisodeDuration;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("pictures")
    @Expose
    private List<MainPicture> pictures = null;
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("related_anime")
    @Expose
    private List<RelatedAnime> relatedAnime = null;
    @SerializedName("related_manga")
    @Expose
    private List<Object> relatedManga = null;
    @SerializedName("recommendations")
    @Expose
    private List<Recommendation> recommendations = null;
    @SerializedName("studios")
    @Expose
    private List<Studio> studios = null;
    @SerializedName("statistics")
    @Expose
    private Statistics statistics;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MainPicture getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(MainPicture mainPicture) {
        this.mainPicture = mainPicture;
    }

    public AlternativeTitles getAlternativeTitles() {
        return alternativeTitles;
    }

    public void setAlternativeTitles(AlternativeTitles alternativeTitles) {
        this.alternativeTitles = alternativeTitles;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getNumListUsers() {
        return numListUsers;
    }

    public void setNumListUsers(Integer numListUsers) {
        this.numListUsers = numListUsers;
    }

    public Integer getNumScoringUsers() {
        return numScoringUsers;
    }

    public void setNumScoringUsers(Integer numScoringUsers) {
        this.numScoringUsers = numScoringUsers;
    }

    public String getNsfw() {
        return nsfw;
    }

    public void setNsfw(String nsfw) {
        this.nsfw = nsfw;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public MyListStatus getMyListStatus() {
        return myListStatus;
    }

    public void setMyListStatus(MyListStatus myListStatus) {
        this.myListStatus = myListStatus;
    }

    public Integer getNumEpisodes() {
        return numEpisodes;
    }

    public void setNumEpisodes(Integer numEpisodes) {
        this.numEpisodes = numEpisodes;
    }

    public StartSeason getStartSeason() {
        return startSeason;
    }

    public void setStartSeason(StartSeason startSeason) {
        this.startSeason = startSeason;
    }

    public Broadcast getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Broadcast broadcast) {
        this.broadcast = broadcast;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getAverageEpisodeDuration() {
        return averageEpisodeDuration;
    }

    public void setAverageEpisodeDuration(Integer averageEpisodeDuration) {
        this.averageEpisodeDuration = averageEpisodeDuration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<MainPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<MainPicture> pictures) {
        this.pictures = pictures;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public List<RelatedAnime> getRelatedAnime() {
        return relatedAnime;
    }

    public void setRelatedAnime(List<RelatedAnime> relatedAnime) {
        this.relatedAnime = relatedAnime;
    }

    public List<Object> getRelatedManga() {
        return relatedManga;
    }

    public void setRelatedManga(List<Object> relatedManga) {
        this.relatedManga = relatedManga;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public List<Studio> getStudios() {
        return studios;
    }

    public void setStudios(List<Studio> studios) {
        this.studios = studios;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

}
