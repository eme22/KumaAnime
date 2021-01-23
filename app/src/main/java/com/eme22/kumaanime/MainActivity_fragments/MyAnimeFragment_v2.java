package com.eme22.kumaanime.MainActivity_fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.kumaanime.AppUtils.Mods.CustomLockableNestedScrollView;
import com.eme22.kumaanime.AppUtils.Mods.CustomNestedScrollableHost;
import com.eme22.kumaanime.MainActivity_fragments.util.MyAnimePageAdapter;
import com.eme22.kumaanime.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class MyAnimeFragment_v2 extends Fragment {

    TabLayout tabLayout;
    TabLayoutMediator tabLayoutMediator;
    ViewPager2 viewPager2;
    MyAnimePageAdapter pagea;

    ConstraintLayout myall;
    CustomNestedScrollableHost cps;
    ProgressBar pp;
    CustomLockableNestedScrollView myScrollView;
    SwipeRefreshLayout refreshLayout;

    ImageView foto;

    TextView name;
    TextView  id;
    TextView place;
    TextView date;
    TextView  id_text;
    TextView place_text;
    TextView date_text;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_anime_login2, container, false);
        AppBarLayout appBarLayout = v.findViewById(R.id.ablAppBar);
        TabLayout tabl = v.findViewById(R.id.my_animes_tablayout);
        ViewPager2 viewp = v.findViewById(R.id.user_viewpager);
        pagea = new MyAnimePageAdapter(requireActivity());
        viewp.setAdapter(pagea);
        TabLayoutMediator tablm = new TabLayoutMediator(
                tabl,
                viewp,
                (tab, position) -> tab.setText(getTitle(position))
        );
        tablm.attach();

        viewp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                if( position != 0) {
                    appBarLayout.setExpanded(false);
                }

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewp.setNestedScrollingEnabled(false);
        }

        return v;
    }


    private String getTitle(int position) {
        switch (position) {
            case 0:
                return getString(R.string.statics);
            case 1:
                return getString(R.string.animes);
            default:
                return null;
        }
    }
}
