package com.freemusicdownloader.mp3downloader.Ads;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.freemusicdownloader.mp3downloader.Ads.Admob;
import com.freemusicdownloader.mp3downloader.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created byon 01/31/2017.
 */
public class AudienceNetworkAds {


    public static AdView adView;
    public static InterstitialAd interstitialAd;
    public static RelativeLayout adViewContainer;
    public static Context cnt;




    public static void facebookLoadBanner(final Context context, final View view)
    {

        adViewContainer = (RelativeLayout) view.findViewById(R.id.adViewContainer);

        adView = new AdView(context, context.getResources().getString(R.string.facebook_banner)
                , AdSize.BANNER_320_50);
        adViewContainer.addView(adView);
        adView.loadAd();
        cnt=context;



        adView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

               Admob.createLoadBanner(context, view);


            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });




      //  AdSettings.addTestDevice("4a8ed8f46b57a2bb7a0781dde878f07f");


    }





    public static void facebookInterstitialAd(final Activity context)
    {


        interstitialAd = new InterstitialAd(context, context.getResources().getString(
                R.string.facebook_Intersitial));

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {


            }


            @Override
            public void onInterstitialDismissed(Ad ad) {


            }

            @Override
            public void onError(Ad ad, AdError adError) {

                Admob.createLoadInterstitial(context);
            }

            @Override
            public void onAdLoaded(Ad ad) {

                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {


            }

            @Override
            public void onLoggingImpression(Ad ad) {



            }
        });
        interstitialAd.loadAd();

        cnt=context;


        AdSettings.setIntegrationErrorMode(AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CALLBACK_MODE);

      //  AdSettings.addTestDevice("4a8ed8f46b57a2bb7a0781dde878f07f");

    }

    static void startAnim(AVLoadingIndicatorView avi){
        avi.show();
        // or avi.smoothToShow();
    }

    static void stopAnim(AVLoadingIndicatorView avi){
        avi.hide();
        // or avi.smoothToHide();
    }



}
