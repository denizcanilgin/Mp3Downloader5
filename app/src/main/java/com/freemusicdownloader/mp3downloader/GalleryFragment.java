package com.freemusicdownloader.mp3downloader;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.wang.avi.AVLoadingIndicatorView;

import org.michaelbel.bottomsheet.BottomSheet;

import java.io.File;
import java.io.IOException;
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
    private SwipeMenuCreator mMenuCreator;

    private TextView tv_downloadedempty;

    public customAdapterListMusics.Holder lastPlayed;
    public ProgressDialog mydialog;


    public int countAds = 0;

    public ArrayList<String> listMusicName;
    public ArrayList<String> listMusicTime;
    public ArrayList<String> listMusicNameCont;
    public ArrayList<String> listMusicurll;
    public customAdapterListMusics adapterListMusics;
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


        adapterListMusics = new customAdapterListMusics(getActivity().getApplication(), getAllMusics(pathControl()), listMusicNameCont, listMusicName, listMusicTime);

        mylist.setAdapter(adapterListMusics);
        if (mylist.getAdapter().getCount() != 0) {
//            AudienceNetworkAds.facebookLoadBanner(getActivity(), view);
//            AudienceNetworkAds.facebookInterstitialAd(getActivity(),ads_layout,avLoadingIndicatorView);
            tv_downloadedempty.setVisibility(View.GONE);
        } else {
            tv_downloadedempty.setVisibility(View.VISIBLE);
        }


        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getActivity())) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }


    }

    public class customAdapterListMusics extends BaseAdapter {

        public ArrayList<String> listmusicURL;
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

            listMusicurll = listmusicURL;

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
            rowView = inflater.inflate(R.layout.list_item_row_2, null);

            holder.txt_music_name = (TextView) rowView.findViewById(R.id.songtext);
            holder.txt_music_author = (TextView) rowView.findViewById(R.id.authortext);
            holder.txt_music_time = (TextView) rowView.findViewById(R.id.musictime);
            holder.btn_play_stop = (ImageButton) rowView.findViewById(R.id.playstop2);


            try {

                String s = listmusicname.get(position).toString().trim();
            } catch (Exception e) {
                Log.i("aaaaaaaaaa", "" + e);
            }

            holder.txt_music_name.setText(listmusicname.get(position).toString().trim());
            holder.txt_music_author.setText(listmusicauthor.get(position).toString().trim());
            holder.txt_music_time.setText(listmusictime.get(position).toString().trim());


            mylist.setClickable(true);
            mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int index, long l) {

                    BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                    builder.setTitle(listMusicName.get(index).toString() + "\n- " + listMusicName.get(index).toString())
                            .setWindowDimming(134)
                            .setBackgroundColor(getResources().getColor(R.color.colorWhite))
                            .setTitleTextColor(getResources().getColor(R.color.dark_primary))
                            .setItemTextColor(getResources().getColor(R.color.dark_primary))
                            .setIconColor(getResources().getColor(R.color.dark_primary))
                            .setDividers(true)
                            .setItems(items, icons, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    switch (i) {

                                        case 0:
                                            new GlobalData().setUri(Uri.parse(listMusicurll.get(index).toString()));
                                            new GlobalData().setMusicName(listmusicauthor.get(index).toString().trim());
                                            new GlobalData().setSongListIndex(index);
                                            Intent ıntent = new Intent(getActivity(), MusicPlayerActivity.class);
                                            startActivity(ıntent);
                                            break;
                                        case 1:
                                            delete_item(index);
                                            adapterListMusics.notifyDataSetChanged();
                                            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                                            if (mylist.getAdapter().getCount() != 0) {
                                                tv_downloadedempty.setVisibility(View.GONE);
                                            } else {
                                                tv_downloadedempty.setVisibility(View.VISIBLE);
                                            }

                                            break;
                                        case 2:
                                            share_selected_song(listmusicname.get(position), listmusicauthor.get(position));
                                            break;

                                    }

                                }
                            });
                    builder.show();

                }
            });

            holder.btn_play_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(listmusicURL.get(position).toString()), "audio/mp3");
                    startActivity(intent);
                }
            });

            return rowView;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("starttt","start coming");

    }

    public void delete_item(final int position) {

        try {
            File file = new File(Uri.parse(listMusicurll.get(position)).getPath());
            file.exists();
            file.delete();
            listMusicName = new ArrayList<String>();
            listMusicTime = new ArrayList<String>();
            listMusicNameCont = new ArrayList<String>();
            getAllMusics(pathControl());
            customAdapterListMusics adapterListMusics = new customAdapterListMusics(getActivity().getApplication(), getAllMusics(pathControl()), listMusicNameCont, listMusicName, listMusicTime);
            mylist.setAdapter(adapterListMusics);

            Toast.makeText(getActivity(), "Song Deleted!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getActivity(), "An Error Occurred!", Toast.LENGTH_SHORT).show();
        }

    }

    public void playMusic(String url) {

        new task().execute(url);
    }

    public void stopMusic() {


        mp.stop(); //error
        mp.reset();


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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:

                // Toast.makeText(getActivity(), "Gallery Refresh ...", Toast.LENGTH_SHORT).show();

                listMusicName = new ArrayList<String>();
                listMusicTime = new ArrayList<String>();
                listMusicNameCont = new ArrayList<String>();
                getAllMusics(pathControl());
                customAdapterListMusics adapterListMusics = new customAdapterListMusics(getActivity().getApplication(), getAllMusics(pathControl()), listMusicNameCont, listMusicName, listMusicTime);
                mylist.invalidate();
                mylist.setAdapter(adapterListMusics);

                if (mylist.getAdapter().getCount() != 0) {

                    tv_downloadedempty.setVisibility(View.GONE);
                } else {
                    tv_downloadedempty.setVisibility(View.VISIBLE);
                }


                if (mylist.getAdapter().getCount() != 0) {


//            AudienceNetworkAds.facebookLoadBanner(getActivity(), view);
//            AudienceNetworkAds.facebookInterstitialAd(getActivity(),ads_layout,avLoadingIndicatorView);
                }

                return true;
        }

        return false;
    }

    class task extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mydialog.setMessage("Playing...");
            mydialog.show();
            mydialog.setCancelable(false);
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


        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + File.separator;

        if (path != null) {
            return path;
        } else {
            path = getActivity().getApplication().getFilesDir() + File.separator + "Mp3Download/";

            return path;

        }


    }


}
