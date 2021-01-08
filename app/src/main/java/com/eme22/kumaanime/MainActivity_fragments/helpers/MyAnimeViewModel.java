package com.eme22.kumaanime.MainActivity_fragments.helpers;

import androidx.lifecycle.MutableLiveData;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;

import java.util.ArrayList;

public class MyAnimeViewModel {

    MutableLiveData<ArrayList<MiniAnime>> userLiveData;
    ArrayList<MiniAnime> userArrayList;

    public MyAnimeViewModel() {
        userLiveData = new MutableLiveData<>();

        // call your Rest API in init method
        init();
    }

    public MutableLiveData<ArrayList<MiniAnime>> getUserMutableLiveData() {
        return userLiveData;
    }

    public void init(){
        userLiveData.setValue(userArrayList);
    }

}