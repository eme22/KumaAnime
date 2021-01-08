
package com.eme22.kumaanime.AppUtils.Servers.OKRu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("movieId")
    @Expose
    private String movieId;
    @SerializedName("likeId")
    @Expose
    private String likeId;
    @SerializedName("contentId")
    @Expose
    private String contentId;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("collageInfo")
    @Expose
    private CollageInfo collageInfo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("isLive")
    @Expose
    private Boolean isLive;
    @SerializedName("notPublished")
    @Expose
    private Boolean notPublished;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Movie() {
    }

    /**
     * 
     * @param notPublished
     * @param contentId
     * @param link
     * @param movieId
     * @param title
     * @param url
     * @param duration
     * @param isLive
     * @param likeId
     * @param statusText
     * @param collageInfo
     * @param id
     * @param poster
     * @param status
     */
    public Movie(String id, String movieId, String likeId, String contentId, String poster, String duration, String title, String url, String link, CollageInfo collageInfo, String status, String statusText, Boolean isLive, Boolean notPublished) {
        super();
        this.id = id;
        this.movieId = movieId;
        this.likeId = likeId;
        this.contentId = contentId;
        this.poster = poster;
        this.duration = duration;
        this.title = title;
        this.url = url;
        this.link = link;
        this.collageInfo = collageInfo;
        this.status = status;
        this.statusText = statusText;
        this.isLive = isLive;
        this.notPublished = notPublished;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Movie withId(String id) {
        this.id = id;
        return this;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Movie withMovieId(String movieId) {
        this.movieId = movieId;
        return this;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public Movie withLikeId(String likeId) {
        this.likeId = likeId;
        return this;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Movie withContentId(String contentId) {
        this.contentId = contentId;
        return this;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Movie withPoster(String poster) {
        this.poster = poster;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Movie withDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Movie withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Movie withUrl(String url) {
        this.url = url;
        return this;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Movie withLink(String link) {
        this.link = link;
        return this;
    }

    public CollageInfo getCollageInfo() {
        return collageInfo;
    }

    public void setCollageInfo(CollageInfo collageInfo) {
        this.collageInfo = collageInfo;
    }

    public Movie withCollageInfo(CollageInfo collageInfo) {
        this.collageInfo = collageInfo;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Movie withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Movie withStatusText(String statusText) {
        this.statusText = statusText;
        return this;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Movie withIsLive(Boolean isLive) {
        this.isLive = isLive;
        return this;
    }

    public Boolean getNotPublished() {
        return notPublished;
    }

    public void setNotPublished(Boolean notPublished) {
        this.notPublished = notPublished;
    }

    public Movie withNotPublished(Boolean notPublished) {
        this.notPublished = notPublished;
        return this;
    }

}
