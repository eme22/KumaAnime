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

public class AnimeAdapter_v3_live extends PagedListAdapter<MiniAnime, AnimeAdapter_v3_live.ANimeAdapter_v3_ViewHolder> {

    private final OnItemClicked listener;

    public AnimeAdapter_v3_live(OnItemClicked listener) {
        super(MiniAnime.DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnimeAdapter_v3_live.ANimeAdapter_v3_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.anime_item, parent, false);

        return new ANimeAdapter_v3_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeAdapter_v3_live.ANimeAdapter_v3_ViewHolder holder, int position) {

        MiniAnime country = getItem(position);
        if (country != null) {
            holder.bindTo(country);
        }

    }

    public class ANimeAdapter_v3_ViewHolder extends RecyclerView.ViewHolder {


        TextView countryName;
        ImageView image;

        public ANimeAdapter_v3_ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.ep_title);
            image = itemView.findViewById(R.id.image_view_anime);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(Objects.requireNonNull(getCurrentList()).get(position));
                }
            });
        }

        public void bindTo(MiniAnime country) {
            countryName.setText(country.getTitle());
            //Glide.with(image.getContext()).load(country.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(image.getContext())).error(R.drawable.no_preview_2).into(image);
            ImageUtils.getSharedInstance().load(country.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(image.getContext())).into(image);
        }
    }

    public interface OnItemClicked {
        void onItemClick(MiniAnime anime);
    }
}
