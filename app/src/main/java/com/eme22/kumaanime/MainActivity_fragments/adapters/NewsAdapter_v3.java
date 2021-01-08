package com.eme22.kumaanime.MainActivity_fragments.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.kumaanime.AppUtils.ImageUtils;
import com.eme22.kumaanime.AppUtils.NewsObject.Datum;
import com.eme22.kumaanime.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

public class NewsAdapter_v3 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM_FLV = 0;
    private static final int ITEM_MAL = 1;
    private static final int ITEM_TOKYO = 2;
    private static final int ITEM_RAMEN = 3;
    private static final int LOADING = -1;
    private final OnItemClicked listener;
    private List<Datum> Datums;

    private boolean isLoadingAdded = false;

    public NewsAdapter_v3(OnItemClicked listener) {
        this.listener = listener;
        Datums = new ArrayList<>();
    }

    public List<Datum> getDatums() {
        return Datums;
    }

    public void setDatums(List<Datum> Datums) {
        this.Datums = Datums;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case ITEM_RAMEN:
            case ITEM_TOKYO:
            case ITEM_FLV: return new DatumVH(inflater.inflate(R.layout.news_item_flv, parent, false));
            case ITEM_MAL: return new DatumVH(inflater.inflate(R.layout.news_item_mal, parent, false));
            default:
            case LOADING:  return new LoadingVH(inflater.inflate(R.layout.item_loading, parent, false));

        }

    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {

        Datum Datum = Datums.get(position);

        if (holder instanceof DatumVH) {

            DatumVH DatumVH = (DatumVH) holder;

            DatumVH.title.setText(Datum.getTitle());
            DatumVH.date.setText(Datum.getDate());
            ImageUtils.getSharedInstance().load(String.valueOf(new CircularProgressDrawable(DatumVH.preview.getContext()))).into(DatumVH.preview);

            //Log.d("SIZE", DatumVH.preview.getHeight()+" "+DatumVH.preview.getWidth());

            /*
            int width;
            int height;
            if (Datum.getType() == 1) {
                width = 0;
                height = DatumVH.preview.getHeight();
            }
            else {
                width = DatumVH.preview.getWidth();
                height = 0;
            }

            ImageUtils.getSharedInstance().load(Datum.getImage()).resize(width,height).error(R.drawable.no_preview_2).placeholder(new CircularProgressDrawable(DatumVH.preview.getContext())).into(DatumVH.preview);
            */
            //Drawable loading = new CircularProgressDrawable(DatumVH.preview.getContext());
            //ImageUtils.getSharedInstance().load(R.drawable.null_drawable).error(R.drawable.no_preview_2).placeholder(new CircularProgressDrawable(DatumVH.preview.getContext())).into(DatumVH.preview);

            if (Datum.getType()== 1) ImageUtils.getSharedInstance().load(Datum.getImage()).fit().error(R.drawable.no_preview_2).placeholder(new CircularProgressDrawable(DatumVH.preview.getContext())).into(DatumVH.preview);
            else DatumVH.preview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    DatumVH.preview.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                    DatumVH.progressBar.setVisibility(View.GONE);
                    //Glide.with(DatumVH.preview.getContext()).load(Datum.getImage()).error(R.drawable.no_preview_2).placeholder(new CircularProgressDrawable(DatumVH.preview.getContext())).into(new DrawableImageViewTarget(DatumVH.preview).waitForLayout());
                    ImageUtils.getSharedInstance().load(Datum.getImage()).resize(DatumVH.preview.getWidth(),0).error(R.drawable.no_preview_2).placeholder(new CircularProgressDrawable(DatumVH.preview.getContext())).into(DatumVH.preview);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return Datums == null ? 0 : Datums.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Datums.get(position).getType();
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Datum mc) {
        Datums.add(mc);
        notifyItemInserted(Datums.size() - 1);
    }

    public void addAll(List<Datum> mcList) {
        for (Datum mc : mcList) {
            add(mc);
        }
    }

    public void remove(Datum city) {
        int position = Datums.indexOf(city);
        if (position > -1) {
            Datums.remove(position);
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
        Datum dummy = new Datum();
        dummy.setType(-1);
        add(dummy);

    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = Datums.size() - 1;
        Datum item = getItem(position);

        if (item != null) {
            Datums.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Datum getItem(int position) {
        return Datums.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class DatumVH extends RecyclerView.ViewHolder {
        ImageView preview;
        TextView title;
        TextView date;
        ProgressBar progressBar;

        public DatumVH(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.anime_news_title_flv);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                title.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }
            progressBar = itemView.findViewById(R.id.anime_news_progressbar);
            date = itemView.findViewById(R.id.anime_news_date_flv);
            preview = itemView.findViewById(R.id.anime_news_image_flv);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(Datums.get(position).getLink());
                }
            });
        }
    }


    protected static class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClicked {
        void onItemClick(String link);
    }

}
