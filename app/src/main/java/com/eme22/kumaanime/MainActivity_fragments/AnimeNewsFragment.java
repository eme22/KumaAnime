package com.eme22.kumaanime.MainActivity_fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Browser;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.Paging;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.AppUtils.NewsObject.Datum;
import com.eme22.kumaanime.AppUtils.NewsObject.NewsList;
import com.eme22.kumaanime.AppUtils.NewsObject.NewsListFetcher_v2;
import com.eme22.kumaanime.MainActivity_fragments.adapters.NewsAdapter_v3;
import com.eme22.kumaanime.MainActivity_fragments.util.PaginationScrollListener;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.R;
import com.google.android.material.snackbar.Snackbar;
import com.tingyik90.prefmanager.PrefManager;

import java.util.List;

public class AnimeNewsFragment extends Fragment {

    private LinearLayoutManager linearLayoutManager;
    private NewsAdapter_v3 adapter;
    private PrefManager prefManager;
    private SharedPreferences prefs;
    private SwipeRefreshLayout refreshLayout;
    private final Snackbar snackbar=null;
    int MODE;

    private static final int PAGE_START = 1;

    private boolean isLoading = false;

    private boolean isLastPage = false;

    private int currentPage = PAGE_START;
    private TaskRunner taskRunner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(requireContext());
        MODE = Integer.parseInt(prefManager.getString("NEWSMODE","0"));
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        prefs.registerOnSharedPreferenceChangeListener(listener);
        taskRunner = new TaskRunner();
    }

    @Override
    public void onDestroy() {
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        Connection.hideError(snackbar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime_news, container, false);

        linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        refreshLayout = view.findViewById(R.id.news_swipetorefresh);
        refreshLayout.setRefreshing(true);
        RecyclerView recyclerView = view.findViewById(R.id.anime_news_recicler);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new NewsAdapter_v3(link -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            TypedValue typedValue = new TypedValue();
            requireActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            CustomTabColorSchemeParams colorSchemeParams = new CustomTabColorSchemeParams.Builder().setToolbarColor(typedValue.data).build();
            builder.setDefaultColorSchemeParams(colorSchemeParams);
            CustomTabsIntent customTabsIntent = builder.build();
            if (MODE == 1) {
                Bundle headers = new Bundle();
                headers.putString("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36");
                customTabsIntent.intent.putExtra(Browser.EXTRA_HEADERS, headers);
                customTabsIntent.launchUrl(requireContext(), Uri.parse("https://translate.google.com/translate?sl=en&tl=es&u="+link));}
            else customTabsIntent.launchUrl(requireContext(), Uri.parse(link));
        });
        recyclerView.setAdapter(adapter);



        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager,refreshLayout) {

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                //Increment page index to load the next one
                currentPage++;
                loadNextPage(view);
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage(view);

        refreshLayout.setOnRefreshListener(() -> {
            refreshview(view);
            refreshLayout.setRefreshing(false);
        });

        return view;
    }

    private void loadNextPage(View v){
        taskRunner.executeAsync(new NewsListFetcher_v2(currentPage, MODE, new Callback() {
            @Override
            public void onSuccess(Object o) {
                NewsList result = (NewsList) o;
                List<Datum> d = result.getData();
                new Handler(Looper.getMainLooper()).post(() -> adapter.removeLoadingFooter());
                isLoading = false;
                new Handler(Looper.getMainLooper()).post(() -> adapter.addAll(d));
                Paging p = result.getPaging();
                if (MODE == 1 || MODE == 2 || MODE == 3) {
                    if (p.getNext() == null) isLastPage  = true;
                    else new Handler(Looper.getMainLooper()).post(() -> adapter.addLoadingFooter());
                }
                else {
                    if(d.size()==0){
                        isLastPage  = true;
                    }
                    else new Handler(Looper.getMainLooper()).post(() -> adapter.addLoadingFooter());
                }
            }

            @Override
            public void onError(Exception e) {
                showError(v, snackbar, v1 -> loadNextPage(v));
            }
        }));
    }

    /*
    private void loadNextPage() {
        taskRunner.executeAsync(new NewsListFetcher(currentPage, MODE), result -> {
            List<Datum> d = result.getData();
            adapter.removeLoadingFooter();
            isLoading = false;
            adapter.addAll(d);
            Paging p = result.getPaging();
            if (MODE == 1 || MODE == 2 || MODE == 3) {
                if (p.getNext() == null) isLastPage  = true;
                else adapter.addLoadingFooter();
            }
            else {
                if(d.size()==0){
                    isLastPage  = true;
                }
                else adapter.addLoadingFooter();
            }
        });

    }
     */


    private void loadFirstPage(View v) {

        taskRunner.executeAsync(new NewsListFetcher_v2(PAGE_START, MODE, new Callback() {
            @Override
            public void onSuccess(Object o) {
                NewsList result = (NewsList) o;
                List<Datum> d = result.getData();
                new Handler(Looper.getMainLooper()).post(() -> refreshLayout.setRefreshing(false));
                Paging p = result.getPaging();
                new Handler(Looper.getMainLooper()).post(() -> adapter.addAll(d));


                if (MODE == 1 || MODE == 2 || MODE == 3) {
                    if (p.getNext() == null) isLastPage  = true;
                }
                else {
                    if(d.size()==0){
                        isLastPage  = true;
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                showError(v, snackbar, v1 -> loadFirstPage(v1));
            }
        }));

        /*
        taskRunner.executeAsync(new NewsListFetcher(PAGE_START, MODE), result -> {
            List<Datum> d = result.getData();
            refreshLayout.setRefreshing(false);
            Paging p = result.getPaging();
            adapter.addAll(d);

            if (MODE == 1 || MODE == 2 || MODE == 3) {
                if (p.getNext() == null) isLastPage  = true;
            }
            else {
                if(d.size()==0){
                    isLastPage  = true;
                }
            }

    });

         */
    }



    private final SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
        if (key.equals("NEWSMODE")){
            refreshview(getView());
        }
    };

    private void refreshview(View v) {
        refreshLayout.setRefreshing(true);
        currentPage = PAGE_START;
        MODE = Integer.parseInt(prefManager.getString("NEWSMODE","0"));
        adapter.clear();
        loadFirstPage(v);

    }

    private void showError(View masterLayout, Snackbar snackbar, View.OnClickListener listener){
        Connection.showError(masterLayout, snackbar,getString(R.string.error),listener);
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (!visible && snackbar != null ){
            snackbar.dismiss();
        }
    }


}
