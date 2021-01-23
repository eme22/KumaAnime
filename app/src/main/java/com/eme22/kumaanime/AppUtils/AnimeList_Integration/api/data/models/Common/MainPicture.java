
package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MainPicture implements Serializable, Parcelable {

    private int id;

    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("large")
    @Expose
    private String large;

    public MainPicture() {
    }

    public MainPicture(int id, String medium, String large) {
        this.id = id;
        this.medium = medium;
        this.large = large;
    }

    public MainPicture(String medium, String large) {
        this.medium = medium;
        this.large = large;
    }

    protected MainPicture(Parcel in) {
        id = in.readInt();
        medium = in.readString();
        large = in.readString();
    }

    public static final Creator<MainPicture> CREATOR = new Creator<MainPicture>() {
        @Override
        public MainPicture createFromParcel(Parcel in) {
            return new MainPicture(in);
        }

        @Override
        public MainPicture[] newArray(int size) {
            return new MainPicture[size];
        }
    };

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(medium);
        dest.writeString(large);
    }
}
