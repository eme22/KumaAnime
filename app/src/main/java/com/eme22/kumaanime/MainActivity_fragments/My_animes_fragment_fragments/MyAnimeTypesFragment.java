package com.eme22.kumaanime.MainActivity_fragments.My_animes_fragment_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.kumaanime.MainActivity_fragments.util.MyAnimeAnimesPageAdapter;
import com.eme22.kumaanime.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyAnimeTypesFragment extends Fragment {

    TabLayout tabl;
    TabLayoutMediator tablm;
    ViewPager2 viewp;
    MyAnimeAnimesPageAdapter pagea;
    //NestedScrollView sv;



    public MyAnimeTypesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_anime_types, container, false);

        pagea = new MyAnimeAnimesPageAdapter(getActivity());
        tabl = v.findViewById(R.id.myanime_tab_layout_types);
        viewp= v.findViewById(R.id.myanime_viewpager_types);
        viewp.setOffscreenPageLimit(4);
        viewp.setAdapter(pagea);
        //sv = requireActivity().findViewById(R.id.my_animes_nestedscroll);
        viewp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    if (position>0 && positionOffset==0.0f && positionOffsetPixels==0){
                        viewp.getLayoutParams().height =
                                viewp.getChildAt(0).getHeight();
                        //focusOnView(sv_tablayout);
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    //focusOnView(sv_tablayout);
                    }
        });
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
            case 0:
                return getString(R.string.watching);
            case 1:
                return getString(R.string.completed);
            case 2:
                return getString(R.string.on_hold);
            case 3:
                return getString(R.string.dropped);
            case 4:
                return getString(R.string.ptw);
            default:
                return null;
        }
    }

    //private void focusOnView(TabLayout tab){
    //    assert sv !=null;
    //    sv.post(() -> sv.smoothScrollTo(0, tab.getBottom()));
    //}


}