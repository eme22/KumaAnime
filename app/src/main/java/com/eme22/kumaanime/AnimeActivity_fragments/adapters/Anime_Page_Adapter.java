package com.eme22.kumaanime.AnimeActivity_fragments.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eme22.kumaanime.AnimeActivity_fragments.AnimeEpisodes;
import com.eme22.kumaanime.AnimeActivity_fragments.AnimeInfo;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;

public class Anime_Page_Adapter extends FragmentStateAdapter {

    public Anime_Page_Adapter(FragmentActivity fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new AnimeEpisodes();
        }
        return new AnimeInfo();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
