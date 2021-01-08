package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.R;

public class MiniAnimeTable_ViewModel_PagedListAdapter extends PagedListAdapter<MiniAnime, MiniAnimeTable_ViewModel_PagedListAdapter.OrderHolder> {

    private Activity activity;
    public MiniAnimeTable_ViewModel_PagedListAdapter() {
        super(MiniAnime.DIFF_CALLBACK);
    }
    public MiniAnimeTable_ViewModel_PagedListAdapter(Activity ac) {
        super(MiniAnime.DIFF_CALLBACK);
        this.activity = ac;

    }
    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.anime_item, parent, false);
        return new MiniAnimeTable_ViewModel_PagedListAdapter.OrderHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder,
                                 int position) {
        if (position <= -1) {
            return;
        }
        MiniAnime teamObject = getItem(position);

        try {
            holder.txvTeamRowFavourite
                    .setText(teamObject.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class OrderHolder extends RecyclerView.ViewHolder {

        private final TextView txvTeamRowFavourite;

        OrderHolder(View itemView) {
            super(itemView);
            txvTeamRowFavourite = itemView.findViewById(R.id.ep_title);
        }

    }
}
