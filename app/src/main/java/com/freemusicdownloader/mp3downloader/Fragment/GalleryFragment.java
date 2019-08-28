package com.freemusicdownloader.mp3downloader.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.freemusicdownloader.mp3downloader.Adapter.GalleryMusicsAdapter;
import com.freemusicdownloader.mp3downloader.Constans.GlobalData;
import com.freemusicdownloader.mp3downloader.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/* Fragment used as page 2 */
public class GalleryFragment extends Fragment {

    private int[] items = new int[]{
            R.string.action_st_play,
            R.string.action_st_delete,
            R.string.action_st_share
    };

    private int[] icons = new int[]{
            R.drawable.ic_play_arrow_black_24dp,
            R.drawable.ic_delete_black_24dp,
            R.drawable.ic_share_black_24dp
    };

    public ListView mylist;
    public MediaPlayer mp;

    private TextView tv_downloadedempty;

    public ProgressDialog mydialog;


    public int countAds = 0;

    public ArrayList<String> listMusicName;
    public ArrayList<String> listMusicTime;
    public ArrayList<String> listMusicNameCont;
    public ArrayList<String> listMusicurll;
    public GalleryMusicsAdapter adapterListMusics;
    LinearLayout ads_layout;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page2, container, false);

        ads_layout = rootView.findViewById(R.id.ads_layout);
        ads_layout.setVisibility(View.INVISIBLE);
        avLoadingIndicatorView = rootView.findViewById(R.id.avi);
        tv_downloadedempty = rootView.findViewById(R.id.tv_downloaded_empty);

        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(getActivity().getApplication());
        View view = getActivity().getWindow().getDecorView().getRootView();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        mylist = rootView.findViewById(R.id.listemm);
        mp = new MediaPlayer();
        mydialog = new ProgressDialog(getActivity());

        listMusicName = new ArrayList<String>();
        listMusicTime = new ArrayList<String>();
        listMusicNameCont = new ArrayList<String>();

        getAllMusics(pathControl());
        new GlobalData().setSongList(getAllMusics(pathControl()));


        //adapterListMusics = new GalleryMusicsAdapter(getActivity().getApplication(), getAllMusics(pathControl()), listMusicNameCont, listMusicName, listMusicTime);

        adapterListMusics = new GalleryMusicsAdapter(getAllMusics(pathControl()), listMusicNameCont, listMusicName, listMusicTime,getActivity(),getActivity().getApplicationContext(),mylist,tv_downloadedempty);
        mylist.setAdapter(adapterListMusics);

        if(mylist != null) {
            if (mylist.getAdapter().getCount() != 0) {
                tv_downloadedempty.setVisibility(View.GONE);
            } else {
                tv_downloadedempty.setVisibility(View.VISIBLE);
            }
        }


        return rootView;
    }

    public void refresh() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getActivity())) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        listMusicName = new ArrayList<String>();
                        listMusicTime = new ArrayList<String>();
                        listMusicNameCont = new ArrayList<String>();
                        getAllMusics(pathControl());
                        GalleryMusicsAdapter adapterListMusics = new GalleryMusicsAdapter(getAllMusics(pathControl()), listMusicNameCont, listMusicName, listMusicTime,getActivity(),getActivity().getApplicationContext(),mylist,tv_downloadedempty);

                        mylist.invalidate();
                        adapterListMusics.notifyDataSetChanged();
                        mylist.setAdapter(adapterListMusics);
                        new GlobalData().setSongList(getAllMusics(pathControl()));

                        if (mylist.getAdapter().getCount() != 0) {

                            tv_downloadedempty.setVisibility(View.GONE);
                        } else {
                            tv_downloadedempty.setVisibility(View.VISIBLE);
                        }


                    }
                });


            }
        }else{

            listMusicName = new ArrayList<String>();
            listMusicTime = new ArrayList<String>();
            listMusicNameCont = new ArrayList<String>();
            getAllMusics(pathControl());
            GalleryMusicsAdapter adapterListMusics = new GalleryMusicsAdapter(getAllMusics(pathControl()), listMusicNameCont, listMusicName, listMusicTime,getActivity(),getActivity().getApplicationContext(),mylist,tv_downloadedempty);

            mylist.invalidate();
            mylist.setAdapter(adapterListMusics);

            if (mylist.getAdapter().getCount() != 0) {

                tv_downloadedempty.setVisibility(View.GONE);
            } else {
                tv_downloadedempty.setVisibility(View.VISIBLE);
            }

        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("starttt","start coming");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:

                refresh();

                return true;
        }

        return false;
    }


    public ArrayList<String> getAllMusics(String path) {

        ArrayList<String> f = new ArrayList<String>();// list of file paths

        File[] listFile;
        File file = new File(path);

        if (file.isDirectory()) {
            listFile = file.listFiles();

            try {
                for (int i = 0; i < listFile.length; i++) {

                    f.add("file://" + listFile[i].getAbsolutePath());

                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(listFile[i].getAbsolutePath());

                    String timem = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    String teee = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    long dur = Long.parseLong(timem);

                    String seconds = String.valueOf((dur % 60000) / 1000);
                    String minutes = String.valueOf(dur / 60000);
                    if (seconds.length() == 1) {

                        listMusicTime.add("0" + minutes + ":0" + seconds);

                    } else {
                        listMusicTime.add("0" + minutes + ":" + seconds);
                    }

                    Date lastModDate = new Date(listFile[i].lastModified());

                    listMusicName.add(listFile[i].getName());

                    listMusicNameCont.add(lastModDate.toString());


                }
            } catch (Exception e) {

            }


        }
        return f;
    }

    public String pathControl() {


        //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + File.separator;
        String path = Environment.getExternalStorageDirectory() + File.separator + "Mp3Download/";

        if (path != null) {
            return path;
        } else {
            path = getActivity().getApplication().getFilesDir() + File.separator + "Mp3Download/";

            return path;

        }


    }


}
