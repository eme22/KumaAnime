package com.eme22.kumaanime.AnimeActivity_fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eme22.kumaanime.AnimeActivity;
import com.eme22.kumaanime.AnimeActivity_fragments.Utils.EpisodeFetcher;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.GeneralAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.OtherUtils;
import com.eme22.kumaanime.MainActivity_fragments.adapters.EpisodeAdapter;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.PermissionActivity;
import com.eme22.kumaanime.R;

import java.util.ArrayList;

public class AnimeEpisodes extends Fragment implements AnimeActivity.AnimeActivityLoad{

    private static final TaskRunner taskRunner = new TaskRunner();

    private static MiniAnime EXTRA_ANIME;
    private static RecyclerView episodes;
    private static CardView loading;
    static EpisodeAdapter adapter;

    private static final int BACK_LENGTH = 1000;
    private long mLastClickTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EXTRA_ANIME = ((AnimeActivity) requireActivity()).getGeneralAnime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_anime_episodes, container, false);
        initView(v);
        if (((AnimeActivity) requireActivity()).isLoaded()) loadepisodes();
        //loadepisodes();

        return v;
    }

    private void loadepisodes() {
        //LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver,
        //        new IntentFilter("EPISODE_SEEN_SEND"));

        GeneralAnime result = ((AnimeActivity) requireActivity()).getResult();

        int eps;
        try {
            eps = result.getDetails().getMyListStatus().getNumEpisodesWatched();
        }
        catch (NullPointerException e){
            eps = -1;
        }
        //((AnimeActivity) requireActivity()).setEpisode(eps);
        taskRunner.executeAsync(new EpisodeFetcher(EXTRA_ANIME, eps, new EpisodeFetcher.Callback() {
            @Override
            public void onSuccess(ArrayList<MiniEpisode> o) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    adapter.addAll(o);
                    Log.d("RECYCLER DATA:", String.valueOf(adapter.getItemCount()));
                    loading.setVisibility(View.GONE);
                });

            }

            @Override
            public void onError(Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    loading.setVisibility(View.GONE);
                    OtherUtils.toast(R.string.episodes_error);
                });

            }
        }));

    }

    private void initView(View v){

        ((AnimeActivity) requireActivity()).setAnimeeps(this);

        episodes = v.findViewById(R.id.episodes_recycler_inside);
        loading = v.findViewById(R.id.episodes_loading_snackbar);
        adapter = new EpisodeAdapter(0, new EpisodeAdapter.OnItemClicked() {
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
                for (String link: anime.getLink().split(",")) {
                    Log.d("AAA:" ,link);
                }

                taskRunner.executeAsync(new Play(requireActivity(),getParentFragmentManager(),anime));

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


            }
        });
        episodes.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        episodes.setAdapter(adapter);
    }

    public void reloadData(){


    }

    @Override
    public void loadData() {
        loadepisodes();
    }

}