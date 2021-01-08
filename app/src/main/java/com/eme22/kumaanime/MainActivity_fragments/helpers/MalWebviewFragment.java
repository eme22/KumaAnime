package com.eme22.kumaanime.MainActivity_fragments.helpers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.AnimeList_Auth;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.PKCE_Util;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.authentication.AuthenticationRequest;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.authentication.AuthenticationResponse;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.AppUtils.AuthUtil;
import com.eme22.kumaanime.AppUtils.OtherUtils;
import com.eme22.kumaanime.R;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eme22.kumaanime.BuildConfig.MAL_CLIENTID;

public class MalWebviewFragment extends Fragment {

    private String response;
    private String verifier;
    private AnimeList_Auth auth;
    private com.eme22.kumaanime.AppUtils.Callback callback;

    public void setFragmentCallback(com.eme22.kumaanime.AppUtils.Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mallogin, container, false);

        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", getString(R.string.loading_login),true);
        auth = new AnimeList_Auth();
        PKCE_Util pkce_util = new PKCE_Util();
        String urlin = null;
        verifier = pkce_util.generateVerifier(128);
        try {
            urlin = auth.getGeneral1(verifier);
        } catch (UnsupportedEncodingException | URISyntaxException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

            final WebView browser = v.findViewById(R.id.mal_login_webview);
            browser.getSettings().setJavaScriptEnabled(true);
            browser.setHorizontalScrollBarEnabled(true);
            browser.setVerticalScrollBarEnabled(true);
            WebSettings settings = browser.getSettings();
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            settings.setSupportZoom(true);
            settings.setLoadWithOverviewMode(true);
            settings.setDomStorageEnabled(true);
            browser.loadUrl(urlin);   //add http at front

            browser.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("animeviewer")) {
                        response = url;
                        String OAuth = auth.getoauthpost(getActivity(), response);
                        if (OAuth == null) {
                            OtherUtils.toast(R.string.login_error);
                            callback.onError(new NullPointerException());
                            requireActivity().getSupportFragmentManager().popBackStack();
                            return false;
                        } else {
                            if (response == null) {
                                OtherUtils.toast(R.string.login_error);
                                callback.onError(new NullPointerException());
                                requireActivity().getSupportFragmentManager().popBackStack();
                                return false;
                            } else {
                                login(OAuth, verifier);
                                return true;

                            }


                        }
                    }
                    return false;
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    if(pd!=null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                }

            });

            return v;

    }

    private void login(String OAuth, String verifier){
        AuthUtil authUtil = new AuthUtil(requireContext());
        MyAnimeListAPIService noAuthService = MyAnimeListAPIAdapter.getApiServiceSingle();

        AuthenticationRequest request = new AuthenticationRequest();
        request.setOAuth(OAuth);
        request.setVerifier2(verifier);

        noAuthService.login("application/json", "application/x-www-form-urlencoded", MAL_CLIENTID,"authorization_code",OAuth,verifier).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(@NotNull Call<AuthenticationResponse> call, @NotNull Response<AuthenticationResponse> response) {

                AuthenticationResponse response1 = response.body();

                assert response1 != null;
                authUtil.saveToken(response1.getAccessToken(),response1.getRefreshToken(),response1.getExpiresIn());
                saveFirst();
                callback.onSuccess(new Object());
                requireActivity().getSupportFragmentManager().popBackStack();

            }

            @Override
            public void onFailure(@NotNull Call<AuthenticationResponse> call, @NotNull Throwable t) {

                Log.e("ERROR LOGIN", t.getLocalizedMessage());
                OtherUtils.toast(R.string.login_error);
                callback.onError( new NullPointerException());
                requireActivity().getSupportFragmentManager().popBackStack();

            }
        });
    }
/*
    private void errortoast(){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getActivity(), getString(R.string.login_error), duration);
        toast.show();
    }

 */


    private void saveFirst(){
        if (getArguments() != null){
            if (getArguments().getBoolean("FIRST")){
                new AuthUtil(requireContext()).setMode(1);
            }
        }
    }

}
