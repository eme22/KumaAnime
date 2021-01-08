package com.eme22.kumaanime.AppUtils;

import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeObjects.BaseAnime_Object;
import com.eme22.kumaanime.AppUtils.AnimeObjects.FLVAnime_Object;
import com.eme22.kumaanime.AppUtils.AnimeObjects.JKAnime_Object;

import java.io.IOException;
import java.util.concurrent.Callable;
@Deprecated
public class BypassCheck_v3 implements Callable<boolean[]> {

    @Override
    public boolean[] call() {
        BaseAnime_Object jojo;
        boolean a,b,c = false;
        try {
            jojo = new JKAnime_Object("https://jkanime.net/jojos-bizarre-adventure-2012/");
            b = false;
        }
        catch (NullPointerException e){
            b = true;
        }
        catch (IOException e){
            Log.e("SERVER CHECK: ", e.getLocalizedMessage());
            b = false;
        }

        try {
            jojo = new FLVAnime_Object("https://www3.animeflv.net/anime/jojos-bizarre-adventure-the-animation");
            a = false;
        }
        catch (NullPointerException e){
            a = true;
        }
        catch (IOException e){
            Log.e("SERVER CHECK: ", e.getLocalizedMessage());
            a = false;
        }

        Log.d("SERVER CHECK: ", String.valueOf(a)+ b);
        return new boolean[]{a, b, c};
    }

}
