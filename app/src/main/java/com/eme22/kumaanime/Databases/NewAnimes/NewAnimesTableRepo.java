package com.eme22.kumaanime.Databases.NewAnimes;

import android.content.Context;

import androidx.paging.DataSource;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.NewAnimeObject;
import com.eme22.kumaanime.Databases.MainDatabase;

import java.util.concurrent.Callable;

public class NewAnimesTableRepo {
    private final NewAnimesTableDao dao;
    private final DataSource.Factory<Integer, NewAnimeObject> newanimebyid;

    public NewAnimesTableRepo(Context context) {
        MainDatabase database = MainDatabase.getInstance(context);
        dao = database.newAnimesTableDao();
        newanimebyid = dao.getnewanimesbyid();
    }

    public DataSource.Factory<Integer, NewAnimeObject> getAllanimebyid() {
        return newanimebyid;
    }

    public void insert(NewAnimeObject anime){
        new NewAnimesTableRepo.InsertAsync(dao, anime).call();
    }

    public void deleteall(){
        new NewAnimesTableRepo.DeleteAllAsync(dao).call();
    }

    public void delete(NewAnimeObject anime){
        new NewAnimesTableRepo.DeleteAsync(dao, anime).call();
    }

    private static class InsertAsync implements Callable<Void> {

        private final NewAnimesTableDao dao;
        private final NewAnimeObject anime;
        public InsertAsync(NewAnimesTableDao dao, NewAnimeObject anime) {
            this.anime=anime;
            this.dao=dao;
        }

        @Override
        public Void call() {
            dao.insert(anime);
            return null;
        }
    }

    private static class DeleteAllAsync implements Callable<Void> {
        private final NewAnimesTableDao dao;

        public DeleteAllAsync(NewAnimesTableDao dao) {
            this.dao = dao;
        }

        @Override
        public Void call() {
            dao.nukeTable();
            return null;
        }
    }

    private static class DeleteAsync implements Callable<Void> {
        private final NewAnimesTableDao dao;
        private final MiniAnime anime;

        public DeleteAsync(NewAnimesTableDao dao, NewAnimeObject anime) {
            this.dao = dao;
            this.anime = anime;
        }

        @Override
        public Void call() {
            dao.delete(anime);
            return null;
        }
    }
}
