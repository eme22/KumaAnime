package com.eme22.kumaanime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.EpisodeFetcherOffline;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.AppUtils.OtherUtils;
import com.eme22.kumaanime.MainActivity_fragments.adapters.EpisodeAdapterOffline;
import com.squareup.picasso.Callback;

import java.io.File;
import java.util.ArrayList;

public class AnimeActivityOffline extends GeneralAnimeActivity_v2{

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        getAnime();
        initViewOffline();
        initData();
    }

    private void initData() {

        initAdapter();

        File image = downloadManager.getImage(GeneralAnime);

        Log.d("IMAGE", image.getPath());

        ImageUtils.getSharedInstance().load(image).placeholder(new CircularProgressDrawable(this)).error(R.drawable.no_preview_2).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d("IMAGE UTIL", "Success");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
        });

        nameView.setText(GeneralAnime.getTitle());

        offline.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        offline.setAdapter(offlineRecycler);
        loadepisodes();
        refreshLayout.setOnRefreshListener(this::refreshview);

    }

    private void initAdapter() {
        offlineRecycler = new EpisodeAdapterOffline(this, downloadManager, new EpisodeAdapterOffline.OnItemClicked() {
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


    private void loadepisodes() {
        taskRunner.executeAsync(new EpisodeFetcherOffline(this,GeneralAnime, new EpisodeFetcherOffline.EpisodeFetcherOfflineCallback() {
            @Override
            public void onSuccess(ArrayList<MiniEpisodeOffline> o) {
                runOnUiThread(() -> {
                    offlineRecycler.clear();
                    offlineRecycler.addAll(o);
                    if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                    Log.d("RECYCLER DATA:", String.valueOf(offlineRecycler.getItemCount()));
                });

            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> OtherUtils.toast(R.string.episodes_error));

            }
        }));
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

    void refreshview() {

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
                offlineRecycler = null;
                initAdapter();
                offline.setAdapter(offlineRecycler);
            });
        }
        loadepisodes();
    }

    @Override
    public void onBackPressed() {
        if (isContextualEnabled) refreshview();
        else super.onBackPressed();
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
                        taskRunner.executeAsync(new AnimeActivityOffline.deleteEpisodes());
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
