package com.eme22.kumaanime.MainActivity_fragments.anime_list_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_ranking.AnimeRanking;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_ranking.Datum;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.Paging;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.GeneralAnimeActivity;
import com.eme22.kumaanime.MainActivity_fragments.adapters.TopAnimeAdapter;
import com.eme22.kumaanime.MainActivity_fragments.util.PaginationScrollListener;
import com.eme22.kumaanime.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopAnimeFragment extends Fragment {

    private static MyAnimeListAPIService rss;
    private static final int BACK_LENGTH = 250;
    private long mLastClickTime = 0;
    private static final int FIRST_INDEX = 0;
    private static final int PER_PAGE = 50;
    private int LAST_REQUESTED = FIRST_INDEX;
    private boolean REACHED_END = false;
    private boolean LOADING = false;
    RecyclerView rs;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TopAnimeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top_anime, container, false);

        init(view);

        load();

        return view;
    }

    private void init(View view) {
        rs = view.findViewById(R.id.top_anime_recyclerview);
        rss = MyAnimeListAPIAdapter.getApiServiceWithAuth(getContext());
        Toolbar tb = view.findViewById(R.id.top_anime_toolbar);
        tb.setNavigationIcon(R.drawable.ic_back);
        tb.setNavigationOnClickListener(v2 -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < BACK_LENGTH){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            new Handler(Looper.getMainLooper()).postDelayed(() -> TopAnimeFragment.this.requireActivity().getSupportFragmentManager().popBackStack(),
                    BACK_LENGTH);
        });
        swipeRefreshLayout = view.findViewById(R.id.top_anime_swiperefresh);
        swipeRefreshLayout.setRefreshing(true);
        linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rs.setLayoutManager(linearLayoutManager);
        //rs.setItemAnimator(new DefaultItemAnimator());
        adapter = new TopAnimeAdapter(anime -> {
            Intent intent = new Intent(getActivity(), GeneralAnimeActivity.class);
            intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, anime);
            startActivity(intent);
        });
        rs.setAdapter(adapter);
        rs.addOnScrollListener(new PaginationScrollListener(linearLayoutManager,swipeRefreshLayout) {
            @Override
            protected void loadMoreItems() {
                LOADING = true;
                LAST_REQUESTED=LAST_REQUESTED+PER_PAGE;
                adapter.addLoadingFooter();
                load();
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
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!LOADING){
                LAST_REQUESTED=0;
                adapter.clear();
                load();
            }
        });
    }

    private void load(){
        Call<AnimeRanking> anime = rss.getranking("all", PER_PAGE, LAST_REQUESTED, "");
        anime.enqueue(new Callback<AnimeRanking>() {
            @Override
            public void onResponse(@NotNull Call<AnimeRanking> call, @NotNull Response<AnimeRanking> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    Paging pages = response.body().getPaging();
                    REACHED_END = pages.getNext() == null;
                    List<Datum> datum = response.body().getData();
                    if (LAST_REQUESTED == 0) swipeRefreshLayout.setRefreshing(false);
                    if (LAST_REQUESTED != 0)  adapter.removeLoadingFooter();
                    LOADING = false;
                    adapter.addAll(datum);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NotNull Call<AnimeRanking> call, @NotNull Throwable t) {

            }
        });

    }



}