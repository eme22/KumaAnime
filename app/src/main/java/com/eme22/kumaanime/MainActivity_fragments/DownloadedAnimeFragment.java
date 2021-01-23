package com.eme22.kumaanime.MainActivity_fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.OfflineFetcher;
import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.FileUtils;
import com.eme22.kumaanime.GeneralAnimeActivity;
import com.eme22.kumaanime.MainActivity;
import com.eme22.kumaanime.MainActivity_fragments.adapters.OfflineAnimeAdapter_v3;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.PermissionActivity;
import com.eme22.kumaanime.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Fragmento de Animes Offline. Extiende {@link Fragment}.
 *
 */
public class DownloadedAnimeFragment extends Fragment {

    TaskRunner taskRunner = new TaskRunner();
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    private OfflineAnimeAdapter_v3 adapter;
    public boolean isContextualEnabled = false;
    private int contextualcounter = 0;

    ArrayList<MiniAnime> deleteList = new ArrayList<>();
    private Toolbar toolbar;
    private DownloadManager manager;

    public DownloadedAnimeFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.anime_list_general, container, false);

        initView(v);
        initDatabase();

        return v;
    }

    private void initView(View v) {
        manager = DownloadManager.getInstance((PermissionActivity) requireActivity());
        recyclerView = v.findViewById(R.id.animelist);
        refreshLayout = v.findViewById(R.id.swipe_refresh_animelist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        toolbar = ((MainActivity) requireActivity()).toolbar;

        adapter = new OfflineAnimeAdapter_v3(this,DownloadManager.getInstance((PermissionActivity) requireActivity()), anime -> {

            if (!isContextualEnabled){
                startOfflineAnime(anime);
            }

        });


        //recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this::refreshview);
    }

    private void startOfflineAnime(MiniAnime anime) {
        Intent intent = new Intent(getActivity(), GeneralAnimeActivity.class);
        intent.putExtra(GeneralAnimeActivity.EXTRA_ANIME, (Parcelable) anime);
        intent.putExtra(GeneralAnimeActivity.EXTRA_ACTION,true);
        startActivity(intent);
    }

    public void refreshview() {

        if (isContextualEnabled){
            new Handler(Looper.getMainLooper()).post(() -> {
                toolbar.findViewById(R.id.anime_search).setVisibility(View.VISIBLE);
                toolbar.setTitle(R.string.app_name);
                toolbar.setNavigationIcon(null);
            });
            isContextualEnabled = false;
            contextualcounter = 0;
            deleteList.clear();
            requireActivity().invalidateOptionsMenu();
        }


        adapter = null;
        adapter = new OfflineAnimeAdapter_v3(this,DownloadManager.getInstance((PermissionActivity) requireActivity()), anime -> {

            if (!isContextualEnabled){
                startOfflineAnime(anime);
            }

        });
        new Handler(Looper.getMainLooper()).post(() -> recyclerView.setAdapter(adapter));

        taskRunner.executeAsync(new OfflineFetcher(requireContext(), manager, new OfflineFetcher.OfflineFetcherCallback() {
            @Override
            public void onSuccess(ArrayList<MiniAnime> animes) {

                for (MiniAnime anime: animes) {
                    Log.d("OFFLINE DATA RESULT", String.valueOf(anime.getId()));
                }


                Log.d("ANIMES OFFLINE", String.valueOf(animes.size()));
                new Handler(Looper.getMainLooper()).post(() -> {
                    /*
                    adapter.clear();
                    adapter.addAll(animes);
                     */
                    adapter.clear();
                    adapter.addAll(animes);
                    //recyclerView.invalidate();
                    refreshLayout.setRefreshing(false);
                });

            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        }));

    }

    private void initDatabase() {
        taskRunner.executeAsync(new OfflineFetcher(requireContext(), manager, new OfflineFetcher.OfflineFetcherCallback() {
            @Override
            public void onSuccess(ArrayList<MiniAnime> animes) {
                new Handler(Looper.getMainLooper()).post(() -> adapter.addAll(animes));//adapter.addAll(animes));
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        if (isContextualEnabled) {
            menu.clear();
            inflater.inflate(R.menu.selection_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_anime){

            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        taskRunner.executeAsync(new deleteAnime());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.dismiss();
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.aceptar, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener).show();


            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void makeSelection(MiniAnime item) {

        if (!isContextualEnabled) {
            isContextualEnabled = true;
            toolbar.findViewById(R.id.anime_search).setVisibility(View.GONE);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(v -> refreshview());
            requireActivity().invalidateOptionsMenu();
        }

        if (!deleteList.contains(item)){
            deleteList.add(item);
            contextualcounter++;
            }
        else {
            deleteList.remove(item);
            contextualcounter--;
        }
        toolbar.setTitle(contextualcounter+" Anime(s) seleccionado(s)");
    }

    private class deleteAnime implements Runnable {


        @Override
        public void run() {

            for (MiniAnime anime: deleteList) {
                Integer id =  anime.getId();
                if (id <=0) continue;
                if (FileUtils.deleteRecursive(manager.getAnime(id))){
                    Log.d("ANIME DELETED", String.valueOf(anime.getId()));
                }
                else Log.e("ANIME NOT DELETED", manager.getAnime(getId()).getPath());
            }

            refreshview();
        }
    }


    public interface IOnBackPressed {
        boolean onBackPressed();
    }
}