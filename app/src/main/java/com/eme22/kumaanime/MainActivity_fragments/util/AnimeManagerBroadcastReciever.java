package com.eme22.kumaanime.MainActivity_fragments.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.eme22.kumaanime.Services.AnimeManager;

public class AnimeManagerBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Intent serviceIntent = new Intent(context, AnimeManager.class);
            serviceIntent.setAction(".on_boot_action");
            AnimeManager.enqueueWork(context,serviceIntent);
        } else {
            Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran", Toast.LENGTH_LONG).show();
        }

    }
}
