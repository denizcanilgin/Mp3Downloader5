package com.freemusicdownloader.mp3downloader;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import org.jetbrains.annotations.NotNull;
import org.michaelbel.bottomsheet.BottomSheet;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {


    private ImageView iv_icon;
    private View view;

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public Toolbar toolbar;

    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[]{

            "HOME",
            "GENRES",
            "GALLERY",
            "FAV"
    };

    private int[] items = new int[]{
            R.string.action_st_play,
            R.string.action_st_download,
            R.string.action_st_share,
            R.string.action_st_favorite
    };

    private int[] icons = new int[]{
            R.drawable.ic_play_arrow_black_24dp,
            R.drawable.ic_file_download_black_24dp,
            R.drawable.ic_share_black_24dp,
            R.drawable.ic_favorite_black_24dp
    };


    private final Fragment[] PAGES = new Fragment[]{

            new SearchFragment(),
            new GenresFragment(),
            new GalleryFragment(),
            new FavFragment()

    };

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = getWindow().getDecorView().getRootView();
        AudienceNetworkAds.facebookLoadBanner(getApplicationContext(), view);


        view = getWindow().getDecorView().getRootView();
        AudienceNetworkAds.facebookLoadBanner(this, view);

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        if (!getPrefs.getBoolean("first", false)) {
            SharedPreferences.Editor editor = getPrefs.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, IntroActivity.class); // Call the AppIntro java class
            startActivity(intent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_cloud_download_black_24dp);
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_library_music_black_24dp);
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_folder_black_24dp);
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_favorite_black_24dp);
                    }
                }, 1);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_folder, menu);
        return true;
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

        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


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


    }
}