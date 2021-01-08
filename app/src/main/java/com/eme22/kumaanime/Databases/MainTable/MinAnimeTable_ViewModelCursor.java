package com.eme22.kumaanime.Databases.MainTable;

import android.app.Application;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;

import org.jetbrains.annotations.NotNull;

public class MinAnimeTable_ViewModelCursor extends AndroidViewModel {

    private final MiniAnimeTable_Repo repo;

    private final Cursor searchList;

    private String filterList = "";

    private int limit = 0;

    public MinAnimeTable_ViewModelCursor(@NonNull @NotNull Application application) {
        super(application);

        repo = new MiniAnimeTable_Repo(application);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(50)
                .setPrefetchDistance(150)
                .setEnablePlaceholders(true)
                .build();

        searchList = repo.getanimesWithCursor(filterList,limit);

    }

    public Cursor getSearchList() {
        return searchList;
    }

    public void setLimit(int limit) {
        this.limit = limit; }

    public void setFilterList(String filter) { filterList = filter; }
}
