package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.content.Context;
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

public class AnimeDirectoryAdapterLive extends PagedListAdapter<MiniAnime, AnimeDirectoryAdapterLive.ViewHolder> {

    private final AnimeAdapter_v4.OnItemClicked listener;
    private final Context context;

    public AnimeDirectoryAdapterLive(AnimeAdapter_v4.OnItemClicked listener, Context context) {
        super(MiniAnime.DIFF_CALLBACK);
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public AnimeDirectoryAdapterLive.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.anime_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeDirectoryAdapterLive.ViewHolder holder, int position) {
        MiniAnime course = getItem(position);
        if (course != null) {
            holder.bind(course);
        } else {
            holder.clear();
        }
    }

    public interface OnItemClicked {
        void onItemClick(MiniAnime anime);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, flv,id,jk,raw;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view_anime);
            title = itemView.findViewById(R.id.ep_title);
            flv = itemView.findViewById(R.id.flv_indicator);
            id = itemView.findViewById(R.id.id_indicator);
            jk = itemView.findViewById(R.id.jk_indicator);
            raw = itemView.findViewById(R.id.raw_indicator);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        void bind(MiniAnime course) {
            title.setText(course.getTitle());

            try {
                String link = course.getLink();
                if (link.contains("animeflv")) flv.setVisibility(View.VISIBLE);
                if (link.contains("animeid")) id.setVisibility(View.VISIBLE);
                if (link.contains("jkanime")) jk.setVisibility(View.VISIBLE);
                if (link.contains("nyaa")) raw.setVisibility(View.VISIBLE);

            } catch (NullPointerException ignored){}

            try {
                ImageUtils.getSharedInstance().load(course.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(image.getContext())).error(R.drawable.no_preview_2).into(image);
            }
            catch (NullPointerException ignore){
                ImageUtils.getSharedInstance().load(R.drawable.no_preview_2);
            }
            //Glide.with(image.getContext()).load(course.getMainPicture().getMedium()).placeholder(new CircularProgressDrawable(image.getContext())).error(R.drawable.no_preview_2).into(image);
        }

        void clear() {
            title.setText("");
            ImageUtils.getSharedInstance().load(R.drawable.null_drawable).into(image);
            //Glide.with(image.getContext()).load(R.drawable.null_drawable).into(image);
        }
    }
}
