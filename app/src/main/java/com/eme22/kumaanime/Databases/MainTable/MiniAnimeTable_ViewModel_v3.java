package com.eme22.kumaanime.Databases.MainTable;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.Databases.MainDatabase;

public class MiniAnimeTable_ViewModel_v3 extends AndroidViewModel {


    private final MiniAnimeTable_Repo repo;

    private final LiveData<PagedList<MiniAnime>> courseList;

    private final LiveData<PagedList<MiniAnime>> searchList;

    private final MutableLiveData<String> filterList = new MutableLiveData<>();

    private int limit = 0;


    public MiniAnimeTable_ViewModel_v3(@NonNull Application application, int type) {
        super(application);
        //courseList = new LivePagedListBuilder<>(dao.getAllCourses(),50).build();

        repo = new MiniAnimeTable_Repo(application);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(50)
                .setPrefetchDistance(150)
                .setEnablePlaceholders(true)
                .build();

        if (type== -1) {
            courseList = new LivePagedListBuilder<>(MainDatabase.getInstance(getApplication()).miniAnimeTable_dao().getallanimesbytitleandid(type), config).build();
            searchList = null;
        }
        else if (type == -2) {
            courseList = null;
            searchList = Transformations.switchMap(filterList, input -> new LivePagedListBuilder<>(repo.getanimes(input), config).build());
        }
        else if (type == -3) {
            courseList = null;
            searchList = Transformations.switchMap(filterList, input -> new LivePagedListBuilder<>(repo.getanimes(input,limit), config).build());
        }
        else {
            courseList = new LivePagedListBuilder<>(MainDatabase.getInstance(getApplication()).miniAnimeTable_dao().getallanimesbytitleandid(type), config).build();
            searchList = null;
        }
    }

    public LiveData<PagedList<MiniAnime>> getCourseList() {
        return courseList;
    }

    public LiveData<PagedList<MiniAnime>> getSearchList() {
        return searchList;
    }

    public void setLimit(int limit) {
        this.limit = limit; }

    public void setFilterList(String filter) { filterList.setValue(filter); }

}
