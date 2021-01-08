package com.eme22.kumaanime.MainActivity_fragments.util;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;
    SwipeRefreshLayout refreshlayout;

    public PaginationScrollListener(LinearLayoutManager layoutManager, SwipeRefreshLayout refreshlayout) {
        this.layoutManager = layoutManager;
        this.refreshlayout = refreshlayout;
    }

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int firstcompletelyvisible = layoutManager.findFirstCompletelyVisibleItemPosition();

        if (!isLoading()) {
            if (((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0) && !isLastPage()) {
                loadMoreItems();
            }

            refreshlayout.setEnabled(firstcompletelyvisible == 0);
        }
    }

    protected abstract void loadMoreItems();
    public abstract int getTotalPageCount();
    public abstract boolean isLastPage();
    public abstract boolean isLoading();
}