package com.eme22.kumaanime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.kumaanime.AnimeActivity_fragments.adapters.Anime_Page_Adapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.AppUtils.Theming;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class GeneralAnimeActivity extends AppCompatActivity {

    public static final String EXTRA_ANIME = "EXTRA ANIME";

    TabLayoutMediator tablm;
    TabLayout tabl;
    ViewPager2 viewp;
    ImageView imagesrc;
    Anime_Page_Adapter pagea;
    TextView title;
    Theming theme;
    private MiniAnime GeneralAnime;
    private int viewed = -1;

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
        setContentView(R.layout.activity_general_anime);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ANIME)){
            GeneralAnime = (MiniAnime) intent.getSerializableExtra(EXTRA_ANIME);
        }
        else throw new NullPointerException();
    }

    void initView(){
        Toolbar toolbar = findViewById(R.id.anime_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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