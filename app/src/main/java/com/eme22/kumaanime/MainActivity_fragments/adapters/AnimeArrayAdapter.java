package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Dao;
import com.eme22.kumaanime.R;

import java.util.ArrayList;
import java.util.List;

public class AnimeArrayAdapter extends ArrayAdapter<MiniAnime> {

    ArrayList<MiniAnime> list;


    public AnimeArrayAdapter(@NonNull Context context, @NonNull List<MiniAnime> objects) {
        super(context, 0);
        list = new ArrayList<>(objects);

    }


    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.anime_item, parent, false);
        // edit: no need to call bindView here. That's done automatically
        return v;
    }


    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = view.findViewById(R.id.ep_title);
        ImageView preview = view.findViewById(R.id.image_view_anime);
        String text = cursor.getString(1);
        String image = cursor.getString(2).split(",")[0];
        Log.d("CURSOR IMAGE", image);
        title.setText(text);
        ImageUtils.getSharedInstance().load(image).placeholder(new CircularProgressDrawable(preview.getContext())).error(R.drawable.no_preview_2).into(preview);

    }
}
