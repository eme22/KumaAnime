package com.eme22.kumaanime.AnimeActivity_fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.eme22.kumaanime.AppUtils.AnimeObjects.server.Server;
import com.eme22.kumaanime.AppUtils.Servers.Common.CommonServer;
import com.eme22.kumaanime.AppUtils.Servers.Fembed.Datum;
import com.eme22.kumaanime.AppUtils.Servers.Fembed.Fembed;
import com.eme22.kumaanime.AppUtils.Servers.Yu.Yu;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.serverproxy.BufferFile;
import com.eme22.serverproxy.BufferProxy;
import com.eme22.serverproxy.DownloadTask;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.zbiyikli.sgetter.SourceGetter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Play fragment {@link DialogFragment} subclass.
 * Shows DialogFragment with title, labels, and sources
 */

public class PlayFragment_v2 extends DialogFragment {

    private static final TaskRunner taskRunner = new TaskRunner();
    private static final String DIRECT_LABELS = "DIRECT_LABELS";
    private static final String DIRECT_SOURCES = "DIRECT_SOURCES";
    private static final String TITLE = "TITLE";
    String title;
    String[] links;
    String[] labels;
    PlayObserver callback;
    private AlertDialog.Builder builder;

    public PlayFragment_v2(PlayObserver callback) {
        this.callback = callback;
    }

    public static PlayFragment_v2 newInstance(String title,String[] labels, String[] links, PlayObserver callback) {
        PlayFragment_v2 fragment = new PlayFragment_v2(callback);
        Bundle args = new Bundle();
        args.putStringArray(DIRECT_LABELS, labels);
        args.putStringArray(DIRECT_SOURCES, links);
        args.putString(TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {


        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            links = getArguments().getStringArray(DIRECT_SOURCES);
            labels = getArguments().getStringArray(DIRECT_LABELS);
            return work();
        }
        else return super.onCreateDialog(savedInstanceState);

    }

    private Dialog work() {
        builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title);
        Log.d("Nombres:" , String.valueOf(labels.length));
        Log.d("Servers", String.valueOf(links.length));
        if (labels.length == links.length) {
            builder.setItems(labels, (dialog, which) -> {
                callback.onServerPress(links[which]);
            });
            builder.setOnCancelListener(dialog -> callback.onCancelOrDismiss());
            builder.setOnDismissListener(dialog -> callback.onCancelOrDismiss());
        }
        else throw  new NullPointerException();

        return builder.create();

    }




}
