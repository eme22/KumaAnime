package com.eme22.kumaanime.MainActivity_fragments.My_animes_fragment_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.Paging;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userlist.Datum;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userlist.UserList;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.GeneralAnimeActivity;
import com.eme22.kumaanime.MainActivity_fragments.adapters.AnimeAdapter_v4;
import com.eme22.kumaanime.MainActivity_fragments.util.PaginationScrollListener;
import com.eme22.kumaanime.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento del ViewPager, subclase de {@link Fragment}.
 * Use the {@link MyAnimeListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAnimeListsFragment extends Fragment {

    private static final String TYPE = "TYPE";
    private static MyAnimeListAPIService rss;
    //private final AnimeAdapter adapter1 = new AnimeAdapter(prin);
    //private AnimeAdapter_v3 adapter1;
    private static final int FIRST_INDEX = 0;
    private static final int PER_PAGE = 50;
    private int LAST_REQUESTED = FIRST_INDEX;
    private boolean REACHED_END = false;
    private boolean LOADING = false;
    private Call<UserList> anime;
    private String type;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AnimeAdapter_v4 adapter;

    public MyAnimeListsFragment() {
    }

    public static MyAnimeListsFragment newInstance(String param1) {
        MyAnimeListsFragment fragment = new MyAnimeListsFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        type = getArguments().getString(TYPE);
    }

    private void ptw() {
        anime = rss.getuseranimelist("@me","plan_to_watch","anime_start_date",PER_PAGE,LAST_REQUESTED);
        anime.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(@NotNull Call<UserList> call, @NotNull Response<UserList> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    Paging pages = response.body().getPaging();
                    REACHED_END = pages.getNext() == null;
                    List<Datum> datum = response.body().getData();
                    ParseResult(datum);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserList> call, @NotNull Throwable t) {

                t.printStackTrace();
            }
        });
    }

    private void dropped() {
        //rss = new MyAnimeListAPIAdapter(getContext()).getApiService();
        anime = rss.getuseranimelist("@me","dropped","anime_start_date",PER_PAGE,LAST_REQUESTED);
        anime.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(@NotNull Call<UserList> call, @NotNull Response<UserList> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    Paging pages = response.body().getPaging();
                    REACHED_END = pages.getNext() == null;
                    List<Datum> datum = response.body().getData();
                    ParseResult(datum);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserList> call, @NotNull Throwable t) {

                t.printStackTrace();
            }
        });
    }

    private void onhold() {
      //  rss = new MyAnimeListAPIAdapter(getContext()).getApiService();
        anime = rss.getuseranimelist("@me","on_hold","anime_start_date",PER_PAGE,LAST_REQUESTED);
        anime.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(@NotNull Call<UserList> call, @NotNull Response<UserList> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    Paging pages = response.body().getPaging();
                    REACHED_END = pages.getNext() == null;
                    List<Datum> datum = response.body().getData();
                    ParseResult(datum);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserList> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void completed() {
   //     rss = new MyAnimeListAPIAdapter(getContext()).getApiService();
        anime = rss.getuseranimelist("@me","completed","anime_start_date",PER_PAGE,LAST_REQUESTED);
        anime.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(@NotNull Call<UserList> call, @NotNull Response<UserList> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    Paging pages = response.body().getPaging();
                    REACHED_END = pages.getNext() == null;
                    List<Datum> datum = response.body().getData();
                    ParseResult(datum);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserList> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void watching() {
  //      rss = new MyAnimeListAPIAdapter(getContext()).getApiService();
        anime = rss.getuseranimelist("@me","watching","anime_start_date",PER_PAGE,LAST_REQUESTED);

        
        anime.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(@NotNull Call<UserList> call, @NotNull Response<UserList> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    Paging pages = response.body().getPaging();
                    REACHED_END = pages.getNext() == null;
                    List<Datum> datum = response.body().getData();
                    ParseResult(datum);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserList> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void ParseResult(List<Datum> datumList) {
        List<MiniAnime> list = new ArrayList<>();
        for (Datum d: datumList) {
            list.add(d.getNode());
        }
        if (LAST_REQUESTED == 0) swipeRefreshLayout.setRefreshing(false);
        if (LAST_REQUESTED != 0)  adapter.removeLoadingFooter();
        LOADING = false;
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.anime_list_general, container, false);
        RecyclerView rs = v.findViewById(R.id.animelist);
        rss = MyAnimeListAPIAdapter.getApiServiceWithAuth(getContext());
       // NestedScrollView ns = v.findViewById(R.id.nested_anime);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_animelist);
        swipeRefreshLayout.setRefreshing(true);

        // Setup Layout Manager
        linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rs.setLayoutManager(linearLayoutManager);
        rs.setItemAnimator(new DefaultItemAnimator());

// Setup Adapter
        adapter = new AnimeAdapter_v4(0, anime -> {
            Intent intent = new Intent(getActivity(), GeneralAnimeActivity.class);
            intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, (Parcelable) anime);
            startActivity(intent);
        });
        rs.setAdapter(adapter);
        rs.addOnScrollListener(new PaginationScrollListener(linearLayoutManager,swipeRefreshLayout) {
            @Override
            protected void loadMoreItems() {
                LOADING = true;
                LAST_REQUESTED=LAST_REQUESTED+PER_PAGE;
                adapter.addLoadingFooter();
                gettype(type);
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return REACHED_END;
            }

            @Override
            public boolean isLoading() {
                return LOADING;
            }




        });

        gettype(type);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!LOADING){
                LAST_REQUESTED=0;
                adapter.clear();
                gettype(type);
            }
        });


        /*
        adapter1 = new AnimeAdapter_v3(0,null, anime -> {

            Intent intent = new Intent(getActivity(), GeneralAnimeActivity.class);
            intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, anime);
            startActivity(intent);

        });
        rs = v.findViewById(R.id.animelist);
        ps= v.findViewById(R.id.animelist_progressbar);
        rs.setLayoutManager(new LinearLayoutManager(getContext()));
        rs.setAdapter(adapter1);
         */
        /*

        ns.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v1, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v1.getChildAt(0).getMeasuredHeight()- v1.getMeasuredHeight()){
                if(!REACHED_END){
                    LAST_REQUESTED=LAST_REQUESTED+PER_PAGE;
                    ps.setVisibility(View.VISIBLE);
                    gettype(type);
                }
            }
            //focusOnView();
        });

         */

        return v;
    }

   void gettype(String type2) {
       switch (type2) {
           case "WATCHING":
               watching();break;
           case "COMPLETED":
               completed();break;
           case "ON HOLD":
               onhold();break;
           case "DROPPED":
               dropped();break;
           case "PTW":
               ptw();break;
       }
   }


    //private void focusOnView(){
       // NestedScrollView sv = requireActivity().findViewById(R.id.my_animes_nestedscroll);
        //TabLayout sv_tablayout = requireActivity().findViewById(R.id.my_animes_tablayout);
        //sv.post(() -> sv.smoothScrollTo(0, sv_tablayout.getBottom()));
    //}
}