package com.eme22.kumaanime.MainActivity_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eme22.kumaanime.AnimeActivity;
import com.eme22.kumaanime.AnimeActivity_fragments.Download;
import com.eme22.kumaanime.AnimeActivity_fragments.Play;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;
import com.eme22.kumaanime.GeneralAnimeActivity;
import com.eme22.kumaanime.MainActivity_fragments.adapters.AnimeAdapter_v4;
import com.eme22.kumaanime.MainActivity_fragments.adapters.EpisodeAdapter;
import com.eme22.kumaanime.MainActivity_fragments.util.NewAnimeFetcher_v3;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.PermissionActivity;
import com.eme22.kumaanime.R;
import com.google.android.material.snackbar.Snackbar;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class NewAnimeFragment extends Fragment {

    private static final TaskRunner taskRunner = new TaskRunner();
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView animes;
    private Snackbar snackbar;
    private int error;
    EpisodeAdapter mAdapter;
    AnimeAdapter_v4 mAdapter2;
    private TextView episodes2;
    private boolean finishLoading = false;

    private static final int BACK_LENGTH = 1000;
    private long mLastClickTime = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snackbar = null;
    }

    @Override
    public void onStart() {
        Connection.hideError(snackbar);
        error = 0;
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_anime, container, false);

        initView(v);

        manageview();

        firstload();

        return v;
    }


    @SuppressWarnings("unchecked")
    private void firstload(){
        taskRunner.executeAsync(new NewAnimeFetcher_v3(requireContext(), new com.eme22.kumaanime.AppUtils.Callback() {
            @Override
            public void onSuccess(Object o) {
                NewAnimeFetcher_v3.Tuple<ArrayList<MiniAnime>,ArrayList<MiniEpisode>> result = (NewAnimeFetcher_v3.Tuple<ArrayList<MiniAnime>, ArrayList<MiniEpisode>>) o;

                requireActivity().runOnUiThread(() -> {
                    mAdapter.addAll(result.getSecond());
                    mAdapter2.addAll(result.getFirst());
                });

                /*
                new Handler(Looper.getMainLooper()).post(() -> {
                    mAdapter.addAll(result[1]);
                    mAdapter2.addAll(result[0]);
                });

                 */



                Connection.hideError(snackbar);
                error = 0;
                new Handler(Looper.getMainLooper()).post(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    //recyclerView.setVisibility(View.VISIBLE);
                    //recyclerView2.setVisibility(View.VISIBLE);
                    animes.setVisibility(View.VISIBLE);
                    episodes2.setVisibility(View.VISIBLE);
                });


                finishLoading = true;
            }

            @Override
            public void onError(Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> swipeRefreshLayout.setRefreshing(false));
                if (e instanceof NullPointerException){
                    error = 1;
                    showError(getView(), R.string.error, snackbar, v -> firstload());
                    e.printStackTrace();
                }
                else if (e instanceof  UnknownHostException){
                    error = 3;
                    showError(getView(), R.string.network_error, snackbar, v -> firstload());
                    e.printStackTrace();
                }
                else e.printStackTrace();
            }
        }));

    }




