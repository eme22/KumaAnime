package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.util.Log;
import android.view.View;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.MainActivity_fragments.DownloadedAnimeFragment;
import com.eme22.kumaanime.R;
import com.squareup.picasso.Callback;

@Deprecated
public class OfflineAnimeAdapter_v2 extends AnimeAdapter_v4{

    DownloadManager downloadManager;
    DownloadedAnimeFragment downloadedAnimeFragment;

    public OfflineAnimeAdapter_v2(DownloadedAnimeFragment context, DownloadManager downloadManager,OnItemClicked listener) {
        super(0, listener);
        this.downloadedAnimeFragment = context;
        this.downloadManager = downloadManager;
    }

    @Override
    void aditionalListener(View itemView) {
        super.aditionalListener(itemView);
        //itemView.setOnLongClickListener(downloadedAnimeFragment);
        //itemView.setOnLongClickListener(this);
    }

    @Override
    protected void populateItemRows(ItemViewHolder viewHolder, int position) {
        super.populateItemRows(viewHolder, position);
        MiniAnime item = mItemList.get(position);

        viewHolder.title.setText(item.getTitle());

        if (downloadedAnimeFragment.isContextualEnabled){
            viewHolder.selection.setAlpha(0.5f);
            viewHolder.selection.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.selection.setVisibility(View.GONE);
        }

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
}
