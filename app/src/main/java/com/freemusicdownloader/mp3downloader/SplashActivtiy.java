package com.freemusicdownloader.mp3downloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import java.security.PublicKey;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by on 01/31/2017.
 */
public class SplashActivtiy extends Activity {


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activtiy_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();


        ImageView SplashLogo = (ImageView)findViewById(R.id.splashLogo);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splashanim);
        SplashLogo.startAnimation(myanim);

        new Timer().schedule(new TimerTask() {


            @Override
            public void run() {

                int virginity_lost = sharedPref.getInt("virgin",0);
               Log.i("virginity_lost","" + virginity_lost);

                Intent i = null;

                if(virginity_lost == 1) {
                    i = new Intent(SplashActivtiy.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                }else
                {

                    i = new Intent(SplashActivtiy.this, IntroActivity.class);


                }

                startActivity(i);


                editor.putInt("virgin", 1);
                editor.commit();

            }
        }, 2000);

    }
}