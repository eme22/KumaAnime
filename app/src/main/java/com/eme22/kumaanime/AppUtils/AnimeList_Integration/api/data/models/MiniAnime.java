package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.MainPicture;
import com.eme22.kumaanime.AppUtils.StringUtils;
import com.eme22.kumaanime.Databases.AnimeDataConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "anime_main_table", indices = {@Index(value = "title",
        unique = true)}) @TypeConverters(AnimeDataConverter.class)
public class MiniAnime implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("main_picture")
    //@Ignore
    @Expose
    private MainPicture mainPicture;

    private String link;


    // 0 = ANIME 1 = OVA 2 = PELICULA 3 = ESPECIAL (SI EXISTE)
    // 5 = ESPAÑOL
    private int show_type;

    public MiniAnime() {
    }
    @Ignore
    public MiniAnime(Integer id, String title, MainPicture mainPicture, String link, int show_type) {
        this.id = id;
        this.title = title;
        this.mainPicture = mainPicture;
        this.link = link;
        this.show_type = show_type;
    }

    protected MiniAnime(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        mainPicture = in.readParcelable(MainPicture.class.getClassLoader());
        link = in.readString();
        show_type = in.readInt();
    }

    public static final Creator<MiniAnime> CREATOR = new Creator<MiniAnime>() {
        @Override
        public MiniAnime createFromParcel(Parcel in) {
            return new MiniAnime(in);
        }

        @Override
        public MiniAnime[] newArray(int size) {
            return new MiniAnime[size];
        }
    };

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

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

    public int getShow_type() { return show_type; }

    public void setShow_type(int show_type) { this.show_type = show_type; }

    public static DiffUtil.ItemCallback<MiniAnime> DIFF_CALLBACK = new DiffUtil.ItemCallback<MiniAnime>() {

        @Override
        public boolean areItemsTheSame(@NonNull MiniAnime oldItem, @NonNull MiniAnime newItem) {
            return StringUtils.compareAnimes(oldItem.getTitle(),newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MiniAnime oldItem, @NonNull MiniAnime newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;

        if (obj == this)
            return true;

        MiniAnime country = (MiniAnime) obj;

        return StringUtils.compareAnimes(country.getTitle(),this.getTitle()) && country.getShow_type() == this.getShow_type();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeParcelable(mainPicture, flags);
        dest.writeString(link);
        dest.writeInt(show_type);
    }
}

