package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.R;

import java.util.ArrayList;
import java.util.List;

public class AnimeAdapter_v4 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int TYPE;
    private final OnItemClicked listener;
    private boolean isLoadingAdded = false;

    protected List<MiniAnime> mItemList;

    public AnimeAdapter_v4(int TYPE, OnItemClicked listener) {
        this.TYPE = TYPE;
        this.listener = listener;
        this.mItemList = new ArrayList<>();
    }

    public void add(MiniAnime mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void addAll(List < MiniAnime > mcList) {
        for (MiniAnime mc: mcList) {
            add(mc);
        }
    }

    public void remove(MiniAnime city) {
        int position = mItemList.indexOf(city);
        if (position > -1) {
            mItemList.remove(position);
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
        add(new MiniAnime());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mItemList.size() - 1;
        MiniAnime item = getItem(position);
        if (item != null) {
            mItemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public MiniAnime getItem(int position) {
        return mItemList.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view;
            int VIEW_TYPE_2_OUTSIDE = 1;
            if (TYPE == VIEW_TYPE_2_OUTSIDE){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_item_outside, parent, false);
            }
            else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_item, parent, false);
            }
            return new ItemViewHolder(TYPE,view);

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView();
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE_LOADING = 1;
        return (position == mItemList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }



    class ItemViewHolder extends RecyclerView.ViewHolder {

         ImageView preview;
         LinearLayout selection;
         TextView title, flv,id,jk,raw;

        public ItemViewHolder(int type, @NonNull View itemView) {
            super(itemView);

            flv = itemView.findViewById(R.id.flv_indicator);
            id = itemView.findViewById(R.id.id_indicator);
            jk = itemView.findViewById(R.id.jk_indicator);
            raw = itemView.findViewById(R.id.raw_indicator);
            selection = itemView.findViewById(R.id.anime_selection);

            if (type == 1) {
                preview = itemView.findViewById(R.id.image_view_anime_outside);
                title = itemView.findViewById(R.id.text_view_title_outside);
            }
            else {
                preview = itemView.findViewById(R.id.image_view_anime);
                title = itemView.findViewById(R.id.ep_title);
            }

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(mItemList.get(position));
                }
            });

            aditionalListener(itemView);
        }
    }

     void aditionalListener(View itemView) {

    }


    private static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.item_loading_progress_bar);
        }
    }

    private void showLoadingView() {
        //ProgressBar would be displayed
    }

    protected void populateItemRows(ItemViewHolder viewHolder, int position) {

        MiniAnime item = mItemList.get(position);

        /*
        try {
            String link = item.getLink();
            Log.d("TAG ID N:", item.getTitle());
            Log.d("TAG ID L:", link);
            if (link.contains("animeflv")) viewHolder.flv.setVisibility(View.VISIBLE);
            if (link.contains("animeid")) viewHolder.id.setVisibility(View.VISIBLE);
            if (link.contains("jkanime")) viewHolder.jk.setVisibility(View.VISIBLE);
            if (link.contains("nyaa")) viewHolder.raw.setVisibility(View.VISIBLE);

        } catch (NullPointerException e){e.printStackTrace();}
        */



        viewHolder.title.setText(item.getTitle());
        try {
            ImageUtils.getSharedInstance().load(item.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(viewHolder.preview.getContext())).error(R.drawable.no_preview_2).into(viewHolder.preview);
        }
        catch (NullPointerException e){
            Log.d("No image: ", item.getTitle()+" "+item.getLink());
            ImageUtils.getSharedInstance().load(R.drawable.no_preview_2).into(viewHolder.preview);
        }


        /*
        try {
            Glide.with(viewHolder.preview.getContext()).load(item.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(viewHolder.preview.getContext())).error(R.drawable.no_preview_2).into(viewHolder.preview);
        }
        catch (NullPointerException e){
            Log.d("ITEM ERROR: ",item.getTitle()+" "+item.getShow_type()+" "+item.getLink());
        }*/



    }

    public interface OnItemClicked {
        void onItemClick(MiniAnime anime);
    }

}
