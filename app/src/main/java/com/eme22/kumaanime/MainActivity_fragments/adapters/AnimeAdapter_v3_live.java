package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.R;

import java.util.Objects;

public class AnimeAdapter_v3_live extends PagedListAdapter<MiniAnime, AnimeAdapter_v3_live.animeadapterV3Viewholder> {

    private final OnItemClicked listener;

    public AnimeAdapter_v3_live(OnItemClicked listener) {
        super(MiniAnime.DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public animeadapterV3Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.anime_item, parent, false);

        return new animeadapterV3Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull animeadapterV3Viewholder holder, int position) {

        MiniAnime country = getItem(position);
        if (country != null) {
            holder.bindTo(country);
        }

    }

    public class animeadapterV3Viewholder extends RecyclerView.ViewHolder {


        public TextView title, flv,id,jk,raw;
        ImageView image;

        public animeadapterV3Viewholder(@NonNull View itemView) {
            super(itemView);
            flv = itemView.findViewById(R.id.flv_indicator);
            id = itemView.findViewById(R.id.id_indicator);
            jk = itemView.findViewById(R.id.jk_indicator);
            raw = itemView.findViewById(R.id.raw_indicator);
            title = itemView.findViewById(R.id.ep_title);
            image = itemView.findViewById(R.id.image_view_anime);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(Objects.requireNonNull(getCurrentList()).get(position));
                }
            });
        }

        public void bindTo(MiniAnime country) {

            try {
                String link = country.getLink();
                if (link.contains("animeflv")) flv.setVisibility(View.VISIBLE);
                if (link.contains("animeid")) id.setVisibility(View.VISIBLE);
                if (link.contains("jkanime")) jk.setVisibility(View.VISIBLE);
                if (link.contains("nyaa")) raw.setVisibility(View.VISIBLE);

            } catch (NullPointerException ignored){}

            title.setText(country.getTitle());
            //Glide.with(image.getContext()).load(country.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(image.getContext())).error(R.drawable.no_preview_2).into(image);
            ImageUtils.getSharedInstance().load(country.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(image.getContext())).into(image);
        }
    }

    public interface OnItemClicked {
        void onItemClick(MiniAnime anime);
    }
}
