package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_ranking.Datum;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.R;

import java.util.ArrayList;
import java.util.List;

public class TopAnimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Datum> ranking;
    private final OnItemClicked listener;
    private boolean isLoadingAdded = false;

    public TopAnimeAdapter(OnItemClicked listener) {
        this.listener = listener;
        this.ranking = new ArrayList<>();
    }

    public void add(Datum mc) {
        ranking.add(mc);
        notifyItemInserted(ranking.size() - 1);
    }

    public void addAll(List<Datum> mcList) {
        for (Datum mc: mcList) {
            add(mc);
        }
    }

    public void remove(Datum city) {
        int position = ranking.indexOf(city);
        if (position > -1) {
            ranking.remove(position);
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
        add(new Datum());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = ranking.size() - 1;
        Datum item = getItem(position);
        if (item != null) {
            ranking.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Datum getItem(int position) {
        return ranking.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_item_top, parent, false));
        else return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView();
        }

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        Datum item = ranking.get(position);
        viewHolder.ranking2.setText(String.valueOf(item.getRanking().getRank()));
        viewHolder.title.setText(item.getNode().getTitle());
        //Glide.with(viewHolder.preview.getContext()).load(item.getNode().getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(viewHolder.preview.getContext())).error(R.drawable.no_preview_2).into(viewHolder.preview);
        ImageUtils.getSharedInstance().load(item.getNode().getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(viewHolder.preview.getContext())).error(R.drawable.no_preview_2).into(viewHolder.preview);
        /*
        try {
            Picasso.get().load(item.getNode().getMainPicture().getMedium()).into(viewHolder.preview);
        }
        catch (NullPointerException ignored){}
        */


    }

    private void showLoadingView() {
    }

    @Override
    public int getItemCount() {
        return ranking == null ? 0 : ranking.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == ranking.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView preview;
        public TextView title;
        public TextView ranking2;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.top_anime_item_preview);
            title = itemView.findViewById(R.id.top_anime_item_title);
            ranking2 = itemView.findViewById(R.id.top_anime_item_ranking);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(ranking.get(position).getNode());
                }
            });
        }
    }

    public interface OnItemClicked {
        void onItemClick(MiniAnime anime);
    }
}
