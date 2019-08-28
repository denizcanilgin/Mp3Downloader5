package com.freemusicdownloader.mp3downloader.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.freemusicdownloader.mp3downloader.R;
import com.freemusicdownloader.mp3downloader.Fragment.SampleSlide;
import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro {


    public ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //setNavBarColor(R.color.white);
        setColorDoneText(R.color.black);
       // setBarColor(R.color.color_gray);
        setNextArrowColor(R.color.black);
        setColorSkipButton(R.color.black);
        setSeparatorColor(R.color.black);
        setColorSkipButton(R.color.black);
        setIndicatorColor(R.color.black,R.color.black);



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

            Intent i = new Intent(IntroActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);


    }


}
