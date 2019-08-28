package com.freemusicdownloader.mp3downloader.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freemusicdownloader.mp3downloader.Adapter.SearchMusicsAdapter;
import com.freemusicdownloader.mp3downloader.Constans.Constants;
import com.freemusicdownloader.mp3downloader.Ads.GoogleAnalyticsApplication;
import com.freemusicdownloader.mp3downloader.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wang.avi.AVLoadingIndicatorView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import static com.facebook.FacebookSdk.getApplicationContext;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private ArrayList<String> musicURL, musicName, musicAuthor, musicTime;
    private Tracker mTracker;
    private ImageView imgView;
    private TextView song_result;
    private ProgressDialog barProgressDialog;
    private ListView mylist;
    private MediaPlayer mp;
    private ProgressDialog mydialog;
    private String currentimagepath;
    private Boolean succesffuly_downloaded;
    private SearchView mSearchView;
    private SearchMusicsAdapter adapterListMusics;
    private LinearLayout ads_layout;
    private LinearLayout noNetworkLayout;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private String songNameeee;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page1, container, false);
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mylist = (ListView) rootView.findViewById(R.id.listView);
        ads_layout = rootView.findViewById(R.id.ads_layout);
        ads_layout.setVisibility(View.INVISIBLE);
        avLoadingIndicatorView = rootView.findViewById(R.id.avi);

        noNetworkLayout = rootView.findViewById(R.id.noNetworkLayout);
        imgView = rootView.findViewById(R.id.imgview);
        song_result = rootView.findViewById(R.id.song_result);
        song_result.setVisibility(View.INVISIBLE);


        most_popular_songs most_popular_songs = new most_popular_songs();
        most_popular_songs.execute();


        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        GoogleAnalyticsApplication application = (GoogleAnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();


        mydialog = new ProgressDialog(getActivity());

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        SearchMusic searchMusic = new SearchMusic();
        searchMusic.execute(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_music, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setQueryHint("Enter your mp3 name ...");

        setupSearchView();

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void share_selected_song(String songName, String songAuthor) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "You should listen to this amazing song! \n \n " + songName + " - " + songAuthor
                + " \n \n https://play.google.com/store/apps/details?id=com.freemusicdownloader.mp3downloader");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.app_name)));

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public class SearchMusic extends AsyncTask<String, String, SearchMusicsAdapter> {

        @Override
        protected void onPreExecute() {


            if (!getActivity().isFinishing()) {

                LoadingMethod();

            }

            imgView.setImageDrawable(null);


            super.onPreExecute();

        }


        @Override
        protected SearchMusicsAdapter doInBackground(String... strings) {


            String s = strings[0];

            String temp = s.trim().replace(" ", "+");

            Document doc = null;


            try {
                doc = Jsoup.connect(decodeString("aHR0cDovL21wM3BuLm5ldC9zZWFyY2gvcy9mLw==") + temp + "/").get();

                Elements body = doc.select("div#xbody");

                Elements url_content = body.select("li.cplayer-sound-item");


                Elements mp3_time_content = url_content.select("em.cplayer-data-sound-time");


                Elements music_name_content = url_content.select("a[href]").select("b.cplayer-data-sound-title");


                Elements music_author_content = url_content.select("a[href]").select("i.cplayer-data-sound-author");

                musicURL = null;
                musicURL = new ArrayList<String>();
                musicName = null;
                musicName = new ArrayList<String>();
                musicAuthor = null;
                musicAuthor = new ArrayList<String>();
                musicTime = null;
                musicTime = new ArrayList<String>();


                for (Element link : url_content) {

                    musicURL.add(link.attr("data-download-url").toString().trim());

                }


                for (Element mtime : mp3_time_content) {


                    musicTime.add(mtime.text().toString().trim());


                }


                for (Element mname : music_name_content) {

                    // get the value from href attribute
                    //  System.out.println("\nlink  URL IMG : " + link.attr("src"));


                    //   txt.append("\n"+mname.select("b.cplayer-data-sound-title").text()); // şarkı isimleri

                    musicName.add(mname.text().trim());


                    // txt.append("\n"+mname.select("i.cplayer-data-sound-author").text());  //sanatçı ismi


                }


                for (Element mauthor : music_author_content) {


                    musicAuthor.add(mauthor.text().trim());


                }

                //adapterListMusics = new SearchMusicsAdapter(getActivity().getApplication(), musicURL, musicName, musicAuthor, musicTime);

                adapterListMusics = new SearchMusicsAdapter(musicURL,musicName,musicAuthor,musicTime,getActivity(),getActivity().getApplicationContext(),mylist);

            } catch (IOException e) {
                e.printStackTrace();


                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        final Toast toast = Toast.makeText(getActivity(), "SONG NOT FOUND!\nPLEASE TRY AGAIN ", Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });

            }

            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(SearchMusicsAdapter s) {

            mylist.setAdapter(s);
            super.onPostExecute(s);
            LoadingMethodDismiss();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    class most_popular_songs extends AsyncTask<String, Object, SearchMusicsAdapter> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            LoadingMethod();

        }

        @Override
        protected SearchMusicsAdapter doInBackground(String... strings) {
            Document doc = null;

            try {
                doc = Jsoup.connect(decodeString(Constants.GENRES_HOMEPAGES)).get();

                Elements body = doc.select("div#xbody");

                Elements url_content = body.select("li.cplayer-sound-item");


                Elements mp3_time_content = url_content.select("em.cplayer-data-sound-time");


                Elements music_name_content = url_content.select("a[href]").select("b.cplayer-data-sound-title");


                Elements music_author_content = url_content.select("a[href]").select("i.cplayer-data-sound-author");

                musicURL = null;
                musicURL = new ArrayList<String>();
                musicName = null;
                musicName = new ArrayList<String>();
                musicAuthor = null;
                musicAuthor = new ArrayList<String>();
                musicTime = null;
                musicTime = new ArrayList<String>();


                for (Element link : url_content) {


                    musicURL.add(link.attr("data-download-url").toString().trim());


                }

                for (Element mtime : mp3_time_content) {


                    musicTime.add(mtime.text().toString().trim());


                }


                for (Element mname : music_name_content) {

                    musicName.add(mname.text().trim());

                }


                for (Element mauthor : music_author_content) {

                    musicAuthor.add(mauthor.text().trim());


                }

                //adapterListMusics = new SearchMusicsAdapter(getActivity().getApplication(), musicURL, musicName, musicAuthor, musicTime);
                adapterListMusics = new SearchMusicsAdapter(musicURL,musicName,musicAuthor,musicTime,getActivity(),getActivity().getApplicationContext(),mylist);


            } catch (IOException e) {
                e.printStackTrace();

            }
            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(SearchMusicsAdapter adapterListMusics) {

            try {
                mylist.setAdapter(adapterListMusics);
                ads_layout.setVisibility(View.INVISIBLE);
                avLoadingIndicatorView.hide();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "An Error Occurred", Toast.LENGTH_SHORT).show();
            }


            super.onPostExecute(adapterListMusics);
        }
    }

    private void setupSearchView() {

        mSearchView.setIconifiedByDefault(true);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
            SearchableInfo info = searchManager.getSearchableInfo(getActivity().getComponentName());
            mSearchView.setSearchableInfo(info);
        }
        mSearchView.setOnQueryTextListener(SearchFragment.this);
        mSearchView.setOnCloseListener(SearchFragment.this);
        mSearchView.setIconifiedByDefault(true);
    }

    private String decodeString(String encoded) {
        byte[] dataDec = Base64.decode(encoded, Base64.DEFAULT);
        String decodedString = "";
        try {

            decodedString = new String(dataDec, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } finally {

            return decodedString;
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        mTracker.setScreenName("MP3 Main Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void LoadingMethod() {

        if (!isNetworkAvailable()) {


            noNetworkLayout.setVisibility(View.VISIBLE);
            mylist.setVisibility(View.INVISIBLE);
        } else {
            noNetworkLayout.setVisibility(View.INVISIBLE);
            mylist.setVisibility(View.VISIBLE);
        }

        ads_layout.setVisibility(View.VISIBLE);
        ads_layout.bringToFront();
        avLoadingIndicatorView.show();
        avLoadingIndicatorView.smoothToShow();
        avLoadingIndicatorView.animate();
        avLoadingIndicatorView.bringToFront();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 500);

    }

    public void LoadingMethodDismiss() {

        ads_layout.setVisibility(View.INVISIBLE);
        avLoadingIndicatorView.hide();

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}