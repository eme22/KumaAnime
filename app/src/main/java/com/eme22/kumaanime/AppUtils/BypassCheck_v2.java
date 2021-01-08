package com.eme22.kumaanime.AppUtils;

import com.eme22.kumaanime.AppUtils.AnimeObjects.BaseAnime_Object;
import com.eme22.kumaanime.AppUtils.AnimeObjects.FLVAnime_Object;
import com.eme22.kumaanime.AppUtils.AnimeObjects.JKAnime_Object;

import java.util.concurrent.Callable;
@Deprecated
public class BypassCheck_v2 implements Callable<Boolean> {

    private final int io;

    public BypassCheck_v2(Integer io) {
        this.io=io;
    }

    @Override
    public Boolean call() {
        BaseAnime_Object jojo;

        if (io == 1) {
            try {
                jojo = new JKAnime_Object("https://jkanime.net/jojos-bizarre-adventure-2012/");
            } catch (Exception e) {
                jojo = null;
            }

            return (jojo == null);
        }
        try {
            jojo = new FLVAnime_Object("https://www3.animeflv.net/anime/jojos-bizarre-adventure-the-animation");
        } catch (Exception e) {
            jojo = null;
        }

        return (jojo == null);

    }
}
