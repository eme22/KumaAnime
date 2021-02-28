package com.eme22.kumaanime.AppUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.RequiresApi;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RequiresApi(21)
public class ObscuredPreferences_v2 {

    Context context;
    String filename;

    public ObscuredPreferences_v2(Context context, String filename) {
        this.context = context;
        this.filename = filename;
    }

    public SharedPreferences getPreferences() throws GeneralSecurityException, IOException
    {
        MasterKey key = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        return EncryptedSharedPreferences.create(
                context,
                filename,
                key,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }



}
