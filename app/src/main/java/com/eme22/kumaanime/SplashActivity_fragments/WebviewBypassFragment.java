package com.eme22.kumaanime.SplashActivity_fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.eme22.kumaanime.MainActivity;
import com.eme22.kumaanime.R;

public class WebviewBypassFragment extends Fragment {

    private Handler delayhandler;
    private Runnable delayhandlerrunnable;
    public static final String WORK_NEEDED = "WORK_NEEDED";
    boolean[] jobs;

    public WebviewBypassFragment() {
        // Required empty public constructor
    }


    public static WebviewBypassFragment newInstance(boolean[] jobs_start) {
        WebviewBypassFragment fragment = new WebviewBypassFragment();
        Bundle args = new Bundle();
        args.putBooleanArray(WORK_NEEDED,jobs_start);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobs = getArguments().getBooleanArray(WORK_NEEDED);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

     View v = inflater.inflate(R.layout.fragment_webview_bypass, container, false);

     final WebView browser = v.findViewById(R.id.bypass);

     execute(browser);

     return v;
    }

    private void execute(WebView browser){
        if (jobs[0]){
            bypass(0,"https://www3.animeflv.net/",browser);
        }
        else if (jobs[1]){
            bypass(1,"https://jkanime.net/",browser);
        }
        else if (jobs[2]){
            bypass(2,"https://www.animeid.tv/",browser);
        }
        else {
            getParentFragmentManager().beginTransaction().remove(WebviewBypassFragment.this).commitAllowingStateLoss();
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void bypass(int type, String urlin, WebView browser){
        switch (type){
        case 0: jobs[0] = false; break;
        case 1: jobs[1] = false; break;
        case 2: jobs[2] = false; break;
        }
        browser.loadUrl(urlin);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setLoadWithOverviewMode(true);
        delayhandler = new Handler(Looper.getMainLooper());
        delayhandlerrunnable = () -> {
            Log.e("BYPASS ERROR:", "TIMED OUT");
            execute(browser);
        };
        delayhandler.postDelayed(delayhandlerrunnable, 10000);
        browser.setOnTouchListener((v, event) -> {
            if (delayhandler != null) delayhandler.removeCallbacksAndMessages(null);
            return false;
        });
        browser.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WEBVIEW_URL", view.getUrl());
            }

            @SuppressLint("NewApi")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("ES NECESARIO?",url);
                CookieManager.getInstance().setAcceptThirdPartyCookies(view, true);
                //String cookies = CookieManager.getInstance().getCookie(url);
                delayhandler.removeCallbacks(delayhandlerrunnable);

                execute(browser);

                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (delayhandler != null) delayhandler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("FIRST", false);
        startActivity(intent);

        /*
        intent = new Intent(getActivity(), DatabaseFiller.class);
        requireActivity().startService(intent);
        */

        requireActivity().finish();
    }

}