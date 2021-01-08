
package com.eme22.kumaanime.AppUtils.Servers.Fembed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("poster_file")
    @Expose
    private String posterFile;
    @SerializedName("logo_file")
    @Expose
    private String logoFile;
    @SerializedName("logo_position")
    @Expose
    private String logoPosition;
    @SerializedName("logo_link")
    @Expose
    private String logoLink;
    @SerializedName("logo_margin")
    @Expose
    private Integer logoMargin;
    @SerializedName("aspectratio")
    @Expose
    private String aspectratio;
    @SerializedName("powered_text")
    @Expose
    private String poweredText;
    @SerializedName("powered_url")
    @Expose
    private String poweredUrl;
    @SerializedName("css_background")
    @Expose
    private String cssBackground;
    @SerializedName("css_text")
    @Expose
    private String cssText;
    @SerializedName("css_menu")
    @Expose
    private String cssMenu;
    @SerializedName("css_mntext")
    @Expose
    private String cssMntext;
    @SerializedName("css_caption")
    @Expose
    private String cssCaption;
    @SerializedName("css_cttext")
    @Expose
    private String cssCttext;
    @SerializedName("css_ctsize")
    @Expose
    private String cssCtsize;
    @SerializedName("css_ctopacity")
    @Expose
    private String cssCtopacity;
    @SerializedName("css_ctedge")
    @Expose
    private String cssCtedge;
    @SerializedName("css_icon")
    @Expose
    private String cssIcon;
    @SerializedName("css_ichover")
    @Expose
    private String cssIchover;
    @SerializedName("css_tsprogress")
    @Expose
    private String cssTsprogress;
    @SerializedName("css_tsrail")
    @Expose
    private String cssTsrail;
    @SerializedName("css_button")
    @Expose
    private String cssButton;
    @SerializedName("css_bttext")
    @Expose
    private String cssBttext;
    @SerializedName("opt_autostart")
    @Expose
    private Boolean optAutostart;
    @SerializedName("opt_title")
    @Expose
    private Boolean optTitle;
    @SerializedName("opt_quality")
    @Expose
    private Boolean optQuality;
    @SerializedName("opt_caption")
    @Expose
    private Boolean optCaption;
    @SerializedName("opt_download")
    @Expose
    private Boolean optDownload;
    @SerializedName("opt_sharing")
    @Expose
    private Boolean optSharing;
    @SerializedName("opt_playrate")
    @Expose
    private Boolean optPlayrate;
    @SerializedName("opt_mute")
    @Expose
    private Boolean optMute;
    @SerializedName("opt_loop")
    @Expose
    private Boolean optLoop;
    @SerializedName("opt_vr")
    @Expose
    private Boolean optVr;
    @SerializedName("opt_cast")
    @Expose
    private Boolean optCast;
    @SerializedName("opt_nodefault")
    @Expose
    private Boolean optNodefault;
    @SerializedName("opt_forceposter")
    @Expose
    private Boolean optForceposter;
    @SerializedName("opt_parameter")
    @Expose
    private Boolean optParameter;
    @SerializedName("restrict_domain")
    @Expose
    private String restrictDomain;
    @SerializedName("restrict_action")
    @Expose
    private String restrictAction;
    @SerializedName("restrict_target")
    @Expose
    private String restrictTarget;
    @SerializedName("resume_enable")
    @Expose
    private Boolean resumeEnable;
    @SerializedName("resume_text")
    @Expose
    private String resumeText;
    @SerializedName("resume_yes")
    @Expose
    private String resumeYes;
    @SerializedName("resume_no")
    @Expose
    private String resumeNo;
    @SerializedName("adb_enable")
    @Expose
    private Boolean adbEnable;
    @SerializedName("adb_offset")
    @Expose
    private String adbOffset;
    @SerializedName("adb_text")
    @Expose
    private String adbText;
    @SerializedName("ads_adult")
    @Expose
    private Boolean adsAdult;
    @SerializedName("ads_pop")
    @Expose
    private Boolean adsPop;
    @SerializedName("ads_vast")
    @Expose
    private Boolean adsVast;
    @SerializedName("ads_free")
    @Expose
    private Integer adsFree;
    @SerializedName("trackingId")
    @Expose
    private String trackingId;
    @SerializedName("income")
    @Expose
    private Boolean income;
    @SerializedName("incomePop")
    @Expose
    private Boolean incomePop;
    @SerializedName("logger")
    @Expose
    private String logger;
    @SerializedName("revenue")
    @Expose
    private String revenue;
    @SerializedName("revenue_fallback")
    @Expose
    private String revenueFallback;
    @SerializedName("revenue_track")
    @Expose
    private String revenueTrack;

    public String getPosterFile() {
        return posterFile;
    }

    public void setPosterFile(String posterFile) {
        this.posterFile = posterFile;
    }

    public Player withPosterFile(String posterFile) {
        this.posterFile = posterFile;
        return this;
    }

    public String getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(String logoFile) {
        this.logoFile = logoFile;
    }

    public Player withLogoFile(String logoFile) {
        this.logoFile = logoFile;
        return this;
    }

    public String getLogoPosition() {
        return logoPosition;
    }

    public void setLogoPosition(String logoPosition) {
        this.logoPosition = logoPosition;
    }

    public Player withLogoPosition(String logoPosition) {
        this.logoPosition = logoPosition;
        return this;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public Player withLogoLink(String logoLink) {
        this.logoLink = logoLink;
        return this;
    }

    public Integer getLogoMargin() {
        return logoMargin;
    }

    public void setLogoMargin(Integer logoMargin) {
        this.logoMargin = logoMargin;
    }

    public Player withLogoMargin(Integer logoMargin) {
        this.logoMargin = logoMargin;
        return this;
    }

    public String getAspectratio() {
        return aspectratio;
    }

    public void setAspectratio(String aspectratio) {
        this.aspectratio = aspectratio;
    }

    public Player withAspectratio(String aspectratio) {
        this.aspectratio = aspectratio;
        return this;
    }

    public String getPoweredText() {
        return poweredText;
    }

    public void setPoweredText(String poweredText) {
        this.poweredText = poweredText;
    }

    public Player withPoweredText(String poweredText) {
        this.poweredText = poweredText;
        return this;
    }

    public String getPoweredUrl() {
        return poweredUrl;
    }

    public void setPoweredUrl(String poweredUrl) {
        this.poweredUrl = poweredUrl;
    }

    public Player withPoweredUrl(String poweredUrl) {
        this.poweredUrl = poweredUrl;
        return this;
    }

    public String getCssBackground() {
        return cssBackground;
    }

    public void setCssBackground(String cssBackground) {
        this.cssBackground = cssBackground;
    }

    public Player withCssBackground(String cssBackground) {
        this.cssBackground = cssBackground;
        return this;
    }

    public String getCssText() {
        return cssText;
    }

    public void setCssText(String cssText) {
        this.cssText = cssText;
    }

    public Player withCssText(String cssText) {
        this.cssText = cssText;
        return this;
    }

    public String getCssMenu() {
        return cssMenu;
    }

    public void setCssMenu(String cssMenu) {
        this.cssMenu = cssMenu;
    }

    public Player withCssMenu(String cssMenu) {
        this.cssMenu = cssMenu;
        return this;
    }

    public String getCssMntext() {
        return cssMntext;
    }

    public void setCssMntext(String cssMntext) {
        this.cssMntext = cssMntext;
    }

    public Player withCssMntext(String cssMntext) {
        this.cssMntext = cssMntext;
        return this;
    }

    public String getCssCaption() {
        return cssCaption;
    }

    public void setCssCaption(String cssCaption) {
        this.cssCaption = cssCaption;
    }

    public Player withCssCaption(String cssCaption) {
        this.cssCaption = cssCaption;
        return this;
    }

    public String getCssCttext() {
        return cssCttext;
    }

    public void setCssCttext(String cssCttext) {
        this.cssCttext = cssCttext;
    }

    public Player withCssCttext(String cssCttext) {
        this.cssCttext = cssCttext;
        return this;
    }

    public String getCssCtsize() {
        return cssCtsize;
    }

    public void setCssCtsize(String cssCtsize) {
        this.cssCtsize = cssCtsize;
    }

    public Player withCssCtsize(String cssCtsize) {
        this.cssCtsize = cssCtsize;
        return this;
    }

    public String getCssCtopacity() {
        return cssCtopacity;
    }

    public void setCssCtopacity(String cssCtopacity) {
        this.cssCtopacity = cssCtopacity;
    }

    public Player withCssCtopacity(String cssCtopacity) {
        this.cssCtopacity = cssCtopacity;
        return this;
    }

    public String getCssCtedge() {
        return cssCtedge;
    }

    public void setCssCtedge(String cssCtedge) {
        this.cssCtedge = cssCtedge;
    }

    public Player withCssCtedge(String cssCtedge) {
        this.cssCtedge = cssCtedge;
        return this;
    }

    public String getCssIcon() {
        return cssIcon;
    }

    public void setCssIcon(String cssIcon) {
        this.cssIcon = cssIcon;
    }

    public Player withCssIcon(String cssIcon) {
        this.cssIcon = cssIcon;
        return this;
    }

    public String getCssIchover() {
        return cssIchover;
    }

    public void setCssIchover(String cssIchover) {
        this.cssIchover = cssIchover;
    }

    public Player withCssIchover(String cssIchover) {
        this.cssIchover = cssIchover;
        return this;
    }

    public String getCssTsprogress() {
        return cssTsprogress;
    }

    public void setCssTsprogress(String cssTsprogress) {
        this.cssTsprogress = cssTsprogress;
    }

    public Player withCssTsprogress(String cssTsprogress) {
        this.cssTsprogress = cssTsprogress;
        return this;
    }

    public String getCssTsrail() {
        return cssTsrail;
    }

    public void setCssTsrail(String cssTsrail) {
        this.cssTsrail = cssTsrail;
    }

    public Player withCssTsrail(String cssTsrail) {
        this.cssTsrail = cssTsrail;
        return this;
    }

    public String getCssButton() {
        return cssButton;
    }

    public void setCssButton(String cssButton) {
        this.cssButton = cssButton;
    }

    public Player withCssButton(String cssButton) {
        this.cssButton = cssButton;
        return this;
    }

    public String getCssBttext() {
        return cssBttext;
    }

    public void setCssBttext(String cssBttext) {
        this.cssBttext = cssBttext;
    }

    public Player withCssBttext(String cssBttext) {
        this.cssBttext = cssBttext;
        return this;
    }

    public Boolean getOptAutostart() {
        return optAutostart;
    }

    public void setOptAutostart(Boolean optAutostart) {
        this.optAutostart = optAutostart;
    }

    public Player withOptAutostart(Boolean optAutostart) {
        this.optAutostart = optAutostart;
        return this;
    }

    public Boolean getOptTitle() {
        return optTitle;
    }

    public void setOptTitle(Boolean optTitle) {
        this.optTitle = optTitle;
    }

    public Player withOptTitle(Boolean optTitle) {
        this.optTitle = optTitle;
        return this;
    }

    public Boolean getOptQuality() {
        return optQuality;
    }

    public void setOptQuality(Boolean optQuality) {
        this.optQuality = optQuality;
    }

    public Player withOptQuality(Boolean optQuality) {
        this.optQuality = optQuality;
        return this;
    }

    public Boolean getOptCaption() {
        return optCaption;
    }

    public void setOptCaption(Boolean optCaption) {
        this.optCaption = optCaption;
    }

    public Player withOptCaption(Boolean optCaption) {
        this.optCaption = optCaption;
        return this;
    }

    public Boolean getOptDownload() {
        return optDownload;
    }

    public void setOptDownload(Boolean optDownload) {
        this.optDownload = optDownload;
    }

    public Player withOptDownload(Boolean optDownload) {
        this.optDownload = optDownload;
        return this;
    }

    public Boolean getOptSharing() {
        return optSharing;
    }

    public void setOptSharing(Boolean optSharing) {
        this.optSharing = optSharing;
    }

    public Player withOptSharing(Boolean optSharing) {
        this.optSharing = optSharing;
        return this;
    }

    public Boolean getOptPlayrate() {
        return optPlayrate;
    }

    public void setOptPlayrate(Boolean optPlayrate) {
        this.optPlayrate = optPlayrate;
    }

    public Player withOptPlayrate(Boolean optPlayrate) {
        this.optPlayrate = optPlayrate;
        return this;
    }

    public Boolean getOptMute() {
        return optMute;
    }

    public void setOptMute(Boolean optMute) {
        this.optMute = optMute;
    }

    public Player withOptMute(Boolean optMute) {
        this.optMute = optMute;
        return this;
    }

    public Boolean getOptLoop() {
        return optLoop;
    }

    public void setOptLoop(Boolean optLoop) {
        this.optLoop = optLoop;
    }

    public Player withOptLoop(Boolean optLoop) {
        this.optLoop = optLoop;
        return this;
    }

    public Boolean getOptVr() {
        return optVr;
    }

    public void setOptVr(Boolean optVr) {
        this.optVr = optVr;
    }

    public Player withOptVr(Boolean optVr) {
        this.optVr = optVr;
        return this;
    }

    public Boolean getOptCast() {
        return optCast;
    }

    public void setOptCast(Boolean optCast) {
        this.optCast = optCast;
    }

    public Player withOptCast(Boolean optCast) {
        this.optCast = optCast;
        return this;
    }

    public Boolean getOptNodefault() {
        return optNodefault;
    }

    public void setOptNodefault(Boolean optNodefault) {
        this.optNodefault = optNodefault;
    }

    public Player withOptNodefault(Boolean optNodefault) {
        this.optNodefault = optNodefault;
        return this;
    }

    public Boolean getOptForceposter() {
        return optForceposter;
    }

    public void setOptForceposter(Boolean optForceposter) {
        this.optForceposter = optForceposter;
    }

    public Player withOptForceposter(Boolean optForceposter) {
        this.optForceposter = optForceposter;
        return this;
    }

    public Boolean getOptParameter() {
        return optParameter;
    }

    public void setOptParameter(Boolean optParameter) {
        this.optParameter = optParameter;
    }

    public Player withOptParameter(Boolean optParameter) {
        this.optParameter = optParameter;
        return this;
    }

    public String getRestrictDomain() {
        return restrictDomain;
    }

    public void setRestrictDomain(String restrictDomain) {
        this.restrictDomain = restrictDomain;
    }

    public Player withRestrictDomain(String restrictDomain) {
        this.restrictDomain = restrictDomain;
        return this;
    }

    public String getRestrictAction() {
        return restrictAction;
    }

    public void setRestrictAction(String restrictAction) {
        this.restrictAction = restrictAction;
    }

    public Player withRestrictAction(String restrictAction) {
        this.restrictAction = restrictAction;
        return this;
    }

    public String getRestrictTarget() {
        return restrictTarget;
    }

    public void setRestrictTarget(String restrictTarget) {
        this.restrictTarget = restrictTarget;
    }

    public Player withRestrictTarget(String restrictTarget) {
        this.restrictTarget = restrictTarget;
        return this;
    }

    public Boolean getResumeEnable() {
        return resumeEnable;
    }

    public void setResumeEnable(Boolean resumeEnable) {
        this.resumeEnable = resumeEnable;
    }

    public Player withResumeEnable(Boolean resumeEnable) {
        this.resumeEnable = resumeEnable;
        return this;
    }

    public String getResumeText() {
        return resumeText;
    }

    public void setResumeText(String resumeText) {
        this.resumeText = resumeText;
    }

    public Player withResumeText(String resumeText) {
        this.resumeText = resumeText;
        return this;
    }

    public String getResumeYes() {
        return resumeYes;
    }

    public void setResumeYes(String resumeYes) {
        this.resumeYes = resumeYes;
    }

    public Player withResumeYes(String resumeYes) {
        this.resumeYes = resumeYes;
        return this;
    }

    public String getResumeNo() {
        return resumeNo;
    }

    public void setResumeNo(String resumeNo) {
        this.resumeNo = resumeNo;
    }

    public Player withResumeNo(String resumeNo) {
        this.resumeNo = resumeNo;
        return this;
    }

    public Boolean getAdbEnable() {
        return adbEnable;
    }

    public void setAdbEnable(Boolean adbEnable) {
        this.adbEnable = adbEnable;
    }

    public Player withAdbEnable(Boolean adbEnable) {
        this.adbEnable = adbEnable;
        return this;
    }

    public String getAdbOffset() {
        return adbOffset;
    }

    public void setAdbOffset(String adbOffset) {
        this.adbOffset = adbOffset;
    }

    public Player withAdbOffset(String adbOffset) {
        this.adbOffset = adbOffset;
        return this;
    }

    public String getAdbText() {
        return adbText;
    }

    public void setAdbText(String adbText) {
        this.adbText = adbText;
    }

    public Player withAdbText(String adbText) {
        this.adbText = adbText;
        return this;
    }

    public Boolean getAdsAdult() {
        return adsAdult;
    }

    public void setAdsAdult(Boolean adsAdult) {
        this.adsAdult = adsAdult;
    }

    public Player withAdsAdult(Boolean adsAdult) {
        this.adsAdult = adsAdult;
        return this;
    }

    public Boolean getAdsPop() {
        return adsPop;
    }

    public void setAdsPop(Boolean adsPop) {
        this.adsPop = adsPop;
    }

    public Player withAdsPop(Boolean adsPop) {
        this.adsPop = adsPop;
        return this;
    }

    public Boolean getAdsVast() {
        return adsVast;
    }

    public void setAdsVast(Boolean adsVast) {
        this.adsVast = adsVast;
    }

    public Player withAdsVast(Boolean adsVast) {
        this.adsVast = adsVast;
        return this;
    }

    public Integer getAdsFree() {
        return adsFree;
    }

    public void setAdsFree(Integer adsFree) {
        this.adsFree = adsFree;
    }

    public Player withAdsFree(Integer adsFree) {
        this.adsFree = adsFree;
        return this;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Player withTrackingId(String trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    public Boolean getIncome() {
        return income;
    }

    public void setIncome(Boolean income) {
        this.income = income;
    }

    public Player withIncome(Boolean income) {
        this.income = income;
        return this;
    }

    public Boolean getIncomePop() {
        return incomePop;
    }

    public void setIncomePop(Boolean incomePop) {
        this.incomePop = incomePop;
    }

    public Player withIncomePop(Boolean incomePop) {
        this.incomePop = incomePop;
        return this;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public Player withLogger(String logger) {
        this.logger = logger;
        return this;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public Player withRevenue(String revenue) {
        this.revenue = revenue;
        return this;
    }

    public String getRevenueFallback() {
        return revenueFallback;
    }

    public void setRevenueFallback(String revenueFallback) {
        this.revenueFallback = revenueFallback;
    }

    public Player withRevenueFallback(String revenueFallback) {
        this.revenueFallback = revenueFallback;
        return this;
    }

    public String getRevenueTrack() {
        return revenueTrack;
    }

    public void setRevenueTrack(String revenueTrack) {
        this.revenueTrack = revenueTrack;
    }

    public Player withRevenueTrack(String revenueTrack) {
        this.revenueTrack = revenueTrack;
        return this;
    }

}
