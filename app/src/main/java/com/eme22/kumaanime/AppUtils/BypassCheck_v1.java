package com.eme22.kumaanime.AppUtils;

import android.os.AsyncTask;

import com.eme22.kumaanime.AppUtils.AnimeObjects.BaseAnime_Object;
import com.eme22.kumaanime.AppUtils.AnimeObjects.FLVAnime_Object;
import com.eme22.kumaanime.AppUtils.AnimeObjects.JKAnime_Object;

@Deprecated
public class BypassCheck_v1 extends AsyncTask<Integer,Void,Boolean>  {


    public interface BypassInterfaceDummy {
        void processFinish(boolean output);
    }

    public BypassInterfaceDummy delegate = null;

    public BypassCheck_v1(BypassInterfaceDummy delegate) {
        this.delegate = delegate;
    }

    public static boolean isneeded(Integer type) {

        BaseAnime_Object jojo;

        switch (type) {
            default: {
                try {
                    jojo = new FLVAnime_Object("https://www3.animeflv.net/anime/jojos-bizarre-adventure-the-animation");
                } catch (Exception e) {
                    jojo = null;
                }

                return (jojo == null);

            }
            case 1: {
                try {
                    jojo = new JKAnime_Object("https://jkanime.net/jojos-bizarre-adventure-2012/");
                } catch (Exception e) {
                    jojo = null;
                }

                return (jojo == null);

            }
        }
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        boolean yesyesyes = false;
        yesyesyes = isneeded(integers[0]);
        return yesyesyes;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        delegate.processFinish(result);
    }
}
