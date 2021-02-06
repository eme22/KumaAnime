package com.eme22.kumaanime.AnimeActivity_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eme22.kumaanime.AnimeActivity;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details.AnimeDetails;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details.MyListStatus;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_update.AnimeUpdate;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.AppUtils.AnimeObjects.GeneralAnime;
import com.eme22.kumaanime.AppUtils.Mods.OnItemClickSpinner;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.R;
import com.tingyik90.prefmanager.PrefManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Response;

public class AnimeInfo_v2 extends Fragment implements AnimeActivity.AnimeActivityLoad {

    private MiniAnime anime;
    private final TaskRunner task = new TaskRunner();
    private OnItemClickSpinner status;
    private OnItemClickSpinner punctuation;
    private ArrayAdapter<String> a;
    private ArrayAdapter<String> b;
    private ConstraintLayout anime_info_log1;
    private ConstraintLayout anime_info_log2;
    private Button more_log;
    private Button less_log;
    private TextView type;
    private TextView desc;
    private TextView duracion;
    private TextView seen_eps;
    private TextView eps;
    private TextView day;
    private TextView year;
    private TextView parental;
    private RecyclerView studio;
    private RatingBar bar;
    private RecyclerView genero;
    private RecyclerView more_images;
    private RecyclerView anime_related;
    private SwitchCompat mal_desc;
    private SwitchCompat rewatching;
    private CardView apply_changes;
    private SwipeRefreshLayout swipe_general;
    private ScrollView sv;
    private PrefManager prefs;
    private MyAnimeListAPIService api;
    private int animeID = 0;
    private static final String nullinfo = "Este anime aún no esta en La base de Datos de MAL.\nPuede ser que aún no haya salido aún.\n";


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new PrefManager(requireContext());
        if (prefs.getBoolean("isLogged",false)){
            api = MyAnimeListAPIAdapter.getApiServiceWithAuth(requireContext());
        }
        else api = null;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_anime_info, container, false);

        initView(v);
        init();
        if (((AnimeActivity) requireActivity()).isLoaded()) initData();
        return v;
    }

    private void initListeners() {
        status.setOnItemClickListener((parent, view, position, id) -> statuslivechange(position));
        punctuation.setOnItemClickListener((parent, view, position, id) -> punctuationLiveChange(position));
        rewatching.setOnCheckedChangeListener((buttonView, isChecked) -> watchingLiveChange(isChecked));
        //rewatching.setOnClickListener(v -> rewatchinglivechange(rewatching.isChecked()));
    }

    private void punctuationLiveChange(int position) {
        Log.d("POSITION", String.valueOf(position));
        Log.d("ANIME ID", String.valueOf(animeID));
        api.updatemyanime(animeID,null,null, 10-position,null,null,null,null,null,null).enqueue(new retrofit2.Callback<AnimeUpdate>() {
            @Override
            public void onResponse(@NotNull Call<AnimeUpdate> call, @NotNull Response<AnimeUpdate> response) {
                Log.d("PUNCTUATION CHANGE", String.valueOf(response.code()));
                if (response.body() != null) {
                    Log.d("PUNCTUATION CHANGE", response.body().toString());
                }
            }

            @Override
            public void onFailure(@NotNull Call<AnimeUpdate> call, @NotNull Throwable t) {
                Log.d("STATUS CHANGE", String.valueOf(t.getMessage()));
            }
        });
    }

    private void watchingLiveChange(boolean checked) {
        if (checked) api.updatemyanime(animeID,null,checked, null,null,null,null,null,null,null).enqueue(new retrofit2.Callback<AnimeUpdate>() {
            @Override
            public void onResponse(@NotNull Call<AnimeUpdate> call, @NotNull Response<AnimeUpdate> response) {

            }

            @Override
            public void onFailure(@NotNull Call<AnimeUpdate> call, @NotNull Throwable t) {

            }
        });
    }

    private void statuslivechange(int position) {
        Log.d("POSITION", String.valueOf(position));
        Log.d("ANIME ID", String.valueOf(animeID));

        String detailsStatus = status.getSelectedItem().toString();

        switch (detailsStatus){
            default: return;
            case "Viendo": detailsStatus = "watching";break;
            case "Completado": detailsStatus = "completed";break;
            case "En Espera": detailsStatus = "on_hold";break;
            case "Dropeados": detailsStatus = "dropped";break;
            case "Planeando Ver": detailsStatus = "plan_to_watch";break;
        }
        api.updatemyanime(animeID,detailsStatus,null, null,null,null,null,null,null,null).enqueue(new retrofit2.Callback<AnimeUpdate>() {
            @Override
            public void onResponse(@NotNull Call<AnimeUpdate> call, @NotNull Response<AnimeUpdate> response) {
                Log.d("STATUS CHANGE", String.valueOf(response.code()));
                if (response.body() != null) {
                    Log.d("STATUS CHANGE", response.body().toString());
                }
            }

            @Override
            public void onFailure(@NotNull Call<AnimeUpdate> call, @NotNull Throwable t) {
                Log.d("STATUS CHANGE", String.valueOf(t.getMessage()));
            }
        });


    }

    void initView(View v){
        //INITIALIZERS
        status = v.findViewById(R.id.anime_info_status_selection);
        punctuation = v.findViewById(R.id.anime_info_punctuation_selection);
        a = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.anime_states));
        b = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.anime_rates));
        anime_info_log1 = v.findViewById(R.id.anime_info_mal_constrain1);
        anime_info_log2 = v.findViewById(R.id.anime_info_mal_constrain2);
        more_log = v.findViewById(R.id.anime_info_more_button);
        less_log = v.findViewById(R.id.anime_info_less_button);
        type = v.findViewById(R.id.anime_info_type);
        desc = v.findViewById(R.id.anime_info_desc);
        duracion = v.findViewById(R.id.anime_info_duracion);
        seen_eps = v.findViewById(R.id.anime_info_watched_episodes_num);
        eps = v.findViewById(R.id.anime_info_episodes_num);
        day = v.findViewById(R.id.anime_info_day);
        year = v.findViewById(R.id.anime_info_year);
        parental = v.findViewById(R.id.anime_info_rate);
        studio = v.findViewById(R.id.anime_info_studios);
        bar = v.findViewById(R.id.anime_info_rating_bar);
        genero = v.findViewById(R.id.anime_info_genero_recycler);
        more_images = v.findViewById(R.id.anime_info_images);
        anime_related = v.findViewById(R.id.anime_info_related_anime);
        mal_desc = v.findViewById(R.id.anime_info_use_mal_spinner);
        rewatching = v.findViewById(R.id.anime_info_rewatching_switch);
        apply_changes = v.findViewById(R.id.apply_changes_info);
        swipe_general = v.findViewById(R.id.general_info_layout);
        sv = v.findViewById(R.id.anime_info_nested_scrollview);
        sv.setVisibility(View.GONE);
    }

    void init(){

        ((AnimeActivity) requireActivity()).setAnimeinfo(this);

        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(a);
        punctuation.setAdapter(b);
        anime_info_log2.setVisibility(View.GONE);
        apply_changes.setVisibility(View.GONE);
        //swipe_general.setVisibility(View.GONE);
        swipe_general.setRefreshing(true);
    }

    void initData(){

        if(prefs.getBoolean("isLogged",false)) {

            GeneralAnime result = ((AnimeActivity) requireActivity()).getResult();
            AnimeDetails details = result.getDetails();
            String desc2 = result.getDesc_2();
            animeID = details.getId();
            if (details.getId() == -1)
                requireActivity().runOnUiThread(() -> UpdateUiForNullAnime(desc2));
            else requireActivity().runOnUiThread(() -> updateUI(details, desc2));

            sv.setVisibility(View.VISIBLE);

            more_log.setOnClickListener(view -> {
                anime_info_log2.setVisibility(View.VISIBLE);
                more_log.setVisibility(View.GONE);
                sv.post(() -> sv.fullScroll(ScrollView.FOCUS_DOWN));
            });
            less_log.setOnClickListener(view -> {
                anime_info_log2.setVisibility(View.GONE);
                more_log.setVisibility(View.VISIBLE);
            });

            initListeners();

        }
         else {
            more_log.setVisibility(View.GONE);
            anime_info_log1.setVisibility(View.GONE);
            anime_info_log2.setVisibility(View.GONE);
        }

    }

    void updateUI(AnimeDetails details, String desc2){
        int episode;
        try {
            episode = details.getMyListStatus().getNumEpisodesWatched();
        }
        catch (NullPointerException e){
            episode = 0;
        }


        Log.d("EPISODE SEEN:" , String.valueOf(episode));
        try {
            type.setText(details.getMediaType());
        }
        catch (NullPointerException ignored){}
        mal_desc.setChecked(true);
        try {
            desc.setText(details.getSynopsis());
        }
        catch (NullPointerException e){
            desc.setText(R.string.none_avaible);
        }

        mal_desc.setOnCheckedChangeListener((compoundButton, b1) -> {
            if (b1) desc.setText(details.getSynopsis());
            else desc.setText(desc2);
        });
        try {
            String text = details.getAverageEpisodeDuration()/60+" minutos";
            duracion.setText(text);
        }
        catch (NullPointerException e){
            duracion.setText(R.string.none_avaible);
        }

        MyListStatus status_2 = details.getMyListStatus();


        String detailsStatus;
        try {
            detailsStatus  = status_2.getStatus();
        }
        catch (NullPointerException e){
            detailsStatus = "Ninguno";
        }

        switch (detailsStatus){
            case "watching": detailsStatus = "Viendo";break;
            case "completed": detailsStatus = "Completado";break;
            case "on_hold": detailsStatus = "En Espera";break;
            case "dropped": detailsStatus = "Dropeados";break;
            case "plan_to_watch": detailsStatus = "Planeando Ver";break;
        }
        status.setSelection(a.getPosition(detailsStatus));

        try {
            punctuation.setSelection(10-status_2.getScore());
        }
        catch (NullPointerException ignored){
            punctuation.setSelection(10);
        }


        try {
            seen_eps.setText(String.valueOf(status_2.getNumEpisodesWatched()));
        }
        catch (NullPointerException e){
            seen_eps.setText("0");
        }

        try {
            if(details.getNumEpisodes()>0)
                eps.setText(String.valueOf(details.getNumEpisodes()));
        }
        catch (NullPointerException ignored){}
        try{
            bar.setRating(details.getMean().floatValue()/2);
        }catch (NullPointerException ignored){
            bar.setRating(0f);
        }

        try {
            day.setText(details.getBroadcast().getDayOfTheWeek());
        }
        catch (NullPointerException ignored){}
        try {
            year.setText(String.valueOf(details.getStartSeason().getYear()));
        }
        catch (NullPointerException ignored){
            year.setText(R.string.no_avaible);
        }

        if (details.getRating()!=null)
            parental.setText(details.getRating());
        swipe_general.setVisibility(View.VISIBLE);
        swipe_general.setRefreshing(false);
    }

    void UpdateUiForNullAnime(String desc2) {
        Log.d("Animeinfo: ", "NULLANIME");
        mal_desc.setVisibility(View.GONE);
        more_log.setVisibility(View.GONE);
        anime_info_log1.setVisibility(View.GONE);
        anime_info_log2.setVisibility(View.GONE);
        desc.setText(getString(R.string.description_not_found, nullinfo, desc2));
        swipe_general.setVisibility(View.VISIBLE);
        swipe_general.setRefreshing(false);
    }

    @Override
    public void loadData() {
        initData();
    }
}
