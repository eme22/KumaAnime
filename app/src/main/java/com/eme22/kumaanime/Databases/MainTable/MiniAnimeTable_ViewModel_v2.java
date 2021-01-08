package com.eme22.kumaanime.Databases.MainTable;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;

public class MiniAnimeTable_ViewModel_v2 extends ViewModel {

    public LiveData<PagedList<MiniAnime>> teamAllList;

    public MutableLiveData<String> filterTextAll = new MutableLiveData<>();

    public void initAllTeams(MiniAnimeTable_Dao teamDao) {
        PagedList.Config config = (new PagedList.Config.Builder())
                .setPageSize(10)
                .build();

        teamAllList = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("") || input.equals("%%")) {
//check if the current value is empty load all data else search
                return new LivePagedListBuilder<>(
                        teamDao.getallanimesbyid(), config)
                        .build();
            } else {
                Log.d("CURRENTINPUT: " , input);
                return new LivePagedListBuilder<>(
                        teamDao.loadAllTeamByName(input),config)
                        .build();
            }

        });

    }

}
