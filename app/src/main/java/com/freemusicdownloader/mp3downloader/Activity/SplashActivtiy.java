package com.freemusicdownloader.mp3downloader.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.freemusicdownloader.mp3downloader.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by on 01/31/2017.
 */
public class SplashActivtiy extends Activity {


    private AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activtiy_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        avLoadingIndicatorView = findViewById(R.id.avi);
        avLoadingIndicatorView.setIndicator("BallClipRotatePulseIndicator");
        startAnim();

        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();


        new Timer().schedule(new TimerTask() {


            @Override
            public void run() {

                int virginity_lost = sharedPref.getInt("virgin",0);
               Log.i("virginity_lost","" + virginity_lost);

                Intent i = null;

//                if(virginity_lost == 1) {
//                    i = new Intent(SplashActivtiy.this, MainActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                }else
//                {
//
//                    i = new Intent(SplashActivtiy.this, IntroActivity.class);
//
//
//                }

                i = new Intent(SplashActivtiy.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);


//                editor.putInt("virgin", 1);
//                editor.commit();

            }
        }, 1500);

    }

    void startAnim(){
        avLoadingIndicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }
}