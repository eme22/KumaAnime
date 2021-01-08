package com.eme22.kumaanime.MainActivity_fragments.helpers;

import androidx.lifecycle.MutableLiveData;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userlist.Datum;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userlist.UserList;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAnimeRepo {

    private final MyAnimeListAPIService service;

    private static final int MAL_API_STARTING_INDEX = 1;

    MutableLiveData<ArrayList<MiniAnime>> cache = new MutableLiveData<>();

    private final int lastrequestedpage = MAL_API_STARTING_INDEX;

    private boolean isrequestinprogress = false;

    private static final int PAGE_SIZE = 50;

    public MyAnimeRepo(MyAnimeListAPIService service) {
        this.service = service;
    }

    private void getWatchingAnimes(){

        isrequestinprogress = true;
        final boolean[] success = {false};

        Call<UserList> response = service.getuseranimelist("@me","watching","list_updated_at",PAGE_SIZE,lastrequestedpage);

        response.enqueue(new Callback<UserList>() {

            final ArrayList<MiniAnime> ni = new ArrayList<>();

            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {

                List<Datum> datum = response.body().getData();
                for (int i=0;i<datum.size();i++){
                    ni.add(datum.get(i).getNode());
                    success[0] = true;
                }

            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                t.printStackTrace();
            }
        });
}

}
