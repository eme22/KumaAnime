package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.AppUtils.NewsObject.Datum;
import com.eme22.kumaanime.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class OfflineAnimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final OnItemClicked listener;
    private List<MiniAnime> animes;

    File location;

    private boolean isLoadingAdded = false;

    public OfflineAnimeAdapter(OnItemClicked listener) {
        this.listener = listener;
        animes = new ArrayList<>();
        if (DownloadManager.getInstance() != null) {
            location = DownloadManager.getInstance().getMAIN_DIR();
        }
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){

            case 1: return new AnimeViewHolder(inflater.inflate(R.layout.anime_item, parent, false));
            default:
            case 0:  return new LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        MiniAnime anime = animes.get(position);

        if (holder instanceof AnimeViewHolder) {
            ((AnimeViewHolder) holder).title.setText(anime.getTitle());


            Context imagecontext = ((AnimeViewHolder) holder).preview.getContext();
            File image = new File(location, String.valueOf(anime.getId()));
            Uri imageuri = FileProvider.getUriForFile(imagecontext, imagecontext.getApplicationContext().getPackageName() + ".provider", image);

            try {
                ImageUtils.getSharedInstance().load(imageuri).placeholder(new CircularProgressDrawable(imagecontext)).error(R.drawable.no_preview_2).into(((AnimeViewHolder) holder).preview);
            } catch (Exception e) {
                try {
                    if (ImageUtils.DownloadImage(anime.getMainPicture().getMedium(),image)){
                        ImageUtils.getSharedInstance().load(imageuri).placeholder(new CircularProgressDrawable(imagecontext)).error(R.drawable.no_preview_2).into(((AnimeViewHolder) holder).preview);
                    }
                    else ImageUtils.getSharedInstance().load(R.drawable.no_preview_2).into(((AnimeViewHolder) holder).preview);
                } catch (IOException e2) {
                    e2.printStackTrace();
                    ImageUtils.getSharedInstance().load(R.drawable.no_preview_2).into(((AnimeViewHolder) holder).preview);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return animes == null ? 0 : animes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return animes.get(position).getId()>0 ? 1 : 0;
    }

    public void add(MiniAnime mc) {
        animes.add(mc);
        notifyItemInserted(animes.size() - 1);
    }

    public void addAll(List<MiniAnime> mcList) {
        for (MiniAnime mc : mcList) {
            add(mc);
        }
    }

    public void remove(MiniAnime city) {
        int position = animes.indexOf(city);
        if (position > -1) {
            animes.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        MiniAnime dummy = new MiniAnime();
        dummy.setId(0);
        add(dummy);

    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = animes.size() - 1;
        MiniAnime item = getItem(position);

        if (item != null) {
            animes.remove(position);
            notifyItemRemoved(position);
        }
    }

    public MiniAnime getItem(int position) {
        return animes.get(position);
    }

    private class AnimeViewHolder extends RecyclerView.ViewHolder {

        public ImageView preview;
        public TextView title;

        public AnimeViewHolder(View inflate) {
            super(inflate);
            preview = itemView.findViewById(R.id.image_view_anime);
            title = itemView.findViewById(R.id.ep_title);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(animes.get(position));
                }
            });
        }
    }

    protected static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClicked {
        void onItemClick(MiniAnime anime);
    }

}
