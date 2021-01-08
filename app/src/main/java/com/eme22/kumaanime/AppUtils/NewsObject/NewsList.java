package com.eme22.kumaanime.AppUtils.NewsObject;



import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.Paging;

import java.util.List;

public class NewsList {

    private List<Datum> data = null;

    private Paging paging;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
