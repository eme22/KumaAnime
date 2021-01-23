package com.eme22.kumaanime.MainActivity_fragments.My_animes_fragment_fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata.AnimeStatistics;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata.Userdata;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.R;

import org.jetbrains.annotations.NotNull;

import id.yuana.chart.pie.PieChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAnimeStaticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAnimeStaticsFragment extends Fragment {

    private TextView wat;
    private  TextView com;
    private  TextView onh;
    private  TextView drop;
    private TextView ptw3;
    private TextView animl;
    private TextView mscor;
    private  TextView eps;
    private TextView dias;
    private TextView rew;
    private PieChartView ch2;



    public MyAnimeStaticsFragment() {
    }

    public static MyAnimeStaticsFragment newInstance() {
        MyAnimeStaticsFragment fragment = new MyAnimeStaticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_anime_statics, container, false);

        NestedScrollView scrollView = v.findViewById(R.id.anime_statics_main_nest);
        wat = v.findViewById(R.id.user_watching);
        com = v.findViewById(R.id.user_completed);
        onh = v.findViewById(R.id.user_on_hold);
        drop = v.findViewById(R.id.user_dropped);
        ptw3 = v.findViewById(R.id.user_ptw);
        mscor = v.findViewById(R.id.user_mean);
        animl = v.findViewById(R.id.watched_animes_num_user);
        eps = v.findViewById(R.id.watched_episodes_num_user);
        dias = v.findViewById(R.id.user_days);
        rew = v.findViewById(R.id.rewatched_animes_num_user);
        ch2 = v.findViewById(R.id.pie_simple);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scrollView.setNestedScrollingEnabled(false);
        }

        getuserdata();

        return v;
    }

    void getuserdata(){
        final AnimeStatistics[] statics = new AnimeStatistics[1];
        MyAnimeListAPIService rss = MyAnimeListAPIAdapter.getApiServiceWithAuth(getContext());
        Call<Userdata> user = rss.getuserinfo("@me","anime_statistics");
        user.enqueue(new Callback<Userdata>() {
            @Override
            public void onResponse(@NotNull Call<Userdata> call, @NotNull Response<Userdata> response) {
                Userdata user2 = response.body();
                assert user2 != null;
                statics[0] = user2.getAnimeStatistics();
                int watching2 = statics[0].getNumItemsWatching();
                int completed2 = statics[0].getNumItemsCompleted();
                int onhold2 = statics[0].getNumItemsOnHold();
                int dropped2 = statics[0].getNumItemsDropped();
                int ptw2 = statics[0].getNumItemsPlanToWatch();
                int animes = statics[0].getNumItems();
                int episodes2 = statics[0].getNumEpisodes();
                double days2 = statics[0].getNumDays();
                double means = statics[0].getMeanScore();
                int rewatched = statics[0].getNumTimesRewatched();

                wat.setText(String.valueOf(watching2));

                com.setText(String.valueOf(completed2));

                onh.setText(String.valueOf(onhold2));

                drop.setText(String.valueOf(dropped2));

                ptw3.setText(String.valueOf(ptw2));

                mscor.setText(String.valueOf(means));

                animl.setText(String.valueOf(animes));

                eps.setText(String.valueOf(episodes2));

                dias.setText(String.valueOf(days2));

                rew.setText(String.valueOf(rewatched));

                ch2.setDataPoints(floatArrayOf((float) watching2, (float) completed2, (float) onhold2, (float) dropped2, (float) ptw2));

                checknight();

                ch2.setSliceColor(intArrayOf(R.color.green, R.color.blue, R.color.yellow, R.color.red, R.color.grey));

            }


            @Override
            public void onFailure(@NotNull Call<Userdata> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private float[] floatArrayOf(float...fs) {return fs;}
    private int[] intArrayOf(int...fs) {return fs;}

    void checknight(){

        int nightModeFlags =
                requireContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                ch2.setCenterColor(R.color.black);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                ch2.setCenterColor(R.color.white);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                ch2.setCenterColor(R.color.white);
                break;
        }

    }
}