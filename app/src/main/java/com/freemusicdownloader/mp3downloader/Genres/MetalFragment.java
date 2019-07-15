package com.freemusicdownloader.mp3downloader.Genres;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freemusicdownloader.mp3downloader.DownloadAsyncTask;
import com.freemusicdownloader.mp3downloader.R;
import com.google.android.gms.analytics.Tracker;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/* Fragment used as page 1 */
public class MetalFragment extends Fragment {

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

    public ArrayList<String> musicURL;
    public ArrayList<String> musicName;
    public ArrayList<String> musicAuthor;
    public ArrayList<String> musicTime;
    private Tracker mTracker;
    public String sarkıismi;
    public ImageView imgView;
    public TextView song_result;

    private ProgressDialog barProgressDialog;
    public ListView mylist;
    public static MediaPlayer mp;
    public static ProgressDialog mydialog;
    public AlertDialog alert;
    public AlertDialog alerttt;
    private SweetAlertDialog pDialog;


    private SearchView mSearchView;

    public customAdapterListMusics adapterListMusics;

    customAdapterListMusics.Holder lastPlayed;

    private View view;
    public int countAds = 0;
    public int sarkıposition;
    LinearLayout ads_layout;
    LinearLayout noNetworkLayout;
    AVLoadingIndicatorView avLoadingIndicatorView;
    private ViewPager viewpager;

    private NotificationManagerCompat notificationManager;


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


        view = getActivity().getWindow().getDecorView().getRootView();
        // AudienceNetworkAds.facebookLoadBanner(getActivity(), view);
        //  AudienceNetworkAds.facebookInterstitialAd(MainActivity.this,ads_layout,avLoadingIndicatorView);

        notificationManager = NotificationManagerCompat.from(getActivity());


        mylist = (ListView) rootView.findViewById(R.id.listView);
        ads_layout = rootView.findViewById(R.id.ads_layout);
        ads_layout.setVisibility(View.INVISIBLE);
        avLoadingIndicatorView = rootView.findViewById(R.id.avi);

        noNetworkLayout = rootView.findViewById(R.id.noNetworkLayout);
        imgView = rootView.findViewById(R.id.imgview);
        song_result = rootView.findViewById(R.id.song_result);
        song_result.setVisibility(View.INVISIBLE);


        most_popular_songs_metal songsMetal = new most_popular_songs_metal();
        songsMetal.execute();

//
//        new MainActivity().tollbarTitle("Rock");

        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

//        GoogleAnalyticsApplication application = (GoogleAnalyticsApplication) getActivity().getApplication();
//        mTracker = application.getDefaultTracker();


        mydialog = new ProgressDialog(getActivity());

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getActivity())) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }


    }



    @Override
    public void onStart() {
        super.onStart();
    }



    class PlayMusicAction extends AsyncTask<Uri, Uri, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            LoadingMethod();
        }

        @Override
        protected Uri doInBackground(Uri... uris) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uris[0], "audio/mp3");
            startActivity(intent);

            return null;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
