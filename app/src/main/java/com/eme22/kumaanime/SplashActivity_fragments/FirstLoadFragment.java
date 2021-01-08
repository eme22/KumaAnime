package com.eme22.kumaanime.SplashActivity_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eme22.kumaanime.AppUtils.AuthUtil;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.MainActivity;
import com.eme22.kumaanime.MainActivity_fragments.helpers.MalWebviewFragment;
import com.eme22.kumaanime.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FirstLoadFragment extends Fragment implements Callback {

    AuthUtil authUtil;
    Button login;
    FloatingActionButton noLogin;
    MalWebviewFragment webviewfragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authUtil = new AuthUtil(requireContext());
        View v = inflater.inflate(R.layout.fragment_first_load, container, false);
        ImageView itemImageView = v.findViewById(R.id.image_logo_first);
        ViewCompat.setTransitionName(itemImageView, "logo_image_2");

        login = v.findViewById(R.id.first_login_button);
        noLogin = v.findViewById(R.id.first_noLoginFAB);
        login.setOnClickListener(v12 -> {

            click();
            loadWeview();


        });
        noLogin.setOnClickListener(v1 -> {
            click();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("FIRST", true);
            startActivity(intent);
            requireActivity().finish();
        });

        return v;
    }

    private void click() {

        login.setClickable(false);
        noLogin.setClickable(false);

    }
    private void click2() {

        login.setClickable(true);
        noLogin.setClickable(true);

    }

    private void loadWeview(){
        webviewfragment = new MalWebviewFragment();
        webviewfragment.setFragmentCallback(this);
        Bundle bundle = new Bundle();
        bundle.putBoolean("FIRST", true);
        webviewfragment.setArguments(bundle);
        FragmentManager manager = getParentFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.splash_layout, webviewfragment).addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onSuccess(Object o) {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("FIRST", true);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onError(Exception e) {
        click2();
    }
}