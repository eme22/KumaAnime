package com.eme22.kumaanime.Databases.MainTable;

import android.content.Context;
import android.database.Cursor;

import androidx.paging.DataSource;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.Databases.MainDatabase;

import java.util.concurrent.Callable;

public class MiniAnimeTable_Repo {
    private final MiniAnimeTable_Dao dao;
    private final DataSource.Factory<Integer, MiniAnime> allanimebytitle;
    private final DataSource.Factory<Integer, MiniAnime> allanimebyid;

    public MiniAnimeTable_Repo(Context context) {
        MainDatabase database = MainDatabase.getInstance(context);
        dao = database.miniAnimeTable_dao();
        allanimebytitle = dao.getallanimesbytitle();
        allanimebyid = dao.getallanimesbyid();
    }

    public DataSource.Factory<Integer, MiniAnime> getAllanimebytitle() {
        return allanimebytitle;
    }

    public DataSource.Factory<Integer, MiniAnime> getAllanimebyid() {
        return allanimebyid;
    }

    public DataSource.Factory<Integer, MiniAnime> getanimes(String anime) {
        return new SearchAsync(dao, anime).call();
    }

    public Cursor getanimesWithCursor(String anime, int limit) {
        return new SearchCursorAsync(dao, anime,limit).call();
    }

    public DataSource.Factory<Integer, MiniAnime> getanimes(String anime, int limit) {
        return new SearchAsync(dao, anime,limit).call();
    }

    public MiniAnime getanime(String anime) {
        return new GetAsync(dao, anime).call();
    }

    public MiniAnime getanime(Integer anime_id) {
        return new GetAsync(dao, anime_id).call();
    }

    public void insert(MiniAnime anime){
        new InsertAsync(dao, anime).call();
    }

    public void update(MiniAnime anime){
        new UpdateAsync(dao, anime).call();
    }

    public void delete(MiniAnime anime){
        new DeleteAsync(dao, anime).call();
    }

    public void nuke() {new NukeAsync(dao).call();}

    public int count() {return new getAnimeCount(dao).call();}

    private static class InsertAsync implements Callable<Void> {

        private final MiniAnimeTable_Dao dao;
        private final MiniAnime anime;
        public InsertAsync(MiniAnimeTable_Dao dao, MiniAnime anime) {
            this.anime=anime;
            this.dao=dao;
        }

        @Override
        public Void call() {
            dao.insert(anime);
            return null;
        }
    }


    private static class UpdateAsync implements Callable<Void> {
        private final MiniAnimeTable_Dao dao;
        private final MiniAnime anime;

        public UpdateAsync(MiniAnimeTable_Dao dao, MiniAnime anime) {
            this.dao = dao;
            this.anime = anime;
        }

        @Override
        public Void call() {
            dao.update(anime);
            return null;
        }
    }

    private static class DeleteAsync implements Callable<Void> {
        private final MiniAnimeTable_Dao dao;
        private final MiniAnime anime;

        public DeleteAsync(MiniAnimeTable_Dao dao, MiniAnime anime) {
            this.dao = dao;
            this.anime = anime;
        }

        @Override
        public Void call() {
            dao.delete(anime);
            return null;
        }
    }

    private static class NukeAsync implements Callable<Void> {
        private final MiniAnimeTable_Dao dao;

        public NukeAsync(MiniAnimeTable_Dao dao) {
            this.dao = dao;
        }

        @Override
        public Void call() {
            dao.nuke();
            return null;
        }
    }

    private static class GetAsync implements Callable<MiniAnime> {

        private final MiniAnimeTable_Dao dao;
        private final String anime;
        private final Integer anime_id;

        public GetAsync(MiniAnimeTable_Dao dao, String anime) {
            this.dao = dao;
            this.anime = anime;
            this.anime_id = null;
        }

        public GetAsync(MiniAnimeTable_Dao dao, Integer anime_id) {
            this.dao = dao;
            this.anime_id = anime_id;
            this.anime = null;
        }

        @Override
        public MiniAnime call() {
            if (anime == null)return dao.getanime(anime_id);
            else if (anime_id == null)  return dao.getanime(anime);
            else throw new NullPointerException();
        }
    }

    private static class SearchCursorAsync implements Callable<Cursor> {

        private final MiniAnimeTable_Dao dao;
        private final String anime;
        private final int limit;

        public SearchCursorAsync(MiniAnimeTable_Dao dao, String anime, int limit) {
            this.dao = dao;
            this.anime = anime;
            this.limit = limit;
        }

        @Override
        public Cursor call() {
            return dao.loadAllTeamByNameCursor(anime,limit);
        }
    }

    private static class SearchAsync implements Callable<DataSource.Factory<Integer, MiniAnime>> {

        private final MiniAnimeTable_Dao dao;
        private final String anime;
        private final int limit;

        public SearchAsync(MiniAnimeTable_Dao dao, String anime, int limit) {
            this.dao = dao;
            this.anime = anime;
            this.limit = limit;
        }

        public SearchAsync(MiniAnimeTable_Dao dao, String anime) {
            this.dao = dao;
            this.anime = anime;
            this.limit=0;
        }

        @Override
        public DataSource.Factory<Integer, MiniAnime> call() {
            if (limit>0) return dao.loadAllTeamByName(anime,limit);
            else return dao.loadAllTeamByName(anime);
        }
    }

    private static class getAnimeCount implements Callable<Integer> {
        private final MiniAnimeTable_Dao dao;

        public getAnimeCount(MiniAnimeTable_Dao dao) {
            this.dao = dao;
        }

        @Override
        public Integer call() {
            return dao.getDataCount();
        }
    }
}
