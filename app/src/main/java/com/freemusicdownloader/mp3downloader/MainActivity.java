package com.freemusicdownloader.mp3downloader;

import android.Manifest;
import android.app.AlertDialog;
import static java.util.Arrays.asList;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsx.rateme.OnRatingListener;
import com.androidsx.rateme.RateMeDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    public ArrayList<String> musicURL;
    public ArrayList<String> musicName;
    public ArrayList<String> musicAuthor;
    public ArrayList<String> musicTime;
    private Tracker mTracker;
    public String sarkıismi;
    public ImageView imgView;
    public TextView song_result;

    public String currentimagepath;
    private ProgressDialog barProgressDialog;
    public ListView mylist;
    public static MediaPlayer mp;
    public static ProgressDialog mydialog;
    public AlertDialog alert;
    public AlertDialog alerttt;
    private SweetAlertDialog pDialog;


    private SearchView mSearchView;
    private BoomMenuButton bmb;

    public customAdapterListMusics adapterListMusics;

    customAdapterListMusics.Holder lastPlayed;

    private View view;
    public int countAds = 0;
    public int sarkıposition;
    LinearLayout ads_layout;
    LinearLayout noNetworkLayout;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }



        ads_layout = findViewById(R.id.ads_layout);
        ads_layout.setVisibility(View.INVISIBLE);
        avLoadingIndicatorView = findViewById(R.id.avi);

        noNetworkLayout = findViewById(R.id.noNetworkLayout);
        imgView = findViewById(R.id.imgview);
        song_result = findViewById(R.id.song_result);
        song_result.setVisibility(View.INVISIBLE);




        GoogleAnalyticsApplication application = (GoogleAnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        final most_popular_songs popularSongs = new most_popular_songs();
        popularSongs.execute();


        view = getWindow().getDecorView().getRootView();
        AudienceNetworkAds.facebookLoadBanner(getApplicationContext(), view);
      //  AudienceNetworkAds.facebookInterstitialAd(MainActivity.this,ads_layout,avLoadingIndicatorView);


        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        assert bmb != null;

        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_2);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
            bmb.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilder().listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {


                    switch (index) {
                        case 0:
                            song_result.setVisibility(View.INVISIBLE);
                            Intent ıntent = new Intent(getApplicationContext(), MusicList.class);
                            startActivity(ıntent);
                            break;
                        case 1:
                            song_result.setVisibility(View.INVISIBLE);
                            imgView.setImageDrawable(null);
//                            most_popular_songs_acoustic_vocal();
                            most_popular_songs_acoustic_vocal most_popular_songs_acoustic_vocal = new most_popular_songs_acoustic_vocal();
                            most_popular_songs_acoustic_vocal.execute();
                            break;
                        case 2:
                            song_result.setVisibility(View.INVISIBLE);
                            imgView.setImageDrawable(null);
                            // most_popular_songs_jazz_blues();
                            most_popular_songs_jazz_blues most_popular_songs_jazz_blues = new most_popular_songs_jazz_blues();
                            most_popular_songs_jazz_blues.execute();
                            AudienceNetworkAds.facebookInterstitialAd(MainActivity.this,ads_layout,avLoadingIndicatorView);

                            break;
                        case 3:
                            song_result.setVisibility(View.INVISIBLE);
                            imgView.setImageDrawable(null);
                            //   most_popular_songs_metal();
                            most_popular_songs_metal most_popular_songs_metal = new most_popular_songs_metal();
                            most_popular_songs_metal.execute();
                            AudienceNetworkAds.facebookInterstitialAd(MainActivity.this,ads_layout,avLoadingIndicatorView);

                            break;
                        case 4:
                            song_result.setVisibility(View.INVISIBLE);
                            imgView.setImageDrawable(null);
                            most_popular_songs popularSongs1 = new most_popular_songs();
                            popularSongs1.execute();
                            break;
                        case 5:
                            song_result.setVisibility(View.INVISIBLE);
                            imgView.setImageDrawable(null);
                            //   most_popular_songs_rock();
                            most_popular_songs_rock most_popular_songs_rock = new most_popular_songs_rock();
                            most_popular_songs_rock.execute();
                            AudienceNetworkAds.facebookInterstitialAd(MainActivity.this,ads_layout,avLoadingIndicatorView);

                            break;
                        case 6:
                            song_result.setVisibility(View.INVISIBLE);
                            imgView.setImageDrawable(null);
                            //   most_popular_songs_classical();
                            most_popular_songs_classical most_popular_songs_classical = new most_popular_songs_classical();
                            most_popular_songs_classical.execute();
                            break;
                        case 7:
                            song_result.setVisibility(View.INVISIBLE);
                            imgView.setImageDrawable(null);
                            //  most_popular_songs_relax();
                            most_popular_songs_relax most_popular_songs_relax = new most_popular_songs_relax();
                            most_popular_songs_relax.execute();
                            break;
                        case 8:
                            song_result.setVisibility(View.INVISIBLE);
                            imgView.setImageDrawable(null);
                            //  most_popular_songs_drumm_bass();
                            most_popular_songs_drumm_bass most_popular_songs_drumm_bass = new most_popular_songs_drumm_bass();
                            most_popular_songs_drumm_bass.execute();
                            break;

                    }
                }
            }));


        mylist = (ListView) findViewById(R.id.listView);
        // most_popular_songs();
        mydialog = new ProgressDialog(MainActivity.this);


        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }





    }



    protected void onStart() {

//        if (Build.VERSION.SDK_INT > 19) {
//            //most_popular_songs();
//            imgView.setVisibility(View.INVISIBLE);
//
//        } else if (Build.VERSION.SDK_INT <= 19) {
//            imgView.setVisibility(View.VISIBLE);
//
//        }
        super.onStart();

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


    private void startDownload(String url, String filename) {

        new DownloadFileAsync().execute(url, filename);
    }

    public boolean onClose() {
        return false;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {


        //SearchMusic (query);
        SearchMusic searchMusic = new SearchMusic();
        searchMusic.execute(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_music, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setQueryHint("Enter your mp3 name ...");


        setupSearchView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.folder:

                Intent intent = new Intent(getApplicationContext(), MusicList.class);
                startActivity(intent);
                return true;
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


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showDialog(DIALOG_DOWNLOAD_PROGRESS);
            barProgressDialog = new ProgressDialog(MainActivity.this);
            barProgressDialog.setTitle("Downloading MP3 ...");
            barProgressDialog.setMessage(sarkıismi);
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
            barProgressDialog.setProgress(0);
            barProgressDialog.setMax(100);
            barProgressDialog.show();
            barProgressDialog.setCancelable(false);


        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                //Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());

                Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

                OutputStream output = null;
                if (isSDPresent) {

                    File folder = new File(Environment.getExternalStorageDirectory() +
                            File.separator + "Mp3Download");


                    if (!folder.exists()) {
                        folder.mkdir();
                    }


                    currentimagepath = Environment.getExternalStorageDirectory() +
                            File.separator + "Mp3Download/" + aurl[1];


                    output = new FileOutputStream(currentimagepath);


                } else {

                    File folder = new File(getApplication().getFilesDir() +
                            File.separator + "Mp3Download/" + aurl[1]);


                    if (!folder.exists()) {
                        folder.mkdir();
                    }

                    currentimagepath = getApplication().getFilesDir() +
                            File.separator + "Mp3Download/" + aurl[1];


                    output = new FileOutputStream(currentimagepath);


                }


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    barProgressDialog.setProgress((int) ((total * 100) / lenghtOfFile));

                    // publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            barProgressDialog.dismiss();
            //MainActivity.this.alert.dismiss();
            final KonfettiView konfettiView = (KonfettiView) findViewById(R.id.viewKonfetti);
            konfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.BLUE)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.RECT)
                    .addSizes(new Size(12, 5f))
                    .setPosition(konfettiView.getX() + konfettiView.getWidth() / 2, konfettiView.getY() + konfettiView.getHeight() / 3)
                    .burst(100);

            createNotification(currentimagepath);


            //if you need some code to run when the delay expires



        }
    }



    class PlayMusicAction extends AsyncTask<Uri,Uri,Uri>{

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



            Log.i("denizcan","closed");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private NotificationManager notifManager;

    public void createNotification(String filepath) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = "default_channel_id"; // default_channel_id
        String title = "Default Channel"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;


        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        Intent intentnatif = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(filepath));

        intentnatif.setDataAndType(Uri.parse("file://" + filepath), "audio/mp3");


        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), id);
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentnatif, 0);
            builder.setContentTitle("Successful file download !")                           // required
                    .setSmallIcon(R.drawable.notif)   // required
                    .setContentText(getString(R.string.app_name)) // required
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent)
                    .setTicker("Successful file download !")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext(), id);
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentnatif, 0);
            builder.setContentTitle("Successful file download !")                           // required
                    .setSmallIcon(R.drawable.notif)   // required
                    .setContentText(getString(R.string.app_name)) // required
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("Successful file download !")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
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
            final Holder holder = new Holder();
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


            holder.btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.System.canWrite(getApplicationContext())) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                        }
                    }


                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            startDownload(listmusicURL.get(position), listmusicname.get(position).toString() + ".mp3");
                            countAds++;
                            if (countAds % 3 == 0) {

                                AudienceNetworkAds.facebookInterstitialAd(MainActivity.this,ads_layout,avLoadingIndicatorView);

                        }
                    }
                }

            });


            holder.btn_play_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    countAds++;
                    if (countAds % 8 == 0) {
                        AudienceNetworkAds.facebookInterstitialAd(MainActivity.this,ads_layout,avLoadingIndicatorView);
                    }
                    try {

                        if (isPackageInstalled("com.google.android.music", getPackageManager())) {

                            PlayMusicAction playMusicAction = new PlayMusicAction();
                            playMusicAction.doInBackground(Uri.parse(listmusicURL.get(position).toString()));

                        } else {


                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("MP3 Downloader")
                                    .setContentText("Please install the 'Google Play Music' for quick play.\nDo you want to install it now?")
                                    .setConfirmButton("YES", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
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

                    }catch (Exception e)
                    {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("MP3 Downloader")
                                .setContentText("Please install the 'Google Play Music' for quick play.\nDo you want to install it now?")
                                .setConfirmButton("YES", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
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


    public class SearchMusic extends AsyncTask<String, String, customAdapterListMusics> {

        @Override
        protected void onPreExecute() {




            if (!isFinishing()) {

                 LoadingMethod();

            }

            imgView.setImageDrawable(null);


            super.onPreExecute();

        }


        @Override
        protected customAdapterListMusics doInBackground(String... strings) {


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

                adapterListMusics = new customAdapterListMusics(getApplication(), musicURL, musicName, musicAuthor, musicTime);


            } catch (IOException e) {
                e.printStackTrace();


                runOnUiThread(new Runnable() {
                    public void run() {
                        final Toast toast = Toast.makeText(getApplicationContext(), "SONG NOT FOUND!\nPLEASE TRY AGAIN ", Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });

            }

            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(customAdapterListMusics s) {

            mylist.setAdapter(s);
            super.onPostExecute(s);
            LoadingMethodDismiss();
            Log.i("denizcan","onPostExecute");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("denizcan","onDestroy");



    }


    class most_popular_songs extends AsyncTask<String, Object, customAdapterListMusics>  {








        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           LoadingMethod();

        }

        @Override
        protected customAdapterListMusics doInBackground(String... strings) {
            Document doc = null;


            try {
                doc = Jsoup.connect(decodeString("aHR0cDovL21wM3BuLm5ldC9zZWFyY2gvcy9mL01vc3QrcG9wdWxhcitzb25ncy8")).get();

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

                adapterListMusics = new customAdapterListMusics(getApplication(), musicURL, musicName, musicAuthor, musicTime);


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
            }catch (Exception e)
            {
                Toast.makeText(MainActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            }


            super.onPostExecute(adapterListMusics);
        }
    }


    class most_popular_songs_rock extends AsyncTask<String, Object, customAdapterListMusics> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingMethod();
        }

        @Override
        protected customAdapterListMusics doInBackground(String... strings) {
            Document doc = null;


            try {
                doc = Jsoup.connect(decodeString("aHR0cDovL21wM3BuLmluZm8vdG9wL3JvY2sv")).get();

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

                adapterListMusics = new customAdapterListMusics(getApplication(), musicURL, musicName, musicAuthor, musicTime);


            } catch (IOException e) {
                e.printStackTrace();

            }
            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(customAdapterListMusics adapterListMusics) {
            mylist.setAdapter(adapterListMusics);
            ads_layout.setVisibility(View.INVISIBLE);
            avLoadingIndicatorView.hide();

            super.onPostExecute(adapterListMusics);
        }
    }


    class most_popular_songs_relax extends AsyncTask<String, Object, customAdapterListMusics> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingMethod();
        }

        @Override
        protected customAdapterListMusics doInBackground(String... strings) {
            Document doc = null;


            try {
                doc = Jsoup.connect(decodeString("aHR0cDovL21wM3BuLmluZm8vdG9wL3JlbGF4Lw")).get();

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

                adapterListMusics = new customAdapterListMusics(getApplication(), musicURL, musicName, musicAuthor, musicTime);


            } catch (IOException e) {
                e.printStackTrace();

            }
            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(customAdapterListMusics adapterListMusics) {
            mylist.setAdapter(adapterListMusics);
            ads_layout.setVisibility(View.INVISIBLE);
            avLoadingIndicatorView.hide();

            super.onPostExecute(adapterListMusics);
        }
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

                adapterListMusics = new customAdapterListMusics(getApplication(), musicURL, musicName, musicAuthor, musicTime);


            } catch (IOException e) {
                e.printStackTrace();

            }
            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(customAdapterListMusics adapterListMusics) {
            mylist.setAdapter(adapterListMusics);
            ads_layout.setVisibility(View.INVISIBLE);
            avLoadingIndicatorView.hide();

            super.onPostExecute(adapterListMusics);
        }
    }


    class most_popular_songs_jazz_blues extends AsyncTask<String, Object, customAdapterListMusics> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingMethod();
        }

        @Override
        protected customAdapterListMusics doInBackground(String... strings) {
            Document doc = null;


            try {
                doc = Jsoup.connect(decodeString("aHR0cDovL21wM3BuLmluZm8vdG9wL2phenpfYmx1ZXMv")).get();

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

                adapterListMusics = new customAdapterListMusics(getApplication(), musicURL, musicName, musicAuthor, musicTime);


            } catch (IOException e) {
                e.printStackTrace();

            }
            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(customAdapterListMusics adapterListMusics) {
            mylist.setAdapter(adapterListMusics);
            ads_layout.setVisibility(View.INVISIBLE);
            avLoadingIndicatorView.hide();

            super.onPostExecute(adapterListMusics);
        }
    }


    class most_popular_songs_drumm_bass extends AsyncTask<String, Object, customAdapterListMusics> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingMethod();
        }

        @Override
        protected customAdapterListMusics doInBackground(String... strings) {
            Document doc = null;


            try {
                doc = Jsoup.connect(decodeString("aHR0cDovL21wM3BuLmluZm8vdG9wL2RydW1fYmFzcy8")).get();

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

                adapterListMusics = new customAdapterListMusics(getApplication(), musicURL, musicName, musicAuthor, musicTime);


            } catch (IOException e) {
                e.printStackTrace();

            }
            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(customAdapterListMusics adapterListMusics) {
            mylist.setAdapter(adapterListMusics);
            ads_layout.setVisibility(View.INVISIBLE);
            avLoadingIndicatorView.hide();

            super.onPostExecute(adapterListMusics);
        }
    }

    class most_popular_songs_acoustic_vocal extends AsyncTask<String, Object, customAdapterListMusics> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingMethod();
        }

        @Override
        protected customAdapterListMusics doInBackground(String... strings) {
            Document doc = null;


            try {
                doc = Jsoup.connect(decodeString("aHR0cDovL21wM3BuLmluZm8vdG9wL2Fjb3VzdGljX3ZvY2FsLw")).get();

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

                adapterListMusics = new customAdapterListMusics(getApplication(), musicURL, musicName, musicAuthor, musicTime);


            } catch (IOException e) {
                e.printStackTrace();

            }
            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(customAdapterListMusics adapterListMusics) {
            mylist.setAdapter(adapterListMusics);
            ads_layout.setVisibility(View.INVISIBLE);
            avLoadingIndicatorView.hide();

            super.onPostExecute(adapterListMusics);
        }
    }


    class most_popular_songs_classical extends AsyncTask<String, Object, customAdapterListMusics> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingMethod();
        }

        @Override
        protected customAdapterListMusics doInBackground(String... strings) {
            Document doc = null;


            try {
                doc = Jsoup.connect(decodeString("aHR0cDovL21wM3BuLmluZm8vdG9wL2NsYXNzaWNhbC8")).get();

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

                adapterListMusics = new customAdapterListMusics(getApplication(), musicURL, musicName, musicAuthor, musicTime);


            } catch (IOException e) {
                e.printStackTrace();

            }
            return adapterListMusics;
        }

        @Override
        protected void onPostExecute(customAdapterListMusics adapterListMusics) {
            mylist.setAdapter(adapterListMusics);
            ads_layout.setVisibility(View.INVISIBLE);
            avLoadingIndicatorView.hide();

            super.onPostExecute(adapterListMusics);
        }
    }


    private void setupSearchView() {

        mSearchView.setIconifiedByDefault(true);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
            SearchableInfo info = searchManager.getSearchableInfo(this.getComponentName());
            mSearchView.setSearchableInfo(info);
        }
//
        mSearchView.setOnQueryTextListener(MainActivity.this);
        mSearchView.setOnCloseListener(MainActivity.this);

        mSearchView.setIconifiedByDefault(true);
        //  mSearchView.setFocusable(false);
        // mSearchView.setIconified(false);
        //  mSearchView.requestFocusFromTouch();
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
    static class task extends AsyncTask<String, Void, Void>

    {

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
    protected void onResume() {
        super.onResume();

        Log.i("denizcan","resume");

        mTracker.setScreenName("MP3 Main Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        return true;
    }

    public void LoadingMethod(){

        mylist = (ListView) findViewById(R.id.listView);

        if(!isNetworkAvailable()){


            noNetworkLayout.setVisibility(View.VISIBLE);
            mylist.setVisibility(View.INVISIBLE);
        }else
        {
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



    public void LoadingMethodDismiss(){

        ads_layout.setVisibility(View.INVISIBLE);
        avLoadingIndicatorView.hide();


    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
