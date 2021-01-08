
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_update;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnimeUpdate {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("num_watched_episodes")
    @Expose
    private Integer numWatchedEpisodes;
    @SerializedName("is_rewatching")
    @Expose
    private Boolean isRewatching;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("num_times_rewatched")
    @Expose
    private Integer numTimesRewatched;
    @SerializedName("rewatch_value")
    @Expose
    private Integer rewatchValue;
    @SerializedName("tags")
    @Expose
    private List<Object> tags = null;
    @SerializedName("comments")
    @Expose
    private String comments;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getNumWatchedEpisodes() {
        return numWatchedEpisodes;
    }

    public void setNumWatchedEpisodes(Integer numWatchedEpisodes) {
        this.numWatchedEpisodes = numWatchedEpisodes;
    }

    public Boolean getIsRewatching() {
        return isRewatching;
    }

    public void setIsRewatching(Boolean isRewatching) {
        this.isRewatching = isRewatching;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getNumTimesRewatched() {
        return numTimesRewatched;
    }

    public void setNumTimesRewatched(Integer numTimesRewatched) {
        this.numTimesRewatched = numTimesRewatched;
    }

    public Integer getRewatchValue() {
        return rewatchValue;
    }

    public void setRewatchValue(Integer rewatchValue) {
        this.rewatchValue = rewatchValue;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