/*
    private void firstload() {
        taskRunner.executeAsync(new NewAnimeFetcher_v2(requireContext()), result -> {

            try {
                mAdapter2.addAll(result.getFirst());
                mAdapter.addAll(result.getSecond());
                Connection.hideError(snackbar);
                error = 0;
            }
            catch (NullPointerException e){
                error = 1;
                showError(getView(), R.string.error, snackbar, v -> firstload());
            }

            swipeRefreshLayout.setRefreshing(false);
            animes.setVisibility(View.VISIBLE);
            episodes2.setVisibility(View.VISIBLE);
            finishLoading = true;

        });
    }

 */


    private void initView(View v) {
        swipeRefreshLayout = v.findViewById(R.id.swipetest_newanime);
        animes = v.findViewById(R.id.new_animes_title);
        episodes2 = v.findViewById(R.id.new_episodes_title);
        RecyclerView recyclerView = v.findViewById(R.id.episodes_recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView recyclerView2 = v.findViewById(R.id.offline_main_last_added_recycler);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView.refreshDrawableState();
        mAdapter2 = new AnimeAdapter_v4(1, this::loadAnime);
        mAdapter = new EpisodeAdapter(1, new EpisodeAdapter.OnItemClicked() {


            @Override
            public void onPlayClick(MiniEpisode anime) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < BACK_LENGTH){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                /*
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.setAction(PlayActivity.ACTION_1);
                intent.putExtra(PlayActivity.DATA_1,anime.getLink());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                new Handler(Looper.getMainLooper()).postDelayed(() -> startActivity(intent), BACK_LENGTH);
                 */

                taskRunner.executeAsync(new Play(requireContext(),getParentFragmentManager(),anime));

                //Fragment f = PlayFragment.newInstance(anime.getLink());
                //getParentFragmentManager().beginTransaction().add(R.id.main_container, f, f.getClass().getSimpleName()).addToBackStack(null).commit();


            }

            @Override
            public void onForceStatusUpdate(MiniEpisode anime) {

            }

            @Override
            public void onDownloadClick(MiniEpisode anime) {
                taskRunner.executeAsync(new Download((PermissionActivity) requireActivity(),getParentFragmentManager(),anime));
            }

            @Override
            public void onCastClick(MiniEpisode anime) {

            }

            @Override
            public void onManageClick(MiniEpisode anime) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < BACK_LENGTH){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                taskRunner.executeAsync(new NewAnimeFragment.SearchAnimeAsync(requireContext(), anime.getAnimeID(), new NewAnimeFragment.Callback() {
                    @Override
                    public void onSuccess(MiniAnime o) {
                        /*
                        Intent intent = new Intent(NewAnimeFragment.this.getActivity(), GeneralAnimeActivity.class);
                        intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, (Parcelable) o);
                        NewAnimeFragment.this.startActivity(intent);
                        */
                        loadAnime(o);
                    }


                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                }));

            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView2.setAdapter(mAdapter2);
        swipeRefreshLayout.setOnRefreshListener(this::updateAnimes);


    }

    private void loadAnime(MiniAnime anime) {
        //Intent intent = new Intent(NewAnimeFragment.this.requireActivity(), GeneralAnimeActivity.class);
        Intent intent = new Intent(NewAnimeFragment.this.requireActivity(), AnimeActivity.class);
        intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, (Parcelable) anime);
        NewAnimeFragment.this.startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    private void updateAnimes(){

        taskRunner.executeAsync(new NewAnimeFetcher_v3(requireActivity().getApplication(), new com.eme22.kumaanime.AppUtils.Callback() {
            @Override
            public void onSuccess(Object o) {
                NewAnimeFetcher_v3.Tuple<ArrayList<MiniAnime>,ArrayList<MiniEpisode>> result = (NewAnimeFetcher_v3.Tuple<ArrayList<MiniAnime>, ArrayList<MiniEpisode>>) o;
                requireActivity().runOnUiThread(() -> {
                    mAdapter.clear();
                    mAdapter2.clear();
                    mAdapter.addAll(result.getSecond());
                    mAdapter2.addAll(result.getFirst());
                });
                /*
                new Handler(Looper.getMainLooper()).post(() -> {
                        mAdapter.clear();
                        mAdapter2.clear();
                        mAdapter.addAll(result[1]);
                        mAdapter2.addAll(result[0]);
                });

                 */



                error = 0;
                Connection.hideError(snackbar);
                //mAdapter.setEpisodes(result.getSecond());
                new Handler(Looper.getMainLooper()).post(() -> swipeRefreshLayout.setRefreshing(false));
            }

            @Override
            public void onError(Exception e) {
                new Handler(Looper.getMainLooper()).post(() ->  swipeRefreshLayout.setRefreshing(false));
                if (e instanceof NullPointerException){
                    error = 2;
                    showError(getView(), R.string.error, snackbar, v -> updateAnimes());
                    e.printStackTrace();
                }
                else if (e instanceof UnknownHostException){
                    error = 4;
                    showError(getView(), R.string.network_error, snackbar, v -> updateAnimes());
                    e.printStackTrace();
                }
                else e.printStackTrace();
            }
        }));



    }

/*
    private void updateAnimes() {
        taskRunner.executeAsync(new NewAnimeFetcher_v2(requireActivity().getApplication()), result -> {
            mAdapter2.clear();
            mAdapter.clear();
            try {
                mAdapter2.addAll(result.getFirst());
                mAdapter.addAll(result.getSecond());
                error = 0;
            }
            catch (NullPointerException e ){
                error = 2;
                showError(getView(), R.string.error, snackbar, v -> updateAnimes());
            }

            Connection.hideError(snackbar);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

 */



    void manageview(){
        if(!finishLoading) {
            //recyclerView.setVisibility(View.GONE);
            //recyclerView2.setVisibility(View.GONE);
            animes.setVisibility(View.GONE);
            episodes2.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d("PAUSED", ":P");
        Connection.hideError(snackbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d("RESUMED", ":P");
        //Log.d("RECYCLERSTATUS1", String.valueOf(recyclerView.getMeasuredHeight()));
        //Log.d("RECYCLERSTATUS1", String.valueOf(mAdapter.getItemCount()));
        //Log.d("RECYCLERSTATUS2", String.valueOf(recyclerView2.getMeasuredHeight()));
        //Log.d("RECYCLERSTATUS2", String.valueOf(mAdapter2.getItemCount()));
        if (error == 1) showError(getView(), R.string.error, snackbar, v -> firstload());
        else if (error == 2) showError(getView(), R.string.error, snackbar, v -> updateAnimes());
        else if  (error == 3) showError(getView(), R.string.network_error, snackbar, v -> firstload());
        else if  (error == 4) showError(getView(), R.string.network_error, snackbar, v -> updateAnimes());
    }
    /*
   void getanimes(){
        taskRunner.executeAsync(new NewAnimeFetcher(requireActivity().getApplication()), result -> {
            mAdapter = new EpisodeAdapter(NewAnimeFragment.this.getActivity(), result.getSecond());
            recyclerView.setAdapter(mAdapter);
            mAdapter2 = new AnimeAdapter_v3(1, result.getFirst(), anime -> {

                Intent intent = new Intent(NewAnimeFragment.this.getActivity(), GeneralAnimeActivity.class);
                intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, anime);
                NewAnimeFragment.this.startActivity(intent);

            });
            recyclerView2.setAdapter(mAdapter2);
            swipeRefreshLayout.setRefreshing(false);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.VISIBLE);
            animes.setVisibility(View.VISIBLE);
            episodes2.setVisibility(View.VISIBLE);
            finishLoading = true;
        });
    }

     */

    private void showError(View masterLayout, @StringRes Integer error, Snackbar snackbar, View.OnClickListener listener){
        Connection.showError(masterLayout, snackbar,getString(error),listener);
    }

    private static class SearchAnimeAsync implements Runnable {

        private final MiniAnimeTable_Repo repo;
        int DBid;
        NewAnimeFragment.Callback callback;

        public SearchAnimeAsync(Context context, int DBid, NewAnimeFragment.Callback callback) {
            this.repo = new MiniAnimeTable_Repo(context);
            this.DBid = DBid;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                MiniAnime ggwp = repo.getanime(DBid);
                callback.onSuccess(ggwp);
            } catch (Exception e) {
                callback.onError(e);
            }

        }
    }

    private interface Callback {
        void onSuccess(MiniAnime animes);
        void onError(Exception e);
    }




}