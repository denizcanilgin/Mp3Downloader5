package com.freemusicdownloader.mp3downloader;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class Admob {

	public static InterstitialAd mInterstitial;
	private static AdView mAdView;

	public static void createLoadInterstitial(final Activity context)

	{


		MobileAds.initialize(context, "ca-app-pub-5713377519464333~8358171597");

		mInterstitial = new InterstitialAd(context);
		mInterstitial.setAdUnitId(context.getResources().getString(
				R.string.admob_showIntersitial_ad_unit_id));
		mInterstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// TODO Auto-generated method stub

				showInterstitial();
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// TODO Auto-generated method stub


				super.onAdFailedToLoad(errorCode);
			}

			@Override
			public void onAdOpened() {
				// TODO Auto-generated method stub

				super.onAdOpened();
			}

			@Override
			public void onAdClosed() {
				// TODO Auto-generated method stub

				super.onAdClosed();
			}

			@Override
			public void onAdLeftApplication() {
				// TODO Auto-generated method stub
				// Called when an ad leaves the app (for example, to go to the
				// browser).

				super.onAdLeftApplication();
			}

		});

		loadInterstitial();

	}

	public static void loadInterstitial() {



		mInterstitial.loadAd(new AdRequest.Builder().build());
	}

	public static void showInterstitial() {
		if (mInterstitial.isLoaded()) {
			mInterstitial.show();
		}
	}

	public static void createLoadBanner(final Context context, final View view) {
		mAdView = (AdView) view.findViewById(R.id.ad_view);
		AdRequest adRequest = new AdRequest.Builder().build();


		MobileAds.initialize(context, "ca-app-pub-5713377519464333/8358171597");


		mAdView.loadAd(adRequest);



		mAdView.setAdListener(new AdListener() {

			@Override
			public void onAdLoaded() {
				// TODO Auto-generated method stub


				super.onAdLoaded();
			}

			@Override
			public void onAdClosed() {
				// TODO Auto-generated method stub


				super.onAdClosed();
			}

			@Override
			public void onAdOpened() {
				// TODO Auto-generated method stub
//

				super.onAdOpened();
			}

			@Override
			public void onAdLeftApplication() {
				// TODO Auto-generated method stub


				super.onAdLeftApplication();
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// TODO Auto-generated method stub


				super.onAdFailedToLoad(errorCode);
				//Toast.makeText(context, "admob hata", Toast.LENGTH_SHORT).show();
				AudienceNetworkAds.facebookLoadBanner(context,view);
			}

		});

	}

}
