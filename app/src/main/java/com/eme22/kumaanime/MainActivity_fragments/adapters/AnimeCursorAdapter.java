package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.R;

public class AnimeCursorAdapter extends CursorAdapter {

    Cursor cursor;

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public AnimeCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.anime_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = (TextView) view.findViewById(R.id.ep_title);
        ImageView image = (ImageView) view.findViewById(R.id.image_view_anime);

        title.setText(cursor.getString(1));
        ImageUtils.getSharedInstance().load(cursor.getString(2)).placeholder(new CircularProgressDrawable(context)).error(R.drawable.no_preview_2).into(image);

    }
}
