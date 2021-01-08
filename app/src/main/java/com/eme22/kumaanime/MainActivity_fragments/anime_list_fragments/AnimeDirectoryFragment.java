package com.eme22.kumaanime.MainActivity_fragments.anime_list_fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.kumaanime.MainActivity_fragments.util.AnimeDirectoryPageAdapter;
import com.eme22.kumaanime.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AnimeDirectoryFragment extends Fragment {

    private static final int BACK_LENGTH = 250;
    private long mLastClickTime = 0;

    Toolbar tb;
    TabLayout tabl;
    TabLayoutMediator tablm;
    ViewPager2 viewp;
    AnimeDirectoryPageAdapter pagea;


    // TODO: Rename and change types of parameters
    private String show_type;

    public AnimeDirectoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_anime_directory, container, false);

        pagea = new AnimeDirectoryPageAdapter(requireActivity());
        tb = v.findViewById(R.id.anime_directory_toolbar);
        tb.setNavigationIcon(R.drawable.ic_back);
        tb.setNavigationOnClickListener(v2 -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < BACK_LENGTH){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        AnimeDirectoryFragment.this.requireActivity().getSupportFragmentManager().popBackStack();
                    },
                    BACK_LENGTH);
        });
        tabl = v.findViewById(R.id.anime_directory_tablayout);
        viewp= v.findViewById(R.id.anime_directory_viewpager);
        viewp.setOffscreenPageLimit(3);
        viewp.setAdapter(pagea);
        tablm = new TabLayoutMediator(
                tabl,
                viewp,
                (tab, position) -> tab.setText(getTitle(position))
        );
        tablm.attach();

        return v;
    }

    private String getTitle(int position) {
        switch (position) {
            default:
            case 0:
                return getString(R.string.anime);
            case 1:
                return getString(R.string.movie);
            case 2:
                return getString(R.string.ova);
            case 3:
                return getString(R.string.special);
            case 4:
                return getString(R.string.spanish);

        }
    }
}