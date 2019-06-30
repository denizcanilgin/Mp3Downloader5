package com.freemusicdownloader.mp3downloader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro {


    public ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        addSlide(SampleSlide.newInstance(R.layout.slide_1));
        addSlide(SampleSlide.newInstance(R.layout.slide_2));
        addSlide(SampleSlide.newInstance(R.layout.slide_3));
        addSlide(SampleSlide.newInstance(R.layout.slide_4));


        //addSlide(AppIntroFragment.newInstance("Slide Title","Loooong Description Here",R.drawable.bear,getColor(R.color.colorAccent)));

        setFadeAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish_by_virginty();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish_by_virginty();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    private void finish_by_virginty() {
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int virginity_lost = sharedPref.getInt("virgin",0);

        if(virginity_lost == 1 ) {

            finish();

        }else{

            Intent i = new Intent(IntroActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

    }


}
