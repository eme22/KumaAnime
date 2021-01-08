package com.eme22.kumaanime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.eme22.kumaanime.AppUtils.BypassCheck_v4;
import com.eme22.kumaanime.AppUtils.ObscuredPreferences;
import com.eme22.kumaanime.AppUtils.Theming;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.SplashActivity_fragments.FirstLoadFragment;
import com.eme22.kumaanime.SplashActivity_fragments.WebviewBypassFragment;
import com.tingyik90.prefmanager.PrefManager;


public class SplashActivity extends AppCompatActivity {

    private LocalBroadcastManager mLocalBroadcastManager;
    public static String ACTION_CLOSE = "ACTION CLOSE";
    PrefManager prefs;
    Theming theme;
    TaskRunner ts;
    SharedPreferences prefsenc;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_CLOSE)){
                finish();
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loadtheme();

        super.onCreate(savedInstanceState);

        init();

        postInit();


    }

    private void postInit() {

        if (isFirstStart()){
            new Handler(Looper.getMainLooper()).post(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                    // Create new fragment to add (Fragment B)
                    Fragment fragment = new FirstLoadFragment();
                    Transition transition = TransitionInflater.from(SplashActivity.this)
                            .inflateTransition(R.transition.change_image_transform);
                    fragment.setSharedElementEnterTransition(transition);
                    // Our shared element (in Fragment A)
                    ImageView logo = SplashActivity.this.findViewById(R.id.Splash_Image);

                    //System.out.println("OUT");

                    // Add Fragment B
                    FragmentTransaction ft = SplashActivity.this.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.splash_layout, fragment)
                            //.addToBackStack(null)
                            .addSharedElement(logo, "logo_to_first_transition");
                    ft.commit();
                } else {
                    Fragment f = new FirstLoadFragment();
                    FragmentManager manager = SplashActivity.this.getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.splash_layout, f);
                    transaction.commit();
                }
            });
        }
        else {
            //ts.executeAsync(new BypassCheck_v4.FLVCheck(), result1 -> ts.executeAsync(new BypassCheck_v4.JKCheck(), result2 -> ts.executeAsync(new BypassCheck_v4.IDCHECK(), result3 -> check(new boolean[]{result1, result2, result3}))));
            ts.executeAsync(new BypassCheck_v4(), result -> new Handler(Looper.getMainLooper()).post(() -> check(result)));
        }

    }


    private void check(boolean[] res) {

        Fragment f = WebviewBypassFragment.newInstance(res);
        getSupportFragmentManager().beginTransaction().replace(R.id.splash_layout, f, f.getClass().getSimpleName()).addToBackStack(null).commit();

    }

    private void loadtheme(){
        theme = new Theming(this);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            theme.setdaynight();
            //setTheme(R.style.Theme_Splash);
        //}
    }

    private void init(){
        prefs = new PrefManager(this);
        prefsenc = new ObscuredPreferences(this, this.getSharedPreferences("MAL_USERDATA", 0));
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        ts = new TaskRunner();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ACTION_CLOSE);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
        //setTheme(R.style.Theme_Splash);
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
         */
        setContentView(R.layout.activity_splash);
        logcheck();
    }

    private void logcheck() {
        if (prefsenc.getString("TOKEN",null) == null) prefs.putBoolean("isLogged", false);
    }

    private boolean isFirstStart() {

        if (!prefs.getBoolean("isLogged", false)) {
            if (prefsenc.getString("TOKEN", null) == null) {
                prefs.putBoolean("FirstStart", true);
                return true;
            } else return false;
        } else return false;
    }
}


