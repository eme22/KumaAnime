
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnimeStatistics_1 {

    @SerializedName("num_items_watching")
    @Expose
    private Integer numItemsWatching;
    @SerializedName("num_items_completed")
    @Expose
    private Integer numItemsCompleted;
    @SerializedName("num_items_on_hold")
    @Expose
    private Integer numItemsOnHold;
    @SerializedName("num_items_dropped")
    @Expose
    private Integer numItemsDropped;
    @SerializedName("num_items_plan_to_watch")
    @Expose
    private Integer numItemsPlanToWatch;
    @SerializedName("num_items")
    @Expose
    private Integer numItems;
    @SerializedName("num_days_watched")
    @Expose
    private Double numDaysWatched;
    @SerializedName("num_days_watching")
    @Expose
    private Double numDaysWatching;
    @SerializedName("num_days_completed")
    @Expose
    private Double numDaysCompleted;
    @SerializedName("num_days_on_hold")
    @Expose
    private Integer numDaysOnHold;
    @SerializedName("num_days_dropped")
    @Expose
    private Integer numDaysDropped;
    @SerializedName("num_days")
    @Expose
    private Double numDays;
    @SerializedName("num_episodes")
    @Expose
    private Integer numEpisodes;
    @SerializedName("num_times_rewatched")
    @Expose
    private Integer numTimesRewatched;
    @SerializedName("mean_score")
    @Expose
    private Double meanScore;

    public Integer getNumItemsWatching() {
        return numItemsWatching;
    }

    public void setNumItemsWatching(Integer numItemsWatching) {
        this.numItemsWatching = numItemsWatching;
    }

    public Integer getNumItemsCompleted() {
        return numItemsCompleted;
    }

    public void setNumItemsCompleted(Integer numItemsCompleted) {
        this.numItemsCompleted = numItemsCompleted;
    }

    public Integer getNumItemsOnHold() {
        return numItemsOnHold;
    }

    public void setNumItemsOnHold(Integer numItemsOnHold) {
        this.numItemsOnHold = numItemsOnHold;
    }

    public Integer getNumItemsDropped() {
        return numItemsDropped;
    }

    public void setNumItemsDropped(Integer numItemsDropped) {
        this.numItemsDropped = numItemsDropped;
    }

    public Integer getNumItemsPlanToWatch() {
        return numItemsPlanToWatch;
    }

    public void setNumItemsPlanToWatch(Integer numItemsPlanToWatch) {
        this.numItemsPlanToWatch = numItemsPlanToWatch;
    }

    public Integer getNumItems() {
        return numItems;
    }

    public void setNumItems(Integer numItems) {
        this.numItems = numItems;
    }

    public Double getNumDaysWatched() {
        return numDaysWatched;
    }

    public void setNumDaysWatched(Double numDaysWatched) {
        this.numDaysWatched = numDaysWatched;
    }

    public Double getNumDaysWatching() {
        return numDaysWatching;
    }

    public void setNumDaysWatching(Double numDaysWatching) {
        this.numDaysWatching = numDaysWatching;
    }

    public Double getNumDaysCompleted() {
        return numDaysCompleted;
    }

    public void setNumDaysCompleted(Double numDaysCompleted) {
        this.numDaysCompleted = numDaysCompleted;
    }

    public Integer getNumDaysOnHold() {
        return numDaysOnHold;
    }

    public void setNumDaysOnHold(Integer numDaysOnHold) {
        this.numDaysOnHold = numDaysOnHold;
    }

    public Integer getNumDaysDropped() {
        return numDaysDropped;
    }

    public void setNumDaysDropped(Integer numDaysDropped) {
        this.numDaysDropped = numDaysDropped;
    }

    public Double getNumDays() {
        return numDays;
    }

    public void setNumDays(Double numDays) {
        this.numDays = numDays;
    }

    public Integer getNumEpisodes() {
        return numEpisodes;
    }

    public void setNumEpisodes(Integer numEpisodes) {
        this.numEpisodes = numEpisodes;
    }

    public Integer getNumTimesRewatched() {
        return numTimesRewatched;
    }

    public void setNumTimesRewatched(Integer numTimesRewatched) {
        this.numTimesRewatched = numTimesRewatched;
    }

    public Double getMeanScore() {
        return meanScore;
    }

    public void setMeanScore(Double meanScore) {
        this.meanScore = meanScore;
    }

}
