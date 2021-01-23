package com.eme22.kumaanime.Databases.MainTable;

import android.database.Cursor;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;

import java.util.ArrayList;

@Dao
public interface MiniAnimeTable_Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MiniAnime anime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<MiniAnime> animes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAndReturn(MiniAnime anime);

    @Delete
    void delete(MiniAnime anime);

    @Query("DELETE FROM anime_main_table")
    void nuke();

    @Query("SELECT * FROM anime_main_table ORDER BY title ASC")
    DataSource.Factory<Integer,MiniAnime>  getallanimesbytitle();

    @Query("SELECT * FROM anime_main_table ORDER BY id DESC")
    DataSource.Factory<Integer,MiniAnime>  getallanimesbyid();

    @Query("SELECT * FROM anime_main_table WHERE title LIKE :name order by id ASC LIMIT 1")
    MiniAnime getanime(String name);

    @Query("SELECT * FROM anime_main_table WHERE id LIKE :id order by id ")
    MiniAnime getanime(Integer id);

    @Query("SELECT * FROM anime_main_table WHERE show_type LIKE :show_type ORDER BY title ASC")
    DataSource.Factory<Integer,MiniAnime>  getallanimesbytitleandid(int show_type);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(MiniAnime transaction);

    @Query("SELECT * FROM anime_main_table where title LIKE :input order by id ASC")
    DataSource.Factory<Integer,MiniAnime> loadAllTeamByName(String input);

    @Query("SELECT * FROM anime_main_table where title LIKE :input order by id ASC LIMIT :limit")
    DataSource.Factory<Integer,MiniAnime> loadAllTeamByName(String input, int limit);

    @Query("SELECT * FROM anime_main_table where title LIKE :input order by id ASC LIMIT :limit")
    Cursor loadAllTeamByNameCursor(String input, int limit);

    @Query("SELECT COUNT(id) FROM anime_main_table")
    int getDataCount();
}
