package com.eme22.kumaanime.AppUtils;

import android.widget.Toast;

import androidx.annotation.StringRes;

import com.eme22.kumaanime.App;

public class OtherUtils {

    public static void toast(@StringRes int message){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(App.getInstance(), App.getInstance().getText(message), duration);
        toast.show();
    }


}
