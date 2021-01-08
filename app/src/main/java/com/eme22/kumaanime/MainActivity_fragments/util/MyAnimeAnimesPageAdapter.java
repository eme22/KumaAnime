package com.eme22.kumaanime.MainActivity_fragments.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eme22.kumaanime.MainActivity_fragments.My_animes_fragment_fragments.MyAnimeListsFragment;


public class MyAnimeAnimesPageAdapter extends FragmentStateAdapter {

    public MyAnimeAnimesPageAdapter(FragmentActivity fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("POSITION", String.valueOf(position));
        switch (position) {
            default:
            case 0:
                return MyAnimeListsFragment.newInstance("WATCHING");
            case 1:
                return MyAnimeListsFragment.newInstance("COMPLETED");
            case 2:
                return MyAnimeListsFragment.newInstance("ON HOLD");
            case 3:
                return MyAnimeListsFragment.newInstance("DROPPED");
            case 4:
                return MyAnimeListsFragment.newInstance("PTW");
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
