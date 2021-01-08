package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.eme22.kumaanime.MainActivity_fragments.util.NewAnimeFetcher;
import com.eme22.kumaanime.R;

import java.util.ArrayList;

public class ThemingSettingsAdapter extends RecyclerView.Adapter<ThemingSettingsAdapter.ViewHolder> {

    private final ArrayList<NewAnimeFetcher.Tuple<String,Integer>> colors;
    private final OnItemClicked listener;
    Context context;

    public ThemingSettingsAdapter(Context context, ArrayList<NewAnimeFetcher.Tuple<String, Integer>> colors, OnItemClicked listener) {
        this.context = context;
        this.colors = colors;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_item_picker,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewAnimeFetcher.Tuple<String,Integer> currentcolor = colors.get(position);
        holder.title.setText(currentcolor.getFirst());
        holder.color.setBackgroundColor(context.getResources().getColor(currentcolor.getSecond()));
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout color;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.iconName);
            color =itemView.findViewById(R.id.iconImage);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    public interface OnItemClicked {
        void onItemClick(Integer color);
    }
}
