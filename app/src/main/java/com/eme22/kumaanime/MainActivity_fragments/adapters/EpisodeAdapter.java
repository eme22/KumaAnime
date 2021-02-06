package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AnimeActivity_fragments.AnimeEpisodes;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.R;

import java.util.ArrayList;
import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM_INSIDE = 0;
    private final int TYPE;
    final OnItemClicked listener;
    public List<MiniEpisode> mItemList;


    public EpisodeAdapter(int type, OnItemClicked listener) {
        this.TYPE = type;
        this.listener = listener;
        this.mItemList = new ArrayList<>();
    }

    public void add(MiniEpisode mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void addAll(List < MiniEpisode > mcList) {
        for (MiniEpisode mc: mcList) {
            add(mc);
        }
    }

    public void remove(MiniEpisode city) {
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



    public MiniEpisode getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE_ITEM_OUTSIDE = 1;
        return (TYPE == VIEW_TYPE_ITEM_INSIDE) ? VIEW_TYPE_ITEM_INSIDE : VIEW_TYPE_ITEM_OUTSIDE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM_INSIDE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_item_inside, parent, false);
            return new ItemInsideViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_item_outside, parent, false);
            return new ItemOutsideViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemInsideViewHolder) {
            populateItemRowsInside((ItemInsideViewHolder) holder, position);
        } else {
            populateItemRowsOutside((ItemOutsideViewHolder) holder, position);
        }
    }

     void populateItemRowsInside(ItemInsideViewHolder holder, int position) {
        MiniEpisode miniEpisode = mItemList.get(position);
        /*
        try {
            Picasso.get().load(miniEpisode.getMainPicture().getMedium()).into(holder.preview);
        }
        catch (NullPointerException e){
            Picasso.get().load(R.drawable.no_preview_2).into(holder.preview);
        }
        */
        //Glide.with(holder.preview.getContext()).load(miniEpisode.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(holder.preview.getContext())).error(R.drawable.no_preview_2).into(holder.preview);
        ImageUtils.getSharedInstance().load(miniEpisode.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(holder.preview.getContext())).resize(0,100).error(R.drawable.no_preview_2).into(holder.preview);
        holder.title.setText(holder.itemView.getContext().getString(R.string.episode,miniEpisode.getEpisode()));
        if (miniEpisode.isViewed()) holder.seen.setVisibility(View.VISIBLE);
        else holder.seen.setVisibility(View.GONE);

        holder.itemOptions.setOnClickListener(view -> {

            //creating a popup menu
            PopupMenu popup = new PopupMenu(holder.itemOptions.getContext(), holder.itemOptions);
            //inflating menu from xml resource
            popup.inflate(R.menu.episode_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.episode_download) {
                    listener.onDownloadClick(miniEpisode);
                    return true;
                } else if (itemId == R.id.episode_status) {
                    listener.onForceStatusUpdate(miniEpisode);
                    return true;
                } else if (itemId == R.id.episode_cast) {
                    listener.onCastClick(miniEpisode);
                    return true;
                }
                return false;
            });
            //displaying the popup
            popup.show();

        });

    }

    private void populateItemRowsOutside(ItemOutsideViewHolder holder, int position) {
        MiniEpisode miniEpisode = mItemList.get(position);

        ImageUtils.getSharedInstance().load(miniEpisode.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(holder.preview.getContext())).error(R.drawable.no_preview_2).fit().centerCrop().into(holder.preview);


        /*
        holder.preview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width;
                int height;
                holder.preview.getViewTreeObserver()
                        .removeOnGlobalLayoutListener(this);
                width = holder.preview.getWidth();
                height = 0;

                ImageUtils.getSharedInstance().load(miniEpisode.getMainPicture().getMedium()).resize(width,height).placeholder(new CircularProgressDrawable(holder.preview.getContext())).error(R.drawable.no_preview_2).centerCrop().into(holder.preview);
            }
        });
        */


        Log.d("IMAGE: ", miniEpisode.getMainPicture().getMedium());
        //Glide.with(holder.preview.getContext()).load(miniEpisode.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(holder.preview.getContext())).error(R.drawable.no_preview_2).centerCrop().into(new DrawableImageViewTarget(holder.preview).waitForLayout());
        holder.title.setText(miniEpisode.getName());
        holder.episode.setText(miniEpisode.getEpisode());
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    private class ItemOutsideViewHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout play,download,cast,manage;
        public TextView title, episode;
        public ImageView preview;

        public ItemOutsideViewHolder(@NonNull View itemView) {
            super(itemView);
            initOutside(itemView);
        }

        private void initOutside(View v){
            title = v.findViewById(R.id.ep_title);
            episode = v.findViewById(R.id.ep_number_outside);
            preview = v.findViewById(R.id.image_view_anime);
            play = v.findViewById(R.id.episode_play_button);
            play.getBackground().setAlpha(128);
            download = v.findViewById(R.id.episode_download_button);
            download.getBackground().setAlpha(128);
            cast = v.findViewById(R.id.episode_cast_button);
            cast.getBackground().setAlpha(128);
            manage = v.findViewById(R.id.episode_manage_button);
            manage.getBackground().setAlpha(128);
            manage.setOnClickListener(v1 -> {
                int position = getAdapterPosition();
                listener.onManageClick(mItemList.get(position));
            });
            play.setOnClickListener(v12 -> {
                int position = getAdapterPosition();
                listener.onPlayClick(mItemList.get(position));
            });
            download.setOnClickListener(v12 -> {
                int position = getAdapterPosition();
                listener.onDownloadClick(mItemList.get(position));
            });
            cast.setOnClickListener(v12 -> {
                int position = getAdapterPosition();
                listener.onCastClick(mItemList.get(position));
            });

        }
    }

     class ItemInsideViewHolder extends RecyclerView.ViewHolder {

        private ImageView preview;
        private TextView title;
        private ConstraintLayout itemOptions;

        public LinearLayout seen;

        public ItemInsideViewHolder(@NonNull View itemView) {
            super(itemView);
            initInside(itemView);
        }

        private void initInside(View v){
            preview = v.findViewById(R.id.episode_image_inside);
            itemOptions = v.findViewById(R.id.episode_item_options);
            title = v.findViewById(R.id.episode_number_inside);
            seen = v.findViewById(R.id.episode_inside_seen_indicator);
            itemView.setOnClickListener(v1 -> {
                int position = getAdapterPosition();
                listener.onPlayClick(mItemList.get(position));
            });
            //itemOptions.setOnClickListener(v12 -> {
            //    int position = getAdapterPosition();
            //    listener.onManageClick(mItemList.get(position));
            //});

        }

     }



    public interface OnItemClicked {
        void onPlayClick(MiniEpisode anime);
        void onForceStatusUpdate(MiniEpisode anime);
        void onDownloadClick(MiniEpisode anime);
        void onCastClick(MiniEpisode anime);
        void onManageClick(MiniEpisode anime);
    }

}
