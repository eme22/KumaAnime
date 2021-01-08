package com.eme22.kumaanime.Databases.NewAnimes;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.NewAnimeObject;
@Dao
public interface NewAnimesTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MiniAnime anime);

    @Delete
    void delete(MiniAnime anime);

    @Query("SELECT * FROM anime_new_table ORDER BY id DESC")
    DataSource.Factory<Integer, NewAnimeObject>  getnewanimesbyid();

    @Query("DELETE FROM anime_new_table")
    void nukeTable();


}
