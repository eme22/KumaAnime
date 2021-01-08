
package com.eme22.kumaanime.AppUtils.Servers.OKRu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Autoplay {

    @SerializedName("autoplayEnabled")
    @Expose
    private Boolean autoplayEnabled;
    @SerializedName("timeFromEnabled")
    @Expose
    private Boolean timeFromEnabled;
    @SerializedName("noRec")
    @Expose
    private Boolean noRec;
    @SerializedName("fullScreenExit")
    @Expose
    private Boolean fullScreenExit;
    @SerializedName("vitrinaSection")
    @Expose
    private String vitrinaSection;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Autoplay() {
    }

    /**
     * 
     * @param fullScreenExit
     * @param autoplayEnabled
     * @param vitrinaSection
     * @param noRec
     * @param timeFromEnabled
     */
    public Autoplay(Boolean autoplayEnabled, Boolean timeFromEnabled, Boolean noRec, Boolean fullScreenExit, String vitrinaSection) {
        super();
        this.autoplayEnabled = autoplayEnabled;
        this.timeFromEnabled = timeFromEnabled;
        this.noRec = noRec;
        this.fullScreenExit = fullScreenExit;
        this.vitrinaSection = vitrinaSection;
    }

    public Boolean getAutoplayEnabled() {
        return autoplayEnabled;
    }

    public void setAutoplayEnabled(Boolean autoplayEnabled) {
        this.autoplayEnabled = autoplayEnabled;
    }

    public Autoplay withAutoplayEnabled(Boolean autoplayEnabled) {
        this.autoplayEnabled = autoplayEnabled;
        return this;
    }

    public Boolean getTimeFromEnabled() {
        return timeFromEnabled;
    }

    public void setTimeFromEnabled(Boolean timeFromEnabled) {
        this.timeFromEnabled = timeFromEnabled;
    }

    public Autoplay withTimeFromEnabled(Boolean timeFromEnabled) {
        this.timeFromEnabled = timeFromEnabled;
        return this;
    }

    public Boolean getNoRec() {
        return noRec;
    }

    public void setNoRec(Boolean noRec) {
        this.noRec = noRec;
    }

    public Autoplay withNoRec(Boolean noRec) {
        this.noRec = noRec;
        return this;
    }

    public Boolean getFullScreenExit() {
        return fullScreenExit;
    }

    public void setFullScreenExit(Boolean fullScreenExit) {
        this.fullScreenExit = fullScreenExit;
    }

    public Autoplay withFullScreenExit(Boolean fullScreenExit) {
        this.fullScreenExit = fullScreenExit;
        return this;
    }

    public String getVitrinaSection() {
        return vitrinaSection;
    }

    public void setVitrinaSection(String vitrinaSection) {
        this.vitrinaSection = vitrinaSection;
    }

    public Autoplay withVitrinaSection(String vitrinaSection) {
        this.vitrinaSection = vitrinaSection;
        return this;
    }

}
