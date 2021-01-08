package com.eme22.kumaanime.Databases.NewAnimes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.eme22.kumaanime.AppUtils.AnimeObjects.NewAnimeObject;
import com.eme22.kumaanime.Databases.MainDatabase;

public class NewAnimesTableViewModel extends AndroidViewModel {

    public final LiveData<PagedList<NewAnimeObject>> courseList;

    public NewAnimesTableViewModel(@NonNull Application application) {
        super(application);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPrefetchDistance(150)
                .setEnablePlaceholders(true)
                .build();

        courseList = new LivePagedListBuilder<>(MainDatabase.getInstance(getApplication()).newAnimesTableDao().getnewanimesbyid(), config).build();
    }
}
