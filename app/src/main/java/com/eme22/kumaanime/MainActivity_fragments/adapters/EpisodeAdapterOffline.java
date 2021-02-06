package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AnimeActivityOffline;
import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager_v2;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisodeOffline;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.R;
import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;

public class EpisodeAdapterOffline extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final OnItemClicked listener;
    DownloadManager_v2 downloadManager;
    long lastviewed = -1;
    public List<MiniEpisodeOffline> mItemList;
    AnimeActivityOffline activity;

    public EpisodeAdapterOffline(AnimeActivityOffline activity, DownloadManager_v2 downloadManager, OnItemClicked listener) {
        this.listener = listener;
        this.downloadManager = downloadManager;
        this.activity = activity;
        mItemList = new ArrayList<>();
    }

    public void setViewed(int pos){
        lastviewed = pos;
    }


    public void add(MiniEpisodeOffline mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void addAll(List < MiniEpisodeOffline > mcList) {
        for (MiniEpisodeOffline mc: mcList) {
            add(mc);
        }
    }

    public void remove(MiniEpisodeOffline city) {
        int position = mItemList.indexOf(city);
        if (position > -1) {
            mItemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }



    public MiniEpisodeOffline getItem(int position) {
        return mItemList.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_item_inside, parent, false);
        return new ItemInsideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemInsideViewHolder) {
            populateItemRowsInside((ItemInsideViewHolder) holder, position);
        }
    }

    void populateItemRowsInside(ItemInsideViewHolder holder, int position) {
        MiniEpisodeOffline miniEpisode = mItemList.get(position);
        ImageUtils.getSharedInstance().load(miniEpisode.getImage()).placeholder(new CircularProgressDrawable(holder.preview.getContext())).resize(0,100).error(R.drawable.no_preview_2).into(holder.preview, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        holder.title.setText(holder.itemView.getContext().getString(R.string.episode,miniEpisode.getEpisode()));
        if (position<=lastviewed) holder.seen.setVisibility(View.VISIBLE);
        else holder.seen.setVisibility(View.GONE);

        holder.itemOptions.setOnClickListener(view -> {

            //creating a popup menu
            PopupMenu popup = new PopupMenu(holder.itemOptions.getContext(), holder.itemOptions);
            //inflating menu from xml resource
            popup.inflate(R.menu.episode_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.episode_status:
                            listener.onForceStatusUpdate(miniEpisode);
                            return true;
                        case R.id.episode_cast:
                            listener.onCastClick(miniEpisode);
                            return true;
                        default:
                            return false;
                    }
                }
            });
            //displaying the popup
            popup.show();

        });

    }


    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class ItemInsideViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        public ImageView preview;
        public TextView title;
        private ConstraintLayout itemOptions;
        public LinearLayout linearLayout;
        int longclickCount;

        public LinearLayout seen;

        public ItemInsideViewHolder(@NonNull View itemView) {
            super(itemView);
            initInside(itemView);
        }

        private void initInside(View v){
            longclickCount = 0;
            linearLayout = v.findViewById(R.id.episode_iniside_select_indicator);
            preview = v.findViewById(R.id.episode_image_inside);
            itemOptions = v.findViewById(R.id.episode_item_options);
            title = v.findViewById(R.id.episode_number_inside);
            seen = v.findViewById(R.id.episode_inside_seen_indicator);
            itemView.setOnClickListener(v1 -> {
                int position = getAdapterPosition();
                listener.onPlayClick(mItemList.get(position));
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });

            itemView.setOnLongClickListener(this);

            //itemOptions.setOnClickListener(v12 -> {
            //    int position = getAdapterPosition();
            //    listener.onManageClick(mItemList.get(position));
            //});

        }

        @Override
        public boolean onLongClick(View v) {


            longclickCount++;

            Log.d("OFFLINE ADAPTER", String.valueOf(longclickCount));

            if (longclickCount % 2 == 0){
                linearLayout.setVisibility(View.GONE);
            }
            else {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout.setAlpha(0.5f);
            }
            activity.makeSelection(getItem(getAdapterPosition()));
            return false;
        }
    }



    public interface OnItemClicked {
        void onPlayClick(MiniEpisodeOffline anime);
        void onForceStatusUpdate(MiniEpisodeOffline anime);
        void onCastClick(MiniEpisodeOffline anime);
    }
}
