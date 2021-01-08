package com.eme22.kumaanime.MainActivity_fragments.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eme22.kumaanime.MainActivity_fragments.anime_list_fragments.anime_directory_fragments.AnimeDirectoryAnimeList;

public class AnimeDirectoryPageAdapter extends FragmentStateAdapter {

    public AnimeDirectoryPageAdapter(FragmentActivity fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            default:
            case 0:
                return AnimeDirectoryAnimeList.newInstance(0);
            case 1:
                return AnimeDirectoryAnimeList.newInstance(1);
            case 2:
                return AnimeDirectoryAnimeList.newInstance(2);
            case 3:
                return AnimeDirectoryAnimeList.newInstance(3);
            case 4:
                return AnimeDirectoryAnimeList.newInstance(5);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
