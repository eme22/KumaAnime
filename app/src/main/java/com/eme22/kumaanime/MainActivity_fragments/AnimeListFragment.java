package com.eme22.kumaanime.MainActivity_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.eme22.kumaanime.AnimeActivity;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_list.AnimeList;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_ViewModel;
import com.eme22.kumaanime.GeneralAnimeActivity;
import com.eme22.kumaanime.MainActivity_fragments.anime_list_fragments.AnimeDirectoryFragment;
import com.eme22.kumaanime.MainActivity_fragments.anime_list_fragments.TopAnimeFragment;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.R;
import com.tingyik90.prefmanager.PrefManager;

import org.jetbrains.annotations.NotNull;

public class AnimeListFragment extends Fragment {

    ConstraintLayout main;

    RecyclerView recent_animes_recyclerview,recommended_animes_recyclerview,categories_animes_recyclerview;
    CardView top,seasonal,list,random;

    private MiniAnimeTable_ViewModel viewModel;
    private PrefManager prefManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        prefManager = new PrefManager(requireContext());
        if (prefManager.getBoolean("isLogged", false)){
            v = inflater.inflate(R.layout.fragment_anime_list_login, container, false);
            top = v.findViewById(R.id.Top_anime_cardview);
            top.setOnClickListener(v12 -> {
                Fragment fragment = new TopAnimeFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            });
        }
        else {
            v = inflater.inflate(R.layout.fragment_anime_list_no_login, container, false);
        }
            list = v.findViewById(R.id.anime_list_cardview);
            list.setOnClickListener(v1 -> {
                Fragment fragment = new AnimeDirectoryFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            });
            random = v.findViewById(R.id.random_anime_cardview);
            random.setOnClickListener(v13 -> new TaskRunner().executeAsync(new requestDBCount(requireContext(), new AnimeListFragment.Callback() {
                @Override
                public void onSuccess(MiniAnime o) {
                    /*
                    Intent intent = new Intent(AnimeListFragment.this.getActivity(), GeneralAnimeActivity.class);
                    intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, (Parcelable) o);
                    AnimeListFragment.this.startActivity(intent);
                    */
                    loadAnime(o);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            })));

        return v;
    }

    private static class requestDBCount implements Runnable{
        AnimeListFragment.Callback callback;
        MiniAnimeTable_Repo repo;

        public requestDBCount(Context context, AnimeListFragment.Callback callback) {
            this.callback = callback;
            this.repo = new MiniAnimeTable_Repo(context);
        }

        @Override
        public void run() {
            try {
                MiniAnime a = repo.getanime((int) (Math.random() * repo.count()) + 1);
                callback.onSuccess(a);
            }
            catch (Exception e){
                callback.onError(e);
            }
        }
    }

    private void loadAnime(MiniAnime anime) {
        //Intent intent = new Intent(NewAnimeFragment.this.requireActivity(), GeneralAnimeActivity.class);
        Intent intent = new Intent(AnimeListFragment.this.requireActivity(), AnimeActivity.class);
        intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, (Parcelable) anime);
        AnimeListFragment.this.startActivity(intent);
    }

    private interface Callback {
        void onSuccess(MiniAnime animes);
        void onError(Exception e);
    }

}

/*
        MiniAnimeTable_ViewModel_v2 viewModel2 = ViewModelProviders.of(requireActivity()).get(MiniAnimeTable_ViewModel_v2.class);
        viewModel2.initAllTeams(MainDatabase.getInstance(requireActivity()).miniAnimeTable_dao());
        MiniAnimeTable_ViewModel_PagedListAdapter adapter = new MiniAnimeTable_ViewModel_PagedListAdapter(requireActivity());
        viewModel2.teamAllList.observe(
                requireActivity(), pagedList -> {
                    try {
                        Log.e("Paging ", "PageAll" + pagedList.size());
                        adapter.submitList(pagedList);
                    } catch (Exception ignored) {}
                });


        View v = inflater.inflate(R.layout.fragment_anime_list_login, container, false);
        viewModel = ViewModelProviders.of(this).get(MiniAnimeTable_ViewModel.class);
        viewModel.init(1);
        AnimeAdapter_v3_live animeAdapter_v3_live = new AnimeAdapter_v3_live(anime -> {

            Intent intent = new Intent(getActivity(), GeneralAnimeActivity.class);
            intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, anime);
            startActivity(intent);
        });
        Log.d("ANIMES", String.valueOf(animeAdapter_v3_live.getItemCount()));
 */