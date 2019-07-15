package com.freemusicdownloader.mp3downloader;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FavFragment extends Fragment implements View.OnClickListener {

    private ListView lvSong;
    private ArrayList<FavMusic> songList;
    Activity activity;
    GlobalClass globalClass;
    private FavMusicsAdapter adapter;

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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page4, container, false);

        songList = globalClass.getSavedFavSongs(activity);
        lvSong = (ListView) rootView.findViewById(R.id.listView);

        if (songList != null) {
            adapter = new FavMusicsAdapter(activity, songList);
            lvSong.setAdapter(adapter);
            lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View arg1,
                                        final int position, long arg3) {
                   // Toast.makeText(getActivity(),"playing : " + songList.get(position).getMusicName(),0).show();


                    BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
                    builder.setTitle(songList.get(position).toString() + "\n- " + songList.get(position).toString())
                            .setWindowDimming(134)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                            .setTitleTextColor(Color.WHITE)
                            .setItemTextColor(Color.WHITE)
                            .setIconColor(Color.WHITE)
                            .setDividers(true)
                            .setItems(items, icons, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            switch (i){

                                                case 0:
                                                    Toast.makeText(getActivity(),"playing : " + songList.get(position).getMusicName(),0).show();
                                                        play_selected_song(position);
                                                    break;
                                                case 1:

                                                    break;

                                                case 2:

                                                    break;

                                                case 3:

                                                    GlobalClass globalClass = new GlobalClass();
                                                    songList.remove(songList.get(position));
                                                    globalClass.saveFavSongs(activity,songList);
                                                    lvSong.setAdapter(adapter);

                                                    break;

                                            }

                                        }
                                    });
                                    builder.show();
                                    globalClass.removeFavSong(getActivity(), songList.get(position));

                }
            });

        }


        return rootView;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        globalClass = new GlobalClass();
    }

    @Override
    public void onClick(View view) {

    }

    public void play_selected_song(int position){

        Toast.makeText(getActivity(), "Play", Toast.LENGTH_SHORT).show();

        try {

            if (isPackageInstalled("com.google.android.music", activity.getPackageManager())) {

                PlayMusicAction playMusicAction = new PlayMusicAction();
                playMusicAction.doInBackground(Uri.parse(songList.get(position).toString()));

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

            LoadingMethodDismiss();


        }
    }

    public void LoadingMethodDismiss() {


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

}
