package com.freemusicdownloader.mp3downloader.Ads;

import android.app.Application;

import com.freemusicdownloader.mp3downloader.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created  by <> 05/10/17.
 */
public class GoogleAnalyticsApplication extends Application {

    private Tracker mTracker;


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Setting mTracker to Analytics Tracker declared in our xml Folder
            mTracker = analytics.newTracker(R.xml.app_tracker);
        }
        return mTracker;
    }
}