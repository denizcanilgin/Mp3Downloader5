package com.freemusicdownloader.mp3downloader.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freemusicdownloader.mp3downloader.Ads.AudienceNetworkAds;
import com.freemusicdownloader.mp3downloader.DownloadAsync.DownloadAsyncTask;
import com.freemusicdownloader.mp3downloader.DownloadAsync.DownloadFileAsyncOldVersion;
import com.freemusicdownloader.mp3downloader.Model.FavMusic;
import com.freemusicdownloader.mp3downloader.Model.GlobalClass;
import com.freemusicdownloader.mp3downloader.Constans.GlobalData;
import com.freemusicdownloader.mp3downloader.R;
import com.freemusicdownloader.mp3downloader.Fragment.SearchFragment;
import com.freemusicdownloader.mp3downloader.Activity.StreamingMp3Player;

import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SearchMusicsAdapter extends BaseAdapter {

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

    private ArrayList<String> listmusicURL;
    private ArrayList<String> listmusicname;
    private ArrayList<String> listmusicauthor;
    private ArrayList<String> listmusictime;
    private Activity activity;
    private Context context;
    private ListView mylist;
    private LayoutInflater inflater = null;
    private int countAds = 0;

    TextView txt_music_name;
    TextView txt_music_author;
    TextView txt_music_time;
    ImageButton btn_play_stop;
    ImageButton btn_download;

    public SearchMusicsAdapter(ArrayList<String> listmusicURL, ArrayList<String> listmusicname, ArrayList<String> listmusicauthor, ArrayList<String> listmusictime, Activity activity, Context context, ListView mylist) {
        this.listmusicURL = listmusicURL;
        this.listmusicname = listmusicname;
        this.listmusicauthor = listmusicauthor;
        this.listmusictime = listmusictime;
        this.activity = activity;
        this.context = context;
        this.mylist = mylist;

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


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final View rowView;
        rowView = inflater.inflate(R.layout.list_item_row, null);


        txt_music_name = (TextView) rowView.findViewById(R.id.songtext);
        txt_music_author = (TextView) rowView.findViewById(R.id.authortext);
        txt_music_time = (TextView) rowView.findViewById(R.id.musictime);
        btn_play_stop = (ImageButton) rowView.findViewById(R.id.playstop);
        btn_download = (ImageButton) rowView.findViewById(R.id.downbtn);


        txt_music_name.setText(listmusicname.get(position).toString().trim());
        txt_music_author.setText(listmusicauthor.get(position).toString().trim());
        txt_music_time.setText(listmusictime.get(position).toString().trim());

        mylist.setClickable(true);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int songPos, long l) {

                BottomSheet.Builder builder = new BottomSheet.Builder(activity);
                builder.setTitle(listmusicname.get(songPos).toString() + "\n- " + listmusicauthor.get(songPos).toString())
                        .setWindowDimming(134)
                        .setBackgroundColor(activity.getResources().getColor(R.color.white))
                        .setTitleTextColor(activity.getResources().getColor(R.color.dark_primary))
                        .setItemTextColor(activity.getResources().getColor(R.color.dark_primary))
                        .setIconColor(activity.getResources().getColor(R.color.dark_primary))
                        .setDividers(true)
                        .setItems(items, icons, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                switch (i) {
                                    case 0:
                                        if (countAds % 4 == 0) {
                                            AudienceNetworkAds.facebookInterstitialAd(activity);
                                            countAds = 0;
                                        }
                                        countAds++;
                                        new GlobalData().setMusicName(listmusicauthor.get(songPos).toString().trim());
                                        new GlobalData().setMusicSecondName(listmusicname.get(songPos).toString().trim());
                                        new GlobalData().setTextSongURL(listmusicURL.get(songPos).toString());
                                        new GlobalData().setStreamMusicTime(listmusictime.get(songPos).toString().trim());
                                        Intent intent = new Intent(context, StreamingMp3Player.class);
                                        context.startActivity(intent);

                                        break;
                                    case 1:
                                        Toast.makeText(context, "Download will start now", Toast.LENGTH_SHORT).show();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (!Settings.System.canWrite(context)) {
                                                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                                            }
                                        }

                                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                            if (ContextCompat.checkSelfPermission(context,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                                                if (countAds == 0) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", activity, 2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                } else if (countAds == 1) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", activity, 3).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                } else if (countAds == 2) {
                                                    AudienceNetworkAds.facebookInterstitialAd(activity);
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", activity, 4).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                } else if (countAds == 3) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", activity, 5).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                } else if (countAds == 4) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                        new DownloadAsyncTask(listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3", activity, 100).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listmusicURL.get(songPos), listmusicname.get(songPos).toString() + ".mp3");
                                                    AudienceNetworkAds.facebookInterstitialAd(activity);

                                                    countAds = 0;
                                                }
                                                countAds++;
                                            } else {

                                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                                    if (!Settings.System.canWrite(getApplicationContext())) {
                                                        activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2909);
                                                    }
                                                }

                                            }
                                        } else {

                                            if (ContextCompat.checkSelfPermission(context,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                                new DownloadFileAsyncOldVersion(listmusicURL.get(songPos), listmusicname.get(songPos).toString(), activity).execute(listmusicURL.get(songPos), listmusicname.get(songPos).toString());

                                                if (countAds % 3 == 0) {
                                                    AudienceNetworkAds.facebookInterstitialAd(activity);
                                                }
                                                countAds++;
                                            }
                                        }
                                        break;
                                    case 2:
                                        new SearchFragment().share_selected_song(listmusicname.get(position), listmusicauthor.get(position));

                                        break;
                                    case 3:

                                        FavMusic favMusic = new FavMusic(listmusicname.get(songPos), listmusicURL.get(songPos), listmusicauthor.get(songPos), listmusictime.get(songPos));
                                        GlobalClass globalClass = new GlobalClass();
                                        globalClass.addFavMusic(context, favMusic);

                                        String selected_song = listmusicauthor.get(songPos);

                                        Toast.makeText(context, selected_song + " was successfully added to favorites!", Toast.LENGTH_SHORT).show();

                                        break;
                                }
                            }
                        });
                builder.show();
            }
        });

        return rowView;

    }
}
