package com.eme22.kumaanime.MainActivity_fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata.AnimeStatistics;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata.Userdata;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.AppUtils.Mods.CustomLockableNestedScrollView;
import com.eme22.kumaanime.AppUtils.Mods.CustomNestedScrollableHost;
import com.eme22.kumaanime.MainActivity_fragments.util.MyAnimePageAdapter;
import com.eme22.kumaanime.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tingyik90.prefmanager.PrefManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAnimeFragment extends Fragment {

    TabLayout tabl;
    TabLayoutMediator tablm;
    ViewPager2 viewp;
    MyAnimePageAdapter pagea;
    ImageView foto;
    //NestedScrollView sv;
    ConstraintLayout ll;
    CustomNestedScrollableHost cps;
    ProgressBar pp;
    CustomLockableNestedScrollView myScrollView;

    TextView name;
    TextView  id;
    TextView place;
    TextView date;
    TextView  id_text;
    TextView place_text;
    TextView date_text;

    private View header;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PrefManager prefs = new PrefManager(requireContext());
        View v;

        if(prefs.getBoolean("isLogged",false)){
            v = inflater.inflate(R.layout.fragment_my_anime_login, container, false);

            {
                id_text = v.findViewById(R.id.user_id_text);
                place_text = v.findViewById(R.id.user_place_text);
                date_text = v.findViewById(R.id.user_date_text);
            }
            name = v.findViewById(R.id.user_name);
            id = v.findViewById(R.id.user_id);
            place = v.findViewById(R.id.user_place);
            date = v.findViewById(R.id.user_date);
            foto = v.findViewById(R.id.profile_photo);
            //sv = v.findViewById(R.id.my_animes_nestedscroll);
            //sv.setNestedScrollingEnabled(false);
            ll = v.findViewById(R.id.myanime_userdataset1);
            pp = v.findViewById(R.id.myanime_mainbar);
            cps = v.findViewById(R.id.myanime_customnested);
            ll.setVisibility(View.GONE);
            //cps.setVisibility(View.GONE);
            AnimeStatistics ess = getuserdata();
            myScrollView = v.findViewById(R.id.nested_scroll_test);

            header = v.findViewById(R.id.my_anime_appbar);

            pagea = new MyAnimePageAdapter(getActivity(),ess);
            tabl = v.findViewById(R.id.my_animes_tablayout);
            viewp= v.findViewById(R.id.user_viewpager);
            viewp.setAdapter(pagea);
            View v2 = (View) viewp.getParent().getParent().getParent();
            viewp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    int height = v2.getHeight() - header.getHeight();
                    if (position == 1 && positionOffset==0.0f && positionOffsetPixels==0){ viewp.getLayoutParams().height = height;
                    }
                    if (position == 0 && positionOffset==0.0f && positionOffsetPixels==0){
                        View v3 = viewp.getChildAt(0);
                        int height2 =  v3.findViewById(R.id.anime_statics_main_layout).getHeight();
                        v3.findViewById(R.id.anime_statics_dummy_layout).setMinimumHeight(height2);
                        viewp.getLayoutParams().height = height2;
                    }

                    Log.d("VP HEIGHT: ", String.valueOf(viewp.getHeight()));
                    /*
                    {

                    }

                     */


                }

                @Override
                public void onPageSelected(int position) {

                    //View v2 = getParentFragmentManager().findFragmentByTag("f" + viewp.getCurrentItem()).getView();
                    //assert v2 != null;

                    if( position != 0) {
                        myScrollView.setScrollingEnabled(false);
                        hidetab();

                    }
                    else {
                        myScrollView.setScrollingEnabled(true);
                        showtab();
                    }
                    //updatePagerHeightForChild(v2,viewp);
                }
            });
            tablm = new TabLayoutMediator(
                    tabl,
                    viewp,
                    (tab, position) -> tab.setText(getTitle(position))
            );
            tablm.attach();
        }
        else {
            v = inflater.inflate(R.layout.fragment_my_anime_nologin, container, false);
        }




        return v;
    }

    private void updatePagerHeightForChild(View v2, ViewPager2 viewp) {
        v2.post(() -> {
            int wMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(v2.getWidth(), View.MeasureSpec.EXACTLY);
            int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            v2.measure(wMeasureSpec, hMeasureSpec);

            if (viewp.getLayoutParams().height != v2.getMeasuredHeight()) {
                viewp.getLayoutParams().height = v2.getMeasuredHeight();
            }
        });
    }

    private void showtab() {
        id.setVisibility(View.VISIBLE);
        place.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        id_text.setVisibility(View.VISIBLE);
        place_text.setVisibility(View.VISIBLE);
        date_text.setVisibility(View.VISIBLE);
    }

    private void hidetab() {
        id.setVisibility(View.GONE);
        place.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        id_text.setVisibility(View.GONE);
        place_text.setVisibility(View.GONE);
        date_text.setVisibility(View.GONE);
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

    AnimeStatistics getuserdata(){
        final AnimeStatistics[] statics = new AnimeStatistics[1];
        MyAnimeListAPIService rss = MyAnimeListAPIAdapter.getApiServiceWithAuth(getContext());
        Call<Userdata> user = rss.getuserinfo("@me","anime_statistics");
        user.enqueue(new Callback<Userdata>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<Userdata> call, @NotNull Response<Userdata> response) {
                Userdata user2 = response.body();
                assert user2 != null;
                statics[0] = user2.getAnimeStatistics();
                name.setText(user2.getName());
                id.setText(user2.getId().toString());
                String loc = user2.getLocation();
                if(loc.isEmpty()) place.setText("Desconocido");
                else place.setText(user2.getLocation());
                date.setText(user2.getJoinedAt());
                ImageUtils.getSharedInstance().load(user2.getPicture()).placeholder(new CircularProgressDrawable(requireContext())).into(foto);
                //Glide.with(requireContext()).load(user2.getPicture()).placeholder(new CircularProgressDrawable(requireContext())).error(R.drawable.no_preview_2).into(foto);
                ll.setVisibility(View.VISIBLE);
                //cps.setVisibility(View.VISIBLE);
                pp.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<Userdata> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
        return statics[0];
    }

    //private void focusOnView(){
    //    sv.post(() -> sv.smoothScrollTo(0,tabl.getBottom()));
    //}

}