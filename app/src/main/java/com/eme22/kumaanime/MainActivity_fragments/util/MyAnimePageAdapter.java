package com.eme22.kumaanime.MainActivity_fragments.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata.AnimeStatistics;
import com.eme22.kumaanime.MainActivity_fragments.My_animes_fragment_fragments.MyAnimeStaticsFragment;
import com.eme22.kumaanime.MainActivity_fragments.My_animes_fragment_fragments.MyAnimeTypesFragment;

public class MyAnimePageAdapter extends FragmentStateAdapter {
    private final int tabs = 2;

    AnimeStatistics ess;

    public MyAnimePageAdapter(FragmentActivity fm, AnimeStatistics es) {
        super(fm);
        this.ess=es;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyAnimeStaticsFragment();
            case 1:
                return new MyAnimeTypesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return tabs;
    }

}
