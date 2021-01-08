package com.eme22.kumaanime.Databases.MainTable;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class MiniAnimeTable_ViewModelCursorFactory implements ViewModelProvider.Factory {
    private final Application mApplication;


    public MiniAnimeTable_ViewModelCursorFactory(Application application) {
        mApplication = application;
    }


    @NotNull
    @Override
    public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
        return (T) new MinAnimeTable_ViewModelCursor(mApplication);
    }
}