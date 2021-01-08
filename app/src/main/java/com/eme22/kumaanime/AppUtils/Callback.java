package com.eme22.kumaanime.AppUtils;

public interface Callback {


    void onSuccess( Object o);

    void onError( Exception e);

    class EmptyCallback implements Callback {


        @Override
        public void onSuccess(Object o) {

        }

        @Override public void onError(Exception e) {
        }
    }
}
