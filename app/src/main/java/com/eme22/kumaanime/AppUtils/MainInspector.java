package com.eme22.kumaanime.AppUtils;

import android.os.Parcel;
import android.os.Parcelable;

public class MainInspector extends Thread implements Parcelable {

    protected MainInspector(Parcel in) {
    }

    public static final Creator<MainInspector> CREATOR = new Creator<MainInspector>() {
        @Override
        public MainInspector createFromParcel(Parcel in) {
            return new MainInspector(in);
        }

        @Override
        public MainInspector[] newArray(int size) {
            return new MainInspector[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
