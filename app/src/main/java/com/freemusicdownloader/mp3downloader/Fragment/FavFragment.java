package com.freemusicdownloader.mp3downloader.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freemusicdownloader.mp3downloader.Adapter.FavMusicsAdapter;
import com.freemusicdownloader.mp3downloader.DownloadAsync.DownloadAsyncTask;
import com.freemusicdownloader.mp3downloader.Model.FavMusic;
import com.freemusicdownloader.mp3downloader.Model.GlobalClass;
import com.freemusicdownloader.mp3downloader.Constans.GlobalData;
import com.freemusicdownloader.mp3downloader.R;
import com.freemusicdownloader.mp3downloader.Activity.StreamingMp3Player;
import com.wang.avi.AVLoadingIndicatorView;

import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;


public class FavFragment extends Fragment {

    private ListView lvSong;
    private ArrayList<FavMusic> songList;
    private TextView tv_empty_fav;
    Activity activity;
    GlobalClass globalClass;
    private FavMusicsAdapter adapter;
    public int countAds = 0;


    AVLoadingIndicatorView avLoadingIndicatorView;

    private int[] items = new int[]{
            R.string.action_st_play,
            R.string.action_st_download,
            R.string.action_st_share,
            R.string.action_st_remove_favorite
    };

    private int[] icons = new int[]{
            R.drawable.ic_play_arrow_black_24dp,
            R.drawable.ic_file_download_black_24dp,
            R.drawable.ic_share_black_24dp,
            R.drawable.ic_delete_forever_black_24dp
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page4, container, false);

        songList = globalClass.getSavedFavSongs(activity);
        lvSong = rootView.findViewById(R.id.listView);
        tv_empty_fav = rootView.findViewById(R.id.tv_fav_empty);


        if (songList != null) {
            tv_empty_fav.setVisibility(View.GONE);
            adapter = new FavMusicsAdapter(activity, songList);
            lvSong.setAdapter(adapter);
            lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View arg1,
                                        final int position, long arg3) {

                    BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                    builder.setTitle(songList.get(position).getMusicName().toString() + "\n- " + songList.get(position).getMusicAuthor().toString())
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
                                            Toast.makeText(getActivity(), "playing : " + songList.get(position).getMusicName(), Toast.LENGTH_SHORT).show();
                                            play_selected_song(position);
                                            break;
                                        case 1:

                                            download_selected_song(position);

                                            break;

                                        case 2:

                                            share_selected_song(songList.get(position).getMusicName(), songList.get(position).getMusicAuthor());

                                            break;

                                        case 3:

                                            GlobalClass globalClass = new GlobalClass();
                                            songList.remove(songList.get(position));
                                            globalClass.saveFavSongs(activity, songList);
                                            lvSong.setAdapter(adapter);

                                            if (songList.isEmpty())
                                                tv_empty_fav.setVisibility(View.VISIBLE);
                                            else
                                                tv_empty_fav.setVisibility(View.GONE);

                                            break;

                                    }

                                }
                            });
                    builder.show();
                    globalClass.removeFavSong(getActivity(), songList.get(position));

                }
            });

        } else {
            tv_empty_fav.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        globalClass = new GlobalClass();
    }

    public void play_selected_song(int position) {

        try {

            new GlobalData().setMusicName(songList.get(position).getMusicName().toString().trim());
            new GlobalData().setMusicSecondName(songList.get(position).getMusicAuthor().toString().trim());
            new GlobalData().setTextSongURL(songList.get(position).getMusicURL().toString().trim());
            new GlobalData().setStreamMusicTime(songList.get(position).getMusicDuration().toString().trim());

            Intent intent = new Intent(getActivity(), StreamingMp3Player.class);
            startActivity(intent);

        } catch (Exception e) {

        }

    }

    public void download_selected_song(int songPos) {

        // Toast.makeText(getActivity(), "Download", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getContext())) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


            if (countAds == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new DownloadAsyncTask(songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3", getActivity(), 2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3");
            } else if (countAds == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new DownloadAsyncTask(songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3", getActivity(), 3).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3");
            } else if (countAds == 2) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new DownloadAsyncTask(songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3", getActivity(), 4).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3");
            } else if (countAds == 3) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new DownloadAsyncTask(songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3", getActivity(), 5).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3");
            } else if (countAds == 4) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new DownloadAsyncTask(songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3", getActivity(), 100).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, songList.get(songPos).getMusicURL(), songList.get(songPos).getMusicName() + ".mp3");
                countAds = 0;
            }
            countAds++;
        }

    }

    public void share_selected_song(String songName, String songAuthor) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "You should listen to this amazing song! \n \n " + songName + " - " + songAuthor
                + " \n \n https://play.google.com/store/apps/details?id=com.freemusicdownloader.mp3downloader");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.app_name)));

    }

}