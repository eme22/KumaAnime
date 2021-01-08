package com.eme22.kumaanime.AppUtils.AnimeObjects;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.Databases.AnimeDataConverter;

@Entity(tableName = "anime_new_table") @TypeConverters(AnimeDataConverter.class)
public class NewAnimeObject extends MiniAnime {

}
