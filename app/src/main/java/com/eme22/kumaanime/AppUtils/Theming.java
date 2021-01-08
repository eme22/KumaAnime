package com.eme22.kumaanime.AppUtils;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.eme22.kumaanime.R;
import com.tingyik90.prefmanager.PrefManager;


public class Theming {

    PrefManager prefManager;
    Context conts;

    public Theming(Context context) {
        conts = context;
        prefManager = new PrefManager(context);
    }


    public void themecheck() {
        switch (prefManager.getInt("theme", 0)) {
            default: conts.setTheme(R.style.Theme_KumaAnime_NoActionBar);break;
            case 1:
                conts.setTheme(R.style.Theme_KumaAnime_Red_NoActionBar);break;
            case 2:
                conts.setTheme(R.style.Theme_KumaAnime_Green_NoActionBar);break;
            case 3:
               conts.setTheme(R.style.Theme_KumaAnime_Purple_NoActionBar);break;
            case 4:
                conts.setTheme(R.style.Theme_KumaAnime_Orange_NoActionBar);break;
            case 5:
               conts.setTheme(R.style.Theme_KumaAnime_Blue_NoActionBar);break;
        }
    }
    public int gettheme(){
        return prefManager.getInt("theme",0);
    }

    public void settheme(int theme) {

        PrefManager prefManager = new PrefManager(conts);
        switch (theme) {
            default: prefManager.putInt("theme", 0);conts.setTheme(R.style.Theme_KumaAnime_NoActionBar);break;
            case 1:
                prefManager.putInt("theme", 1);conts.setTheme(R.style.Theme_KumaAnime_Red_NoActionBar);break;
            case 2:
                prefManager.putInt("theme", 2);conts.setTheme(R.style.Theme_KumaAnime_Green_NoActionBar);break;
            case 3:
                prefManager.putInt("theme", 3);conts.setTheme(R.style.Theme_KumaAnime_Purple_NoActionBar);break;
            case 4:
                prefManager.putInt("theme", 4);conts.setTheme(R.style.Theme_KumaAnime_Orange_NoActionBar);break;
            case 5:
                prefManager.putInt("theme", 5);conts.setTheme(R.style.Theme_KumaAnime_Blue_NoActionBar);break;
        }
    }

    public void setdaynight(){
        prefManager = new PrefManager(conts);
        String daynight = prefManager.getString("daynight", "2");
        switch (daynight) {
            default: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);break;
            case "1": AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);break;
            case "2":AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);break;
            case "3":AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);break;
        }
    }
}
