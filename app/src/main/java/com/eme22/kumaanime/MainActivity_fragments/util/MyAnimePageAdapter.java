package com.eme22.kumaanime.MainActivity_fragments.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata.AnimeStatistics;
import com.eme22.kumaanime.MainActivity_fragments.My_animes_fragment_fragments.MyAnimeStaticsFragment;
import com.eme22.kumaanime.MainActivity_fragments.My_animes_fragment_fragments.MyAnimeTypesFragment;

import java.util.concurrent.atomic.AtomicReference;

public class MyAnimePageAdapter extends FragmentStateAdapter {

    public MyAnimePageAdapter(FragmentActivity fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            default:
            case 0:
                return new MyAnimeStaticsFragment();
            case 1:
                return new MyAnimeTypesFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
