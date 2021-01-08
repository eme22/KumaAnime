package com.eme22.kumaanime.Databases.MainTable;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;

public class MiniAnimeTable_ViewModel extends AndroidViewModel {

    private final MiniAnimeTable_Repo repo;
    public LiveData allanimes;

    public MiniAnimeTable_ViewModel(@NonNull Application application) {
        super(application);
        repo = new MiniAnimeTable_Repo(application);
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                        .setPrefetchDistance(10)
                        .setPageSize(20).build();
    }

    public void init(int mode) {

        if (mode==1){
            allanimes = (new LivePagedListBuilder<>(repo.getAllanimebyid()
                    , 20))
                    .build();
        }
        else {
            allanimes = (new LivePagedListBuilder<>(repo.getAllanimebytitle()
                    , 20))
                    .build();
        }



    }


    public void insert (MiniAnime anime){
        repo.insert(anime);
    }

    public void update (MiniAnime anime){
        repo.update(anime);
    }

    public void delete(MiniAnime anime){
        repo.delete(anime);
    }

    public LiveData<PagedList<MiniAnime>> getAllanimes(){
        return allanimes;
    }

}
