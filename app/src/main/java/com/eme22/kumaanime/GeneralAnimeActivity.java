package com.eme22.kumaanime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.EpisodeFetcherOffline;
import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager;
import com.eme22.kumaanime.AnimeActivity_fragments.adapters.Anime_Page_Adapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.AppUtils.OtherUtils;
import com.eme22.kumaanime.AppUtils.Theming;
import com.eme22.kumaanime.MainActivity_fragments.adapters.EpisodeAdapterOffline;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class GeneralAnimeActivity extends PermissionActivity {

    public static final String EXTRA_ANIME = "EXTRA ANIME";
    public static final String EXTRA_ACTION = "EXTRA ACTION";

    TabLayoutMediator tablm;
    TabLayout tabl;
    ViewPager2 viewp;
    ImageView imagesrc;
    Anime_Page_Adapter pagea;
    TextView title;
    Theming theme;
    private MiniAnime GeneralAnime;
    private int viewed = -1;
    private final TaskRunner taskRunner = new TaskRunner();
    DownloadManager downloadManager;
    private boolean isContextualEnabled = false;
    private Toolbar toolbar;
    private ArrayList<MiniEpisodeOffline> deleteList = new ArrayList<>();
    private int contextualcounter = 0;
    private SwipeRefreshLayout refreshLayout;
    private EpisodeAdapterOffline adapter;
    private RecyclerView episodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

    }

    private String getTitle(int position) {
        switch (position) {
            case 0:
                return "INFO";
            case 1:
                return "EPISODES";
            default:
                return null;
        }
    }

    void init(){
        downloadManager = DownloadManager.getInstance(this);
        theme = new Theming(this);
        theme.themecheck();
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ANIME)){
            GeneralAnime = (MiniAnime) intent.getSerializableExtra(EXTRA_ANIME);
        }
        else throw new NullPointerException();


        if (intent.hasExtra(EXTRA_ACTION)){
            setContentView(R.layout.activity_general_anime_offline);
            initViewOffline();
        }
        else {
            setContentView(R.layout.activity_general_anime);
            initView();
        }
    }

    private void initViewOffline() {
        toolbar = findViewById(R.id.anime_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        imagesrc = findViewById(R.id.anime_image);
        title = findViewById(R.id.animename);
        title.setText(GeneralAnime.getTitle());
        imagesrc = findViewById(R.id.anime_image);
        title = findViewById(R.id.animename);
        title.setText(GeneralAnime.getTitle());
        refreshLayout = findViewById(R.id.offline_episodes_refresh);

        DownloadManager downloadManager = DownloadManager.getInstance(this);

        try {

            File image = downloadManager.getImage(GeneralAnime);

            Log.d("IMAGE", image.getPath());

            ImageUtils.getSharedInstance().load(image).placeholder(new CircularProgressDrawable(this)).error(R.drawable.no_preview_2).into(imagesrc, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d("IMAGE UTIL", "Success");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
            //ImageUtils.getSharedInstance().load(imageuri).placeholder(new CircularProgressDrawable(imagecontext)).error(R.drawable.no_preview_2).into(viewHolder.preview);
        }
        catch (NullPointerException e){
            Log.d("No image: ", GeneralAnime.getTitle()+" "+ GeneralAnime.getLink());
            ImageUtils.getSharedInstance().load(R.drawable.no_preview_2).into(imagesrc);
        }


        episodes = findViewById(R.id.episodes_recycler_inside);
        //ProgressBar progressBar = findViewById(R.id.episodes_progress_bar);
        createAdapter();
        episodes.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        episodes.setAdapter(adapter);

        //ImageUtils.getSharedInstance().load(DownloadManager.getInstance(this).getImage(GeneralAnime)).placeholder(new CircularProgressDrawable(this)).error(R.drawable.no_preview_2).into(imagesrc);

        //refreshLayout.setOnRefreshListener(() -> loadepisodes(adapter));

        loadepisodes();

        refreshLayout.setOnRefreshListener(this::refreshview);

    }

    private void loadepisodes() {
        taskRunner.executeAsync(new EpisodeFetcherOffline(this,GeneralAnime, new EpisodeFetcherOffline.EpisodeFetcherOfflineCallback() {
            @Override
            public void onSuccess(ArrayList<MiniEpisodeOffline> o) {
                runOnUiThread(() -> {
                    //adapter.setViewed(getViewed());
                    adapter.clear();
                    adapter.addAll(o);
                    if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                    Log.d("RECYCLER DATA:", String.valueOf(adapter.getItemCount()));
                });

            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> OtherUtils.toast(R.string.episodes_error));

            }
        }));

    }

    void initView(){
        toolbar = findViewById(R.id.anime_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        imagesrc = findViewById(R.id.anime_image);
        pagea = new Anime_Page_Adapter(this);
        tabl = findViewById(R.id.anime_window_tab);
        viewp = findViewById(R.id.anime_viewpager);
        title = findViewById(R.id.animename);
        viewp.setAdapter(pagea);
        viewp.setCurrentItem(0, true);
        tablm = new TabLayoutMediator(
                tabl,
                viewp,
                (tab, position) -> tab.setText(getTitle(position))
        );
        tablm.attach();
        title.setText(GeneralAnime.getTitle());
        ImageUtils.getSharedInstance().load(GeneralAnime.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(this)).error(R.drawable.no_preview_2).into(imagesrc);
        //Glide.with(this).load(ggwp.getMainPicture().getMedium()).into(imagesrc);
    }

    public void makeSelection(MiniEpisodeOffline item) {
        if (!isContextualEnabled) {
            isContextualEnabled = true;
            //toolbar.findViewById(R.id.anime_search).setVisibility(View.GONE);
            //toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(v -> refreshview());
            invalidateOptionsMenu();
        }

        if (!deleteList.contains(item)){
            deleteList.add(item);
            contextualcounter++;
        }
        else {
            deleteList.remove(item);
            contextualcounter--;
        }
        toolbar.setTitle(contextualcounter+" Episodio(s) seleccionado(s)");
    }

    private void refreshview() {

        if (isContextualEnabled) {
            new Handler(Looper.getMainLooper()).post(() -> {
                //toolbar.findViewById(R.id.anime_search).setVisibility(View.VISIBLE);
                toolbar.setTitle(R.string.app_name);
                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            });
            isContextualEnabled = false;
            contextualcounter = 0;
            deleteList.clear();
            new Handler(Looper.getMainLooper()).post(() -> {
                toolbar.getMenu().clear();
                adapter = null;
                createAdapter();
                episodes.setAdapter(adapter);
            });
        }
        loadepisodes();
    }

    @Override
    public void onBackPressed() {
        if (isContextualEnabled) refreshview();
        else super.onBackPressed();


    }

    private void createAdapter(){
        adapter = new EpisodeAdapterOffline(this, downloadManager, new EpisodeAdapterOffline.OnItemClicked() {
            @Override
            public void onPlayClick(MiniEpisodeOffline anime) {

                if (!isContextualEnabled){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(downloadManager.getFileUri(anime.getEpisode_file()), "video/mp4");
                    try {
                        startActivity(intent);
                    }
                    catch (Exception ignore){}
                }
            }

            @Override
            public void onForceStatusUpdate(MiniEpisodeOffline anime) {

            }

            @Override
            public void onCastClick(MiniEpisodeOffline anime) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isContextualEnabled) {
            menu.clear();
            getMenuInflater().inflate(R.menu.selection_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_anime){

            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        taskRunner.executeAsync(new deleteEpisodes());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.dismiss();
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.aceptar, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener).show();


            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public MiniAnime getGeneralAnime() {
        return GeneralAnime;
    }

    public void  setAnimeLinks(String links) { this.GeneralAnime.setLink(links); }


    private class deleteEpisodes implements Runnable {
        @Override
        public void run() {

            for (MiniEpisodeOffline anime: deleteList) {
                Log.d("EPISODE TO DELETE", anime.getEpisode_file().getPath());
            }


            for (MiniEpisodeOffline anime: deleteList) {
                //if (anime.getAnimeID()<=0) continue;

                File image = anime.getImage();
                File file = anime.getEpisode_file();

                Log.d("IMAGE TO DELETE:", image.getPath());
                Log.d("VIDEO TO DELETE:", file.getPath());

                try {
                    if (image.delete()) Log.d("IMAGE DELETED:", image.getPath());
                    else Log.e("ERROR DELETING", String.valueOf(anime.getAnimeID()));

                    if (file.delete()) Log.d("FILE DELETED:", file.getPath());
                    else Log.e("ERROR DELETING VID", String.valueOf(anime.getAnimeID()));

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            refreshview();
        }
    }
}


/*
public class GeneralAnimeActivity extends PermissionActivity {

    public static final String EXTRA_ANIME = "EXTRA_ANIME";
    public static final String EXTRA_ACTION = "EXTRA_ACTION";

    TabLayoutMediator tablm;
    TabLayout tabl;
    ViewPager2 viewp;
    ImageView imagesrc;
    Anime_Page_Adapter pagea;
    TextView title;
    DownloadManager downloadManager;
    Theming theme;
    private MiniAnime GeneralAnime;
    private int viewed = -1;
    private final TaskRunner taskRunner = new TaskRunner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initView();

    }

    private String getTitle(int position) {
        switch (position) {
            case 0:
                return "INFO";
            case 1:
                return "EPISODES";
            default:
                return null;
        }
    }

    void init(){
        theme = new Theming(this);
        theme.themecheck();
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ANIME)){
            GeneralAnime = (MiniAnime) intent.getSerializableExtra(EXTRA_ANIME);
        }
        else throw new NullPointerException();
        downloadManager = new DownloadManager(this);
        downloadManager.init();
    }

    void initView(){
        if ( getIntent().getBooleanExtra(EXTRA_ACTION,false)) initViewOffline();
        else initViewNormal();
    }

    private void initCommonItems() {
        Toolbar toolbar = findViewById(R.id.anime_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initViewOffline() {
        setContentView(R.layout.activity_general_anime_offline);
        initCommonItems();
        imagesrc = findViewById(R.id.anime_image);
        title = findViewById(R.id.animename);
        title.setText(GeneralAnime.getTitle());

        try {

            File image = downloadManager.getImage(GeneralAnime);

            Log.d("IMAGE", image.getPath());

            ImageUtils.getSharedInstance().load(image).placeholder(new CircularProgressDrawable(this)).error(R.drawable.no_preview_2).into(imagesrc, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d("IMAGE UTIL", "Success");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
            //ImageUtils.getSharedInstance().load(imageuri).placeholder(new CircularProgressDrawable(imagecontext)).error(R.drawable.no_preview_2).into(viewHolder.preview);
        }
        catch (NullPointerException e){
            Log.d("No image: ", GeneralAnime.getTitle()+" "+ GeneralAnime.getLink());
            ImageUtils.getSharedInstance().load(R.drawable.no_preview_2).into(imagesrc);
        }


        RecyclerView episodes = findViewById(R.id.episodes_recycler_inside);
        ProgressBar progressBar = findViewById(R.id.episodes_progress_bar);
        EpisodeAdapterOffline adapter = new EpisodeAdapterOffline(new EpisodeAdapterOffline.OnItemClicked() {
            @Override
            public void onPlayClick(MiniEpisodeOffline anime) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(downloadManager.getFileUri(anime.getEpisode_file()), "video/mp4");
                try {
                    startActivity(intent);
                }
                catch (Exception ignore){}



            }

            @Override
            public void onForceStatusUpdate(MiniEpisodeOffline anime) {

            }

            @Override
            public void onCastClick(MiniEpisodeOffline anime) {

            }
        });
        episodes.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        episodes.setAdapter(adapter);

        //ImageUtils.getSharedInstance().load(DownloadManager.getInstance(this).getImage(GeneralAnime)).placeholder(new CircularProgressDrawable(this)).error(R.drawable.no_preview_2).into(imagesrc);

        loadepisodes(progressBar,adapter);
    }

    void initViewNormal(){
        /*
        setContentView(R.layout.activity_general_anime);
        Toolbar toolbar = findViewById(R.id.anime_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        initCommonItems();
        imagesrc = findViewById(R.id.anime_image);
        pagea = new Anime_Page_Adapter(this);
        tabl = findViewById(R.id.anime_window_tab);
        viewp = findViewById(R.id.anime_viewpager);
        title = findViewById(R.id.animename);
        viewp.setAdapter(pagea);
        viewp.setCurrentItem(0, true);
        tablm = new TabLayoutMediator(
                tabl,
                viewp,
                (tab, position) -> tab.setText(getTitle(position))
        );
        tablm.attach();
        title.setText(GeneralAnime.getTitle());
        ImageUtils.getSharedInstance().load(GeneralAnime.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(this)).error(R.drawable.no_preview_2).into(imagesrc);
        //Glide.with(this).load(ggwp.getMainPicture().getMedium()).into(imagesrc);
    }

    private void loadepisodes(ProgressBar loading, EpisodeAdapterOffline adapter) {
        taskRunner.executeAsync(new EpisodeFetcherOffline(this,GeneralAnime, new EpisodeFetcherOffline.EpisodeFetcherOfflineCallback() {
            @Override
            public void onSuccess(ArrayList<MiniEpisodeOffline> o) {
                runOnUiThread(() -> {
                    //adapter.setViewed(getViewed());
                    adapter.addAll((ArrayList<MiniEpisodeOffline>) o);
                    Log.d("RECYCLER DATA:", String.valueOf(adapter.getItemCount()));
                    loading.setVisibility(View.GONE);
                });

            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> {
                    loading.setVisibility(View.GONE);
                    OtherUtils.toast(R.string.episodes_error);
                });

            }
        }));
    }


    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public MiniAnime getGeneralAnime() {
        return GeneralAnime;
    }

    public void  setAnimeLinks(String links) { this.GeneralAnime.setLink(links); }

}
*/
