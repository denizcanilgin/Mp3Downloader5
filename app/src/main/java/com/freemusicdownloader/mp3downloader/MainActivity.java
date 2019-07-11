package com.freemusicdownloader.mp3downloader;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidsx.rateme.OnRatingListener;
import com.androidsx.rateme.RateMeDialog;


public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[] {
            "GENRES",
            "HOME",
            "GALLERY"
    };

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[] {
            new GenresFragment(),
            new HomeFragment(),
            new GalleryFragment()


    };

    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the Toolbar as the activity's app bar (instead of the default ActionBar)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Connect the ViewPager to our custom PagerAdapter. The PagerAdapter supplies the pages
        // (fragments) to the ViewPager, which the ViewPager needs to display.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));

        // Connect the tabs with the ViewPager (the setupWithViewPager method does this for us in
        // both directions, i.e. when a new tab is selected, the ViewPager switches to this page,
        // and when the ViewPager switches to a new page, the corresponding tab is selected)
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_folder, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.use:
                Intent aa = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(aa);

                return true;
            case R.id.about:

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Mp3 Downloader");
                builder.setMessage("DISCLAIMER\n" +
                        "\n" +
                        "⚠️This is NOT an official app from the various media services, and this is only an unofficial 3rd-party client that complies with their 3rd party API terms of service. \n" +
                        "⚠️This app is NOT an affiliated nor related product of those services. \n" +
                        "\n" +
                        "All music in the app is available under the Creative Commons Attribution license (https://creativecommons.org/licenses/by-sa/3.0/).\n" +
                        "Author and license links are available for each song");


                builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Tamam butonuna basılınca yapılacaklar

                    }
                });


                builder.show();

                return true;

            case R.id.rate_us:

                showCustomRateMeDialog();


                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showCustomRateMeDialog() {
        new RateMeDialog.Builder(getPackageName(), getString(R.string.app_name))
                .setHeaderBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setBodyBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                .setBodyTextColor(getResources().getColor(R.color.colorAccent))
                .enableFeedbackByEmail("polishmusicbear@gmail.com")
                .showAppIcon(R.drawable.icon)
                .setShowShareButton(true)
                .setRateButtonBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setRateButtonPressedBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setOnRatingListener(new OnRatingListener() {
                    @Override
                    public void onRating(OnRatingListener.RatingAction action, float rating) {

                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        // Nothing to write
                    }
                })
                .build()
                .show(getFragmentManager(), "custom-dialog");
    }


    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }
}