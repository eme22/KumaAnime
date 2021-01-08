package com.eme22.kumaanime.Databases.MainTable;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class MiniAnimeTable_ViewModel_v3_Factory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final int mParam;


    public MiniAnimeTable_ViewModel_v3_Factory(Application application, int param) {
        mApplication = application;
        mParam = param;
    }


    @NotNull
    @Override
    public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
        return (T) new MiniAnimeTable_ViewModel_v3(mApplication, mParam);
    }
}