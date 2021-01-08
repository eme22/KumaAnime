package com.eme22.kumaanime.Databases;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.NewAnimeObject;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Dao;
import com.eme22.kumaanime.Databases.NewAnimes.NewAnimesTableDao;

@Database(entities = {MiniAnime.class, NewAnimeObject.class}, version = 1,exportSchema = false)
public abstract class MainDatabase extends RoomDatabase {

    public abstract MiniAnimeTable_Dao miniAnimeTable_dao();
    public abstract NewAnimesTableDao  newAnimesTableDao();
    private static MainDatabase INSTANCE;

    public static synchronized MainDatabase getInstance(final Context context){
        if (INSTANCE == null) INSTANCE = Room.databaseBuilder(context.getApplicationContext(),MainDatabase.class,"database").fallbackToDestructiveMigration().addCallback(roomCallback).build();
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
