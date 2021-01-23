package com.eme22.kumaanime.MainActivity_fragments.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eme22.kumaanime.MainActivity_fragments.AnimeListFragment;
import com.eme22.kumaanime.MainActivity_fragments.AnimeNewsFragment_v3;
import com.eme22.kumaanime.MainActivity_fragments.DownloadedAnimeFragment;
import com.eme22.kumaanime.MainActivity_fragments.MyAnimeFragment;
import com.eme22.kumaanime.MainActivity_fragments.MyAnimeFragment_v2;
import com.eme22.kumaanime.MainActivity_fragments.NewAnimeFragment;


public class MainPageAdapter extends FragmentStateAdapter {

    private final int tabs = 5;

    public MainPageAdapter(FragmentActivity fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DownloadedAnimeFragment();
                //return MyAnimeListsFragment.newInstance("PTW");
            case 1:
                return new AnimeListFragment();
            case 2:
                return new NewAnimeFragment();
                //return new DownloadedAnimeFragment();
            case 3:
                return new MyAnimeFragment_v2();
            case 4:
                return new AnimeNewsFragment_v3();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return tabs;
    }
}
