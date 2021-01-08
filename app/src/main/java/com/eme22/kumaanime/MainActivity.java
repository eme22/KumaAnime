package com.eme22.kumaanime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.OtherUtils;
import com.eme22.kumaanime.AppUtils.Theming;
import com.eme22.kumaanime.Databases.MainTable.MinAnimeTable_ViewModelCursor;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_ViewModelCursorFactory;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_ViewModel_v2;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_ViewModel_v3;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_ViewModel_v3_Factory;
import com.eme22.kumaanime.MainActivity_fragments.adapters.AnimeCursorAdapter;
import com.eme22.kumaanime.MainActivity_fragments.adapters.MainPageAdapter;
import com.eme22.kumaanime.MainActivity_fragments.anime_list_fragments.anime_directory_fragments.AnimeDirectoryAnimeList;
import com.eme22.kumaanime.MainActivity_fragments.helpers.SettingsFragment;
import com.eme22.kumaanime.MainActivity_fragments.util.AnimeManagerBroadcastReciever;
import com.eme22.kumaanime.Services.DatabaseFiller;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tingyik90.prefmanager.PrefManager;

import java.lang.reflect.Field;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    SearchView search;
    Theming theme;
    Toolbar toolbar;
    TabLayout tabl;
    TabLayoutMediator tablm;
    ViewPager2 viewp;
    MainPageAdapter pagea;
    ConstraintLayout internalmssg;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    PrefManager prefManager;
    Intent alarmIntent;
    private boolean isFirstBackPressed = false;
    private int searchss = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            initdatabase();

            init();

            initViews();

    }

    private void initdatabase() {
        Intent intent = getIntent();
        if ( intent.getBooleanExtra("FIRST", true)){
            intent = new Intent(this, DatabaseFiller.class);
            startService(intent);
        }
    }

    private void slopfix() {
        try {
            Field recyclerViewField = ViewPager2.class.getDeclaredField("mRecyclerView");
            recyclerViewField.setAccessible(true);

            RecyclerView recyclerView = (RecyclerView) recyclerViewField.get(viewp);

            Field touchSlopField = RecyclerView.class.getDeclaredField("mTouchSlop");
            touchSlopField.setAccessible(true);
            Object touchSlop2 = touchSlopField.get(recyclerView);
            assert touchSlop2 != null;
            int touchSlop = (int) touchSlop2;
            touchSlopField.set(recyclerView, touchSlop * 6);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        theme = new Theming(this);
        theme.themecheck();

        prefManager = new PrefManager(this);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmIntent = new Intent(this, AnimeManagerBroadcastReciever.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_menu){
            Fragment fragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getTitle(int position) {
        switch (position) {
            case 0:
                return getString(R.string.anime_offline_titleui);
            case 1:
                return getString(R.string.animes_titleui);
            case 2:
                return getString(R.string.new_animes_titleui);
            case 3:
                return getString(R.string.my_animelist_titleui);
            case 4:
                return getString(R.string.anime_news_titleui);
            default:
                return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews(){
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_toolbar);
        search = findViewById(R.id.anime_search);
        search.setOnQueryTextListener(query);
        search.setOnCloseListener(close);
        search.setSuggestionsAdapter(new AnimeCursorAdapter(this,null, 0));
        ((AutoCompleteTextView) search.findViewById(R.id.search_src_text)).setThreshold(1);
        internalmssg = findViewById(R.id.messages_constrain);
        tabl = findViewById(R.id.general_tab);
        viewp = findViewById(R.id.general_view);
        pagea = new MainPageAdapter(this);

        setSupportActionBar(toolbar);
        internalmssg.setVisibility(View.GONE);
        viewp.setAdapter(pagea);
        viewp.setCurrentItem(2, false);
        viewp.setOffscreenPageLimit(4);

        tablm = new TabLayoutMediator(
                tabl,
                viewp,
                (tab, position) -> tab.setText(getTitle(position))
        );
        tablm.attach();

        slopfix();


    }

    private void startAlarm() {

        int animealarm = Integer.parseInt(prefManager.getString("search_for_animes", "2"));
        switch (animealarm){
            case 0 :break;
            case 1 :alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,AlarmManager.INTERVAL_FIFTEEN_MINUTES,AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingIntent);break;
            case 2 :alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,AlarmManager.INTERVAL_HALF_HOUR,AlarmManager.INTERVAL_HALF_HOUR,pendingIntent);break;
            case 3 :alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,AlarmManager.INTERVAL_HOUR,AlarmManager.INTERVAL_HOUR,pendingIntent);break;
            case 4 :alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,AlarmManager.INTERVAL_HALF_DAY,AlarmManager.INTERVAL_HALF_DAY,pendingIntent);break;
            case 5 :alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,AlarmManager.INTERVAL_DAY,AlarmManager.INTERVAL_DAY,pendingIntent);break;
        }

    }

    private void cancelAlarm() {
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getApplicationContext(), "Alarm Cancelled", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0){
            super.onBackPressed();
        }else{
            if (isFirstBackPressed) {
                super.onBackPressed();
            } else {
                isFirstBackPressed = true;
                OtherUtils.toast(R.string.exit_toast);
                new Handler().postDelayed(() -> isFirstBackPressed = false, 1500);
            }
        }
    }

    SearchView.OnQueryTextListener query = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            getSupportFragmentManager().popBackStack();
            Fragment f = AnimeDirectoryAnimeList.newInstance(query);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(v2 -> {
                toolbar.setNavigationIcon(null);
                getSupportFragmentManager().popBackStack();
                search.setQuery("", false);
                search.setIconified(true);
            });
            getSupportFragmentManager().beginTransaction().add(R.id.main_container_2, f).addToBackStack(null).commit();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.d("Text", String.valueOf(searchss));

            if (newText !=null){
                    getSupportFragmentManager().popBackStack();
                    Fragment f = AnimeDirectoryAnimeList.newInstance(newText);
                    toolbar.setNavigationIcon(R.drawable.ic_back);
                    toolbar.setNavigationOnClickListener(v2 -> {
                        toolbar.setNavigationIcon(null);
                        getSupportFragmentManager().popBackStack();
                        search.setQuery("", false);
                        search.setIconified(true);
                    });
                    getSupportFragmentManager().beginTransaction().add(R.id.main_container_2, f).addToBackStack(null).commit();
            }

            return true;
        }
    };

    private void hideKeyboard() {
        ((InputMethodManager) getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken() ,0);
    }


    SearchView.OnCloseListener close = () -> {
        toolbar.setNavigationIcon(null);
        getSupportFragmentManager().popBackStack();
        return false;
    };
}