package com.eme22.kumaanime;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager_v2;
import com.eme22.kumaanime.AnimeActivity_fragments.adapters.animePageAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.AppUtils.Theming;
import com.eme22.kumaanime.MainActivity_fragments.adapters.EpisodeAdapterOffline;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tingyik90.prefmanager.PrefManager;

import java.util.ArrayList;
import java.util.Objects;

public class GeneralAnimeActivity_v2 extends PermissionActivity{

    //Common
    public static final String EXTRA_ANIME = "EXTRA ANIME";
    Toolbar toolbar;
    TextView nameView;
    ImageView imageView;
    Theming theme;
    MiniAnime GeneralAnime;
    private int viewed = -1;
    final TaskRunner taskRunner = new TaskRunner();
    boolean isContextualEnabled = false;
    int contextualcounter = 0;
    DownloadManager_v2 downloadManager;
    PrefManager prefs;

    //Online
    FragmentStateAdapter adapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    private TabLayoutMediator tabLayoutMediator;

    //Offline

    SwipeRefreshLayout refreshLayout;
    RecyclerView offline;
    EpisodeAdapterOffline offlineRecycler;
    final ArrayList<MiniEpisodeOffline> deleteList = new ArrayList<>();


    void initViewOnline(){
        theme = new Theming(this);
        theme.themecheck();
        downloadManager = new DownloadManager_v2(this);
        prefs = new PrefManager(this);
        //setContentView(R.layout.activity_general_anime);
        setContentView(R.layout.activity_general_anime_v2);
        //refreshLayout = findViewById(R.id.anime_refreshlayout);
        //refreshLayout.setRefreshing(true);
        toolbar = findViewById(R.id.anime_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        adapter = new animePageAdapter(this);
        tabLayout = findViewById(R.id.anime_window_tab);
        tabLayout.setVisibility(View.GONE);
        viewPager2 = findViewById(R.id.anime_viewpager);
        imageView =(ImageView) findViewById(R.id.anime_image);
        nameView = findViewById(R.id.animename);
        nameView.setText(GeneralAnime.getTitle());
        nameView.setVisibility(View.GONE);
        ImageUtils.getSharedInstance().load(GeneralAnime.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(this)).error(R.drawable.no_preview_2).into(imageView);
    }

    void initViewPager(){
        //refreshLayout.setRefreshing(false);
        tabLayout.setVisibility(View.VISIBLE);
        nameView.setVisibility(View.VISIBLE);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(0, true);
        tabLayoutMediator = new TabLayoutMediator(
                tabLayout,
                viewPager2,
                (tab, position) -> tab.setText(onlineGetTitle(position))
        );
        tabLayoutMediator.attach();
    }

    void initViewOffline(){
        theme = new Theming(this);
        theme.themecheck();
        downloadManager = new DownloadManager_v2(this);
        setContentView(R.layout.activity_general_anime_offline);
        toolbar = findViewById(R.id.anime_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        refreshLayout = findViewById(R.id.offline_episodes_refresh);
        offline = findViewById(R.id.episodes_recycler_inside);
        imageView =findViewById(R.id.anime_image);
        nameView = findViewById(R.id.animename);
    }

    void getAnime() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ANIME)){
            GeneralAnime = (MiniAnime) intent.getParcelableExtra(EXTRA_ANIME);
        }
        else throw new NullPointerException();
    }

    String onlineGetTitle(int position) {
        switch (position) {
            case 0:
                return "INFO";
            case 1:
                return "EPISODES";
            default:
                return null;
        }
    }

    public MiniAnime getGeneralAnime() {
        return GeneralAnime;
    }

    public void setGeneralAnime(MiniAnime generalAnime) {
        GeneralAnime = generalAnime;
    }
}
