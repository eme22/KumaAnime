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

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.MainActivity_fragments.DownloadedAnimeFragment;
import com.eme22.kumaanime.R;
import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;

public class OfflineAnimeAdapter_v3 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    DownloadManager downloadManager;
    DownloadedAnimeFragment downloadedAnimeFragment;
    private final OnItemClicked listener;
    private boolean isLoadingAdded = false;



    protected List<MiniAnime> mItemList;

    public OfflineAnimeAdapter_v3(DownloadedAnimeFragment context, DownloadManager downloadManager, OnItemClicked listener) {
        this.listener = listener;
        this.downloadedAnimeFragment = context;
        this.downloadManager = downloadManager;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_item, parent, false);
            return new ItemViewHolder(view);

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



    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        int longclickCount;
        ImageView preview;
        LinearLayout selection;
        TextView title;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            longclickCount = 0;
            selection = itemView.findViewById(R.id.anime_selection);
            selection.setVisibility(View.GONE);
            preview = itemView.findViewById(R.id.image_view_anime);
            title = itemView.findViewById(R.id.ep_title);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(mItemList.get(position));
                }
            });

            itemView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {


            longclickCount++;

            Log.d("OFFLINE ADAPTER", String.valueOf(longclickCount));

            if (longclickCount % 2 == 0){
                selection.setVisibility(View.GONE);
            }
            else {
                selection.setVisibility(View.VISIBLE);
                selection.setAlpha(0.5f);
            }
            downloadedAnimeFragment.makeSelection(getItem(getAdapterPosition()));
            return false;
        }

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

        viewHolder.title.setText(item.getTitle());

        /*
        if (downloadedAnimeFragment.isContextualEnabled){
            viewHolder.selection.setAlpha(0.5f);
            viewHolder.selection.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.selection.setVisibility(View.GONE);
        }

         */

        //File image = new File(downloadManager.getAnime(item.getId()), StringUtils.FormatFile(item.getId(),"png"));
        //Uri imageuri = FileProvider.getUriForFile(imagecontext, imagecontext.getApplicationContext().getPackageName() + ".provider", image);
        try {
            ImageUtils.getSharedInstance().load(downloadManager.getImage(item)).placeholder(new CircularProgressDrawable(viewHolder.preview.getContext())).error(R.drawable.no_preview_2).into(viewHolder.preview, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d("IMAGE UTIL", "Success");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
            //ImageUtils.getSharedInstance().load(imageuri).placeholder(new CircularProgressDrawable(imagecontext)).error(R.drawable.no_preview_2).into(viewHolder.preview);
        }
        catch (NullPointerException e){
            Log.d("No image: ", item.getTitle()+" "+item.getLink());
            ImageUtils.getSharedInstance().load(R.drawable.no_preview_2).into(viewHolder.preview);
        }

    }

    public interface OnItemClicked {
        void onItemClick(MiniAnime anime);
    }
}
