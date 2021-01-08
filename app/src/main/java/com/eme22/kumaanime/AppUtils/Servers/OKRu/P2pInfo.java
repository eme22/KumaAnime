
package com.eme22.kumaanime.AppUtils.Servers.OKRu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class P2pInfo {

    @SerializedName("isPeerEnabled")
    @Expose
    private Boolean isPeerEnabled;
    @SerializedName("ubsc")
    @Expose
    private Integer ubsc;
    @SerializedName("pbsc")
    @Expose
    private Integer pbsc;
    @SerializedName("mptpc")
    @Expose
    private Integer mptpc;
    @SerializedName("pctmt")
    @Expose
    private Integer pctmt;
    @SerializedName("pbesc")
    @Expose
    private Integer pbesc;
    @SerializedName("prrt")
    @Expose
    private Integer prrt;
    @SerializedName("srt")
    @Expose
    private Integer srt;
    @SerializedName("swrt")
    @Expose
    private Integer swrt;
    @SerializedName("dctt")
    @Expose
    private Integer dctt;

    /**
     * No args constructor for use in serialization
     * 
     */
    public P2pInfo() {
    }

    /**
     * 
     * @param mptpc
     * @param prrt
     * @param swrt
     * @param pctmt
     * @param dctt
     * @param srt
     * @param pbsc
     * @param ubsc
     * @param pbesc
     * @param isPeerEnabled
     */
    public P2pInfo(Boolean isPeerEnabled, Integer ubsc, Integer pbsc, Integer mptpc, Integer pctmt, Integer pbesc, Integer prrt, Integer srt, Integer swrt, Integer dctt) {
        super();
        this.isPeerEnabled = isPeerEnabled;
        this.ubsc = ubsc;
        this.pbsc = pbsc;
        this.mptpc = mptpc;
        this.pctmt = pctmt;
        this.pbesc = pbesc;
        this.prrt = prrt;
        this.srt = srt;
        this.swrt = swrt;
        this.dctt = dctt;
    }

    public Boolean getIsPeerEnabled() {
        return isPeerEnabled;
    }

    public void setIsPeerEnabled(Boolean isPeerEnabled) {
        this.isPeerEnabled = isPeerEnabled;
    }

    public P2pInfo withIsPeerEnabled(Boolean isPeerEnabled) {
        this.isPeerEnabled = isPeerEnabled;
        return this;
    }

    public Integer getUbsc() {
        return ubsc;
    }

    public void setUbsc(Integer ubsc) {
        this.ubsc = ubsc;
    }

    public P2pInfo withUbsc(Integer ubsc) {
        this.ubsc = ubsc;
        return this;
    }

    public Integer getPbsc() {
        return pbsc;
    }

    public void setPbsc(Integer pbsc) {
        this.pbsc = pbsc;
    }

    public P2pInfo withPbsc(Integer pbsc) {
        this.pbsc = pbsc;
        return this;
    }

    public Integer getMptpc() {
        return mptpc;
    }

    public void setMptpc(Integer mptpc) {
        this.mptpc = mptpc;
    }

    public P2pInfo withMptpc(Integer mptpc) {
        this.mptpc = mptpc;
        return this;
    }

    public Integer getPctmt() {
        return pctmt;
    }

    public void setPctmt(Integer pctmt) {
        this.pctmt = pctmt;
    }

    public P2pInfo withPctmt(Integer pctmt) {
        this.pctmt = pctmt;
        return this;
    }

    public Integer getPbesc() {
        return pbesc;
    }

    public void setPbesc(Integer pbesc) {
        this.pbesc = pbesc;
    }

    public P2pInfo withPbesc(Integer pbesc) {
        this.pbesc = pbesc;
        return this;
    }

    public Integer getPrrt() {
        return prrt;
    }

    public void setPrrt(Integer prrt) {
        this.prrt = prrt;
    }

    public P2pInfo withPrrt(Integer prrt) {
        this.prrt = prrt;
        return this;
    }

    public Integer getSrt() {
        return srt;
    }

    public void setSrt(Integer srt) {
        this.srt = srt;
    }

    public P2pInfo withSrt(Integer srt) {
        this.srt = srt;
        return this;
    }

    public Integer getSwrt() {
        return swrt;
    }

    public void setSwrt(Integer swrt) {
        this.swrt = swrt;
    }

    public P2pInfo withSwrt(Integer swrt) {
        this.swrt = swrt;
        return this;
    }

    public Integer getDctt() {
        return dctt;
    }

    public void setDctt(Integer dctt) {
        this.dctt = dctt;
    }

    public P2pInfo withDctt(Integer dctt) {
        this.dctt = dctt;
        return this;
    }

}
