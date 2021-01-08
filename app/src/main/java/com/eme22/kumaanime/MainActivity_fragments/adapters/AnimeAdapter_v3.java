package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.R;

import java.util.ArrayList;

public class AnimeAdapter_v3 extends RecyclerView.Adapter<AnimeAdapter_v3.ANimeAdapter_v3_ViewHolder> {

    private ArrayList<MiniAnime> fileNames;
    private final int TYPE;
    private final OnItemClicked listener;

    public AnimeAdapter_v3( int type, ArrayList<MiniAnime> animes2, OnItemClicked listener) {
        this.TYPE = type;
        this.fileNames = animes2;
        this.listener = listener;
    }

    public void setAnimes(ArrayList<MiniAnime> animes){
        this.fileNames=animes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ANimeAdapter_v3_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        if (TYPE == 1) {
            return new ANimeAdapter_v3_ViewHolder(TYPE, mInflater.inflate(R.layout.anime_item_outside, parent, false));
        }
        return new ANimeAdapter_v3_ViewHolder(TYPE, mInflater.inflate(R.layout.anime_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ANimeAdapter_v3_ViewHolder holder, int position) {
        MiniAnime currentItem = fileNames.get(position);
        holder.title.setText(currentItem.getTitle());
        try {
            ImageUtils.getSharedInstance().load(currentItem.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(holder.preview.getContext())).error(R.drawable.no_preview_2).into(holder.preview);
            //Glide.with(holder.preview.getContext()).load(currentItem.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(holder.preview.getContext())).error(R.drawable.no_preview_2).into(holder.preview);
        }
        catch (NullPointerException e){
            ImageUtils.getSharedInstance().load(R.drawable.no_preview_2).into(holder.preview);
            //Glide.with(holder.preview.getContext()).load(R.drawable.no_preview_2).placeholder(new CircularProgressDrawable(holder.preview.getContext())).error(R.drawable.no_preview_2).into(holder.preview);
        }
    }

    protected class ANimeAdapter_v3_ViewHolder extends RecyclerView.ViewHolder {
        public ImageView preview;
        public TextView title;
        public ANimeAdapter_v3_ViewHolder(int type,View itemView) {
            super(itemView);
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
                    listener.onItemClick(fileNames.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (null != fileNames ? fileNames.size() : 0);
    }

    public interface OnItemClicked {
        void onItemClick(MiniAnime anime);
    }
}