//            pDialog.cancel();

            LoadingMethodDismiss();


        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void openFolder(String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setDataAndType(Uri.fromFile(file), "audio/mp3");
        startActivity(Intent.createChooser(intent, ""));
    }

    class customAdapterListMusics extends BaseAdapter {

        ArrayList<String> listmusicURL;
        ArrayList<String> listmusicname;
        ArrayList<String> listmusicauthor;
        ArrayList<String> listmusictime;
        Context context;

        private LayoutInflater inflater = null;

        public customAdapterListMusics(Context cnt, ArrayList<String> listmusic, ArrayList<String> listmusicname, ArrayList<String> listmusicauthor, ArrayList<String> listmusictime) {
            context = cnt;
            this.listmusicURL = listmusic;
            this.listmusicname = listmusicname;
            this.listmusicauthor = listmusicauthor;
            this.listmusictime = listmusictime;


            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public int getCount() {
            return listmusicURL.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override

        public long getItemId(int position) {
            return position;
        }


        public class Holder {

            TextView txt_music_name;
            TextView txt_music_author;
            TextView txt_music_time;
            ImageButton btn_play_stop;
            ImageButton btn_download;


        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            final customAdapterListMusics.Holder holder = new customAdapterListMusics.Holder();
            final View rowView;
            rowView = inflater.inflate(R.layout.list_item_row, null);


            holder.txt_music_name = (TextView) rowView.findViewById(R.id.songtext);
            holder.txt_music_author = (TextView) rowView.findViewById(R.id.authortext);
            holder.txt_music_time = (TextView) rowView.findViewById(R.id.musictime);
            holder.btn_play_stop = (ImageButton) rowView.findViewById(R.id.playstop);
            holder.btn_download = (ImageButton) rowView.findViewById(R.id.downbtn);


            holder.txt_music_name.setText(listmusicname.get(position).toString().trim());
            holder.txt_music_author.setText(listmusicauthor.get(position).toString().trim());
            holder.txt_music_time.setText(listmusictime.get(position).toString().trim());

            lastPlayed = holder;

            mylist.setClickable(true);
            mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int songPos, long l) {

                    BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                    builder.setTitle(listmusicauthor.get(songPos).toString() + "\n- " + listmusicname.get(songPos).toString())
                            .setWindowDimming(134)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                            .setTitleTextColor(Color.WHITE)
                            .setItemTextColor(Color.WHITE)
                            .setIconColor(Color.WHITE)
                            .setDividers(true)
                            .setItems(items, icons, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    switch (i) {

                                        case 0:

                                            Toast.makeText(getActivity(), "Play", Toast.LENGTH_SHORT).show();

                                            if (countAds % 8 == 0) {
                                                // AudienceNetworkAds.facebookInterstitialAd(getActivity(), ads_layout, avLoadingIndicatorView);
                                            }
                                            try {

                                                if (isPackageInstalled("com.google.android.music", getActivity().getPackageManager())) {

                                                    PlayMusicAction playMusicAction = new PlayMusicAction();
                                                    playMusicAction.doInBackground(Uri.parse(listmusicURL.get(songPos).toString()));

                                                } else {


                                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText("MP3 Downloader")
                                                            .setContentText("Please install the 'Google Play Music' for quick play.\nDo you want to install it now?")
                                                            .setConfirmButton("YES", new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                    final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                                                                    try {
                                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.google.android.music")));
                                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.google.android.music")));
                                                                    }

                                                                }
                                                            })
                                                            .setCancelButton("NO", new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                    sweetAlertDialog.cancel();
                                                                }
                                                            })

                                                            .show();

                                                }

                                            } catch (Exception e) {
                                                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText("MP3 Downloader")
                                                        .setContentText("Please install the 'Google Play Music' for quick play.\nDo you want to install it now?")
                                                        .setConfirmButton("YES", new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                                                                try {
                                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.google.android.music")));
                                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.google.android.music")));
                                                                }

                                                            }
                                                        })
                                                        .setCancelButton("NO", new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                sweetAlertDialog.cancel();
                                                            }
                                                        })

                                                        .show();
                                            }


                                            break;
                                        case 1:
                                            Toast.makeText(getActivity(), "Download", Toast.LENGTH_SHORT).show();
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                if (!Settings.System.canWrite(getContext())) {
                                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                            Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                                                }
                                            }

                                            if (ContextCompat.checkSelfPermission(getActivity(),
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


//                        if (countAds % 2 == 0) {
//
//                            AudienceNetworkAds.facebookInterstitialAd(MainActivity.this, ads_layout, avLoadingIndicatorView);
//
//                        }
                                                if (countAds == 0) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", getActivity(), 22).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                } else if (countAds == 1) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", getActivity(), 23).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                } else if (countAds == 2) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", getActivity(), 24).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                } else if (countAds == 3) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", getActivity(), 25).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                }else if (countAds == 4) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", getActivity(), 105).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                    countAds = 0;
                                                }
                                                countAds++;
                                            }
                                            break;
                                        case 2:
                                            Toast.makeText(getActivity(), "Share", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 3:
                                            Toast.makeText(getActivity(), "Favori", Toast.LENGTH_SHORT).show();
                                            break;
                                    }

                                }
                            });
                    builder.show();


                }
            });


            holder.btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }

            });


            holder.btn_play_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });


            return rowView;


        }

    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {

        boolean found = true;

        try {

            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = false;
        }

        return found;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    class most_popular_songs_metal extends AsyncTask<String, Object, customAdapterListMusics> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            LoadingMethod();

        }

        @Override
        protected customAdapterListMusics doInBackground(String... strings) {
            Document doc = null;


            try {
                doc = Jsoup.connect(decodeString("aHR0cDovL21wM3BuLmluZm8vdG9wL21ldGFsLw")).get();

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

                adapterListMusics = new customAdapterListMusics(getActivity().getApplication(), musicURL, musicName, musicAuthor, musicTime);


            } catch (IOException e) {
                e.printStackTrace();

            }
            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(customAdapterListMusics adapterListMusics) {

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

    public static void playMusic(String url) {
        stopMusic();
        new task().execute(url);
    }

    public static void stopMusic() {
        mp.stop(); //error
        mp.reset();
    }

    //task
    static class task extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mydialog.setMessage("Playing...");
            mydialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {


            try {
                if (mp.isPlaying()) {
                    stopMusic();
                }


                mp.setDataSource(strings[0]);
                mp.prepare();
                mp.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        mTracker.setScreenName("MP3 Main Screen");
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


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
