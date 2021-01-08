package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.MainPicture;
import com.eme22.kumaanime.Databases.AnimeDataConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "anime_main_table") @TypeConverters(AnimeDataConverter.class)
public class MiniAnime implements Serializable {

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
            return oldItem.title.toUpperCase().equals(newItem.title.toUpperCase());
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

        return country.title.toUpperCase().equals(this.title.toUpperCase()) && country.getShow_type() == this.getShow_type();
    }
}

