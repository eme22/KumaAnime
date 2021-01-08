
package com.eme22.kumaanime.AppUtils.Servers.OKRu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OKRu {

    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("owner")
    @Expose
    private Boolean owner;
    @SerializedName("voted")
    @Expose
    private Boolean voted;
    @SerializedName("likeCount")
    @Expose
    private Integer likeCount;
    @SerializedName("subscribed")
    @Expose
    private Boolean subscribed;
    @SerializedName("isWatchLater")
    @Expose
    private Boolean isWatchLater;
    @SerializedName("slot")
    @Expose
    private Integer slot;
    @SerializedName("siteZone")
    @Expose
    private Integer siteZone;
    @SerializedName("showAd")
    @Expose
    private Boolean showAd;
    @SerializedName("fromTime")
    @Expose
    private Integer fromTime;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("movie")
    @Expose
    private Movie movie;
    @SerializedName("admanMetadata")
    @Expose
    private AdmanMetadata admanMetadata;
    @SerializedName("partnerId")
    @Expose
    private Integer partnerId;
    @SerializedName("ownerMovieId")
    @Expose
    private String ownerMovieId;
    @SerializedName("alwaysShowRec")
    @Expose
    private Boolean alwaysShowRec;
    @SerializedName("videos")
    @Expose
    private List<Video> videos = null;
    @SerializedName("metadataEmbedded")
    @Expose
    private String metadataEmbedded;
    @SerializedName("metadataUrl")
    @Expose
    private String metadataUrl;
    @SerializedName("hlsManifestUrl")
    @Expose
    private String hlsManifestUrl;
    @SerializedName("failoverHosts")
    @Expose
    private List<String> failoverHosts = null;
    @SerializedName("autoplay")
    @Expose
    private Autoplay autoplay;
    @SerializedName("p2pInfo")
    @Expose
    private P2pInfo p2pInfo;
    @SerializedName("stunServers")
    @Expose
    private List<StunServer> stunServers = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public OKRu() {
    }

    /**
     * 
     * @param movie
     * @param voted
     * @param likeCount
     * @param videos
     * @param slot
     * @param admanMetadata
     * @param subscribed
     * @param siteZone
     * @param provider
     * @param fromTime
     * @param metadataEmbedded
     * @param owner
     * @param isWatchLater
     * @param p2pInfo
     * @param author
     * @param showAd
     * @param alwaysShowRec
     * @param autoplay
     * @param stunServers
     * @param metadataUrl
     * @param failoverHosts
     * @param service
     * @param partnerId
     * @param ownerMovieId
     * @param hlsManifestUrl
     */
    public OKRu(String provider, String service, Boolean owner, Boolean voted, Integer likeCount, Boolean subscribed, Boolean isWatchLater, Integer slot, Integer siteZone, Boolean showAd, Integer fromTime, Author author, Movie movie, AdmanMetadata admanMetadata, Integer partnerId, String ownerMovieId, Boolean alwaysShowRec, List<Video> videos, String metadataEmbedded, String metadataUrl, String hlsManifestUrl, List<String> failoverHosts, Autoplay autoplay, P2pInfo p2pInfo, List<StunServer> stunServers) {
        super();
        this.provider = provider;
        this.service = service;
        this.owner = owner;
        this.voted = voted;
        this.likeCount = likeCount;
        this.subscribed = subscribed;
        this.isWatchLater = isWatchLater;
        this.slot = slot;
        this.siteZone = siteZone;
        this.showAd = showAd;
        this.fromTime = fromTime;
        this.author = author;
        this.movie = movie;
        this.admanMetadata = admanMetadata;
        this.partnerId = partnerId;
        this.ownerMovieId = ownerMovieId;
        this.alwaysShowRec = alwaysShowRec;
        this.videos = videos;
        this.metadataEmbedded = metadataEmbedded;
        this.metadataUrl = metadataUrl;
        this.hlsManifestUrl = hlsManifestUrl;
        this.failoverHosts = failoverHosts;
        this.autoplay = autoplay;
        this.p2pInfo = p2pInfo;
        this.stunServers = stunServers;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public OKRu withProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public OKRu withService(String service) {
        this.service = service;
        return this;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    public OKRu withOwner(Boolean owner) {
        this.owner = owner;
        return this;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public OKRu withVoted(Boolean voted) {
        this.voted = voted;
        return this;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public OKRu withLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public OKRu withSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
        return this;
    }

    public Boolean getIsWatchLater() {
        return isWatchLater;
    }

    public void setIsWatchLater(Boolean isWatchLater) {
        this.isWatchLater = isWatchLater;
    }

    public OKRu withIsWatchLater(Boolean isWatchLater) {
        this.isWatchLater = isWatchLater;
        return this;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public OKRu withSlot(Integer slot) {
        this.slot = slot;
        return this;
    }

    public Integer getSiteZone() {
        return siteZone;
    }

    public void setSiteZone(Integer siteZone) {
        this.siteZone = siteZone;
    }

    public OKRu withSiteZone(Integer siteZone) {
        this.siteZone = siteZone;
        return this;
    }

    public Boolean getShowAd() {
        return showAd;
    }

    public void setShowAd(Boolean showAd) {
        this.showAd = showAd;
    }

    public OKRu withShowAd(Boolean showAd) {
        this.showAd = showAd;
        return this;
    }

    public Integer getFromTime() {
        return fromTime;
    }

    public void setFromTime(Integer fromTime) {
        this.fromTime = fromTime;
    }

    public OKRu withFromTime(Integer fromTime) {
        this.fromTime = fromTime;
        return this;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public OKRu withAuthor(Author author) {
        this.author = author;
        return this;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public OKRu withMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public AdmanMetadata getAdmanMetadata() {
        return admanMetadata;
    }

    public void setAdmanMetadata(AdmanMetadata admanMetadata) {
        this.admanMetadata = admanMetadata;
    }

    public OKRu withAdmanMetadata(AdmanMetadata admanMetadata) {
        this.admanMetadata = admanMetadata;
        return this;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public OKRu withPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
        return this;
    }

    public String getOwnerMovieId() {
        return ownerMovieId;
    }

    public void setOwnerMovieId(String ownerMovieId) {
        this.ownerMovieId = ownerMovieId;
    }

    public OKRu withOwnerMovieId(String ownerMovieId) {
        this.ownerMovieId = ownerMovieId;
        return this;
    }

    public Boolean getAlwaysShowRec() {
        return alwaysShowRec;
    }

    public void setAlwaysShowRec(Boolean alwaysShowRec) {
        this.alwaysShowRec = alwaysShowRec;
    }

    public OKRu withAlwaysShowRec(Boolean alwaysShowRec) {
        this.alwaysShowRec = alwaysShowRec;
        return this;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public OKRu withVideos(List<Video> videos) {
        this.videos = videos;
        return this;
    }

    public String getMetadataEmbedded() {
        return metadataEmbedded;
    }

    public void setMetadataEmbedded(String metadataEmbedded) {
        this.metadataEmbedded = metadataEmbedded;
    }

    public OKRu withMetadataEmbedded(String metadataEmbedded) {
        this.metadataEmbedded = metadataEmbedded;
        return this;
    }

    public String getMetadataUrl() {
        return metadataUrl;
    }

    public void setMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
    }

    public OKRu withMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
        return this;
    }

    public String getHlsManifestUrl() {
        return hlsManifestUrl;
    }

    public void setHlsManifestUrl(String hlsManifestUrl) {
        this.hlsManifestUrl = hlsManifestUrl;
    }

    public OKRu withHlsManifestUrl(String hlsManifestUrl) {
        this.hlsManifestUrl = hlsManifestUrl;
        return this;
    }

    public List<String> getFailoverHosts() {
        return failoverHosts;
    }

    public void setFailoverHosts(List<String> failoverHosts) {
        this.failoverHosts = failoverHosts;
    }

    public OKRu withFailoverHosts(List<String> failoverHosts) {
        this.failoverHosts = failoverHosts;
        return this;
    }

    public Autoplay getAutoplay() {
        return autoplay;
    }

    public void setAutoplay(Autoplay autoplay) {
        this.autoplay = autoplay;
    }

    public OKRu withAutoplay(Autoplay autoplay) {
        this.autoplay = autoplay;
        return this;
    }

    public P2pInfo getP2pInfo() {
        return p2pInfo;
    }

    public void setP2pInfo(P2pInfo p2pInfo) {
        this.p2pInfo = p2pInfo;
    }

    public OKRu withP2pInfo(P2pInfo p2pInfo) {
        this.p2pInfo = p2pInfo;
        return this;
    }

    public List<StunServer> getStunServers() {
        return stunServers;
    }

    public void setStunServers(List<StunServer> stunServers) {
        this.stunServers = stunServers;
    }

    public OKRu withStunServers(List<StunServer> stunServers) {
        this.stunServers = stunServers;
        return this;
    }

}
