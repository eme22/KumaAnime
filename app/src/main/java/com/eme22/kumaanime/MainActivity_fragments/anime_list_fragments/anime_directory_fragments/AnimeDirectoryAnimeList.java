package com.eme22.kumaanime.MainActivity_fragments.anime_list_fragments.anime_directory_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_ViewModel_v3;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_ViewModel_v3_Factory;
import com.eme22.kumaanime.GeneralAnimeActivity;
import com.eme22.kumaanime.MainActivity_fragments.adapters.AnimeDirectoryAdapterLive;
import com.eme22.kumaanime.R;

import org.jetbrains.annotations.NotNull;

public class AnimeDirectoryAnimeList extends Fragment {

    private static final String SHOW_TYPE = "SHOW_TYPE";
    private static final String SHOW_QUERY = "SHOW_QUERY";
    private int show_type;
    private String  show_query;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private AnimeDirectoryAdapterLive adapter;
    private RecyclerView rs;
    private MiniAnimeTable_ViewModel_v3 viewmodelv3;


    public static AnimeDirectoryAnimeList newInstance(int TYPE) {
        AnimeDirectoryAnimeList fragment = new AnimeDirectoryAnimeList();
        Bundle args = new Bundle();
        args.putInt(SHOW_TYPE, TYPE);
        fragment.setArguments(args);
        return fragment;
    }

    public static AnimeDirectoryAnimeList newInstance(String query) {
        AnimeDirectoryAnimeList fragment = new AnimeDirectoryAnimeList();
        Bundle args = new Bundle();
        args.putInt(SHOW_TYPE, -2);
        args.putString(SHOW_QUERY, "%"+query+"%");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            show_type = getArguments().getInt(SHOW_TYPE);
            show_query = getArguments().getString(SHOW_QUERY);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        if (show_type == -2) v = inflater.inflate(R.layout.anime_list_general_simple, container, false);
        else v = inflater.inflate(R.layout.anime_list_general, container, false);
        initViews(v);
        generateAdapter();

        return v;
    }

    protected void initViews(View v) {
        viewmodelv3 = new ViewModelProvider(this, new MiniAnimeTable_ViewModel_v3_Factory(requireActivity().getApplication(), show_type)).get(MiniAnimeTable_ViewModel_v3.class);
        rs = v.findViewById(R.id.animelist);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_animelist);
        linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
    }

    protected void generateAdapter() {
        adapter = new AnimeDirectoryAdapterLive(anime -> {
            Intent intent = new Intent(requireContext(), GeneralAnimeActivity.class);
            intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, anime);
            requireContext().startActivity(intent);
        },requireContext());
        rs.setLayoutManager(linearLayoutManager);
        rs.setItemAnimator(new DefaultItemAnimator());
        rs.setAdapter(adapter);
        rs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstcompletelyvisible = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                swipeRefreshLayout.setEnabled(firstcompletelyvisible == 0);

            }
        });
        if (show_query !=null) {
            viewmodelv3.setFilterList(show_query);
            viewmodelv3.getSearchList().observe(requireActivity(), miniAnimes -> adapter.submitList(miniAnimes));
        }

        else viewmodelv3.getCourseList().observe(requireActivity(), courses -> {
                Log.v("Course","#onChanged called & course list size is "+courses.size());
                adapter.submitList(courses);
            });


    }

    public void setShow_query(String show_query) {
        this.show_query = show_query;
        viewmodelv3.setFilterList(show_query);
        viewmodelv3.getSearchList().observe(requireActivity(), miniAnimes -> adapter.submitList(miniAnimes));
    }
}
