package com.freemusicdownloader.mp3downloader;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidsx.rateme.OnRatingListener;
import com.androidsx.rateme.RateMeDialog;

import org.michaelbel.bottomsheet.BottomSheet;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {


    private ImageView iv_icon;

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

            new HomeFragment(),
            new GenresFragment(),
            new GalleryFragment(),
            new FavFragment()

    };

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        if (!getPrefs.getBoolean("first", false)) {
            SharedPreferences.Editor editor = getPrefs.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, IntroActivity.class); // Call the AppIntro java class
            startActivity(intent);
        }


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

//        new Handler().postDelayed(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        tabLayout.getTabAt(1).select();
//                    }
//                }, 1);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_folder, menu);
        return true;
    }


    public void showBottomMenu() {

        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setTitle("Settings")
                .setWindowDimming(134)
                .setBackgroundColor(getResources().getColor(R.color.white))
                .setTitleTextColor(getResources().getColor(R.color.dark_primary))
                .setItemTextColor(getResources().getColor(R.color.dark_primary))
                .setIconColor(getResources().getColor(R.color.dark_primary))
                .setDividers(true)
                .setItems(items, icons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (i) {

                            case 0:
                                break;
                            case 1:

                                break;
                            case 2:


                                break;
                            case 3:
                                //Toast.makeText(getActivity(), "Favori", Toast.LENGTH_SHORT).show();

                                break;
                        }

                    }
                });
        builder.show();


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
                .showAppIcon(R.mipmap.icon)
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

    public void float_action(View view) {

        Toast.makeText(getApplicationContext(), "fav", 0).show();


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