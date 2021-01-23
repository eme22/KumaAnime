package com.eme22.kumaanime.AnimeActivity_fragments;

import com.eme22.kumaanime.AppUtils.AnimeObjects.server.Server;

import java.util.ArrayList;

public interface PlayObserver {
    void onServerPress(String link);
    void onCancelOrDismiss();
}
