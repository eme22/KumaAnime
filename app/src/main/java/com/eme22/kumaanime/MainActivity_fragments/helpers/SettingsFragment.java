package com.eme22.kumaanime.MainActivity_fragments.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.transition.Slide;

import com.eme22.kumaanime.R;
import com.tingyik90.prefmanager.PrefManager;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int BACK_LENGTH = 250;
    private long mLastClickTime = 0;
    public Context context;
    private PrefManager prefs;
    public static FragmentManager manager;
    public static FragmentTransaction transaction;
    private Preference preference1;
    private Preference preference2;

    public SettingsFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);


        preference1 = findPreference("MAL_LOGIN");
        preference2 = findPreference("MAL_LOGOUT");
        Preference preference3 = findPreference("daynight");

        prefs = new PrefManager(requireActivity());


        //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            //getPreferenceScreen().removePreference(preference3);
        //}

        loginonoff(preference1,preference2);

        if(preference1.isEnabled()){
            preference1.setOnPreferenceClickListener(preference -> {
                if((prefs.getBoolean("isLogged", false))){
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.login_already), Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    MalWebviewFragment mal_login = new MalWebviewFragment();
                    manager = getParentFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.add(R.id.main_container, mal_login).addToBackStack(null);
                    transaction.commit();
                }
                return true;
            });
        }

        if(preference2.isEnabled()){
            preference2.setOnPreferenceClickListener(preference -> {
                prefs.putBoolean("isLogged",false);
                requireActivity().recreate();
                return false;
            });
        }


        assert preference3 != null;
        preference3.setOnPreferenceChangeListener((preference, newValue) -> {

            String value = newValue.toString();
            switch (value){
                case "0":{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);break;}
                case "1":{AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);break;}
                case "2":{AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);break;}
                case "3":{AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);break;}
            }
            return true;
        });

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        assert view != null;
        Toolbar toolbar = view.findViewById(R.id.settings_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < BACK_LENGTH){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            new Handler(Looper.getMainLooper()).postDelayed(() -> SettingsFragment.this.requireActivity().getSupportFragmentManager().popBackStack(), BACK_LENGTH);
        });
        Preference preference4 = findPreference("theme");
        assert preference4 != null;
        preference4.setOnPreferenceClickListener(preference -> {

            ThemingFragment theme = new ThemingFragment();
            theme.setEnterTransition(new Slide(Gravity.END));
            theme.setExitTransition(new Slide(Gravity.START));
            manager = getParentFragmentManager();
            transaction = manager.beginTransaction();
            transaction.add(R.id.main_container, theme).addToBackStack(null);
            transaction.commit();
            return false;

        });
        return view;

    }


    private void loginonoff(Preference preference1, Preference preference2){

        boolean isloged = (prefs.getBoolean("isLogged", false));
        if(isloged){
            getPreferenceScreen().addPreference(preference2);
            getPreferenceScreen().removePreference(preference1);

        }

        else {
            getPreferenceScreen().addPreference(preference1);
            getPreferenceScreen().removePreference(preference2);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        loginonoff(preference1,preference2);
    }


}