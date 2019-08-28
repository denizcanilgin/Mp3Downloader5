package com.freemusicdownloader.mp3downloader.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freemusicdownloader.mp3downloader.Fragment.GalleryFragment;
import com.freemusicdownloader.mp3downloader.Constans.GlobalData;
import com.freemusicdownloader.mp3downloader.Services.MusicPlayer;
import com.freemusicdownloader.mp3downloader.R;

import org.michaelbel.bottomsheet.BottomSheet;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class GalleryMusicsAdapter extends BaseAdapter {

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

    public ArrayList<String> listMusicName;
    public ArrayList<String> listMusicTime;
    public ArrayList<String> listMusicNameCont;

    private ArrayList<String> listmusicURL;
    private ArrayList<String> listmusicname;
    private ArrayList<String> listmusicauthor;
    private ArrayList<String> listmusictime;
    private Activity activity;
    private Context context;
    private ListView mylist;
    private TextView tv_downloadedempty;
    private ArrayList<String> listMusicurll;
    private TextView txt_music_name;
    private TextView txt_music_author;
    private TextView txt_music_time;
    private ImageButton btn_play_stop;
    private LayoutInflater inflater = null;

    public GalleryMusicsAdapter(ArrayList<String> listmusicURL, ArrayList<String> listmusicname, ArrayList<String> listmusicauthor, ArrayList<String> listmusictime, Activity activity, Context context, ListView mylist, TextView tv_downloadedempty) {
        this.listmusicURL = listmusicURL;
        this.listmusicname = listmusicname;
        this.listmusicauthor = listmusicauthor;
        this.listmusictime = listmusictime;
        this.activity = activity;
        this.context = context;
        this.mylist = mylist;
        this.tv_downloadedempty = tv_downloadedempty;

        listMusicurll = listmusicURL;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        listMusicName = new ArrayList<String>();
        listMusicTime = new ArrayList<String>();
        listMusicNameCont = new ArrayList<String>();

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
        rowView = inflater.inflate(R.layout.list_item_row_2, null);

        txt_music_name = rowView.findViewById(R.id.songtext);
        txt_music_author = rowView.findViewById(R.id.authortext);
        txt_music_time = rowView.findViewById(R.id.musictime);
        btn_play_stop = rowView.findViewById(R.id.playstop2);

        txt_music_name.setText(listmusicname.get(position).toString().trim());
        txt_music_author.setText(listmusicauthor.get(position).toString().trim());
        txt_music_time.setText(listmusictime.get(position).toString().trim());


        mylist.setClickable(true);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int index, long l) {

                BottomSheet.Builder builder = new BottomSheet.Builder(activity);
                builder.setTitle(listmusicauthor.get(index).toString() + "\n- " + listmusicname.get(index).toString())
                        .setWindowDimming(134)
                        .setBackgroundColor(activity.getResources().getColor(R.color.colorWhite))
                        .setTitleTextColor(activity.getResources().getColor(R.color.dark_primary))
                        .setItemTextColor(activity.getResources().getColor(R.color.dark_primary))
                        .setIconColor(activity.getResources().getColor(R.color.dark_primary))
                        .setDividers(true)
                        .setItems(items, icons, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                switch (i) {

                                    case 0:
                                        new GlobalData().setUri(Uri.parse(listMusicurll.get(index).toString()));
                                        new GlobalData().setMusicName(listmusicauthor.get(index).toString().trim());
                                        new GlobalData().setSongListIndex(index);
                                        Intent ıntent = new Intent(activity, MusicPlayer.class);
                                        activity.startActivity(ıntent);
                                        break;
                                    case 1:
                                        delete_item(index);

                                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
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

        btn_play_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(listmusicURL.get(position).toString()), "audio/mp3");
                activity.startActivity(intent);
            }
        });

        return rowView;
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
            GalleryMusicsAdapter adapterListMusics = new GalleryMusicsAdapter(getAllMusics(pathControl()), listMusicNameCont,listMusicName, listMusicTime,activity,activity.getApplication(),mylist,tv_downloadedempty);
            mylist.setAdapter(adapterListMusics);

            Toast.makeText(activity, "Song Deleted!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(activity, "An Error Occurred!", Toast.LENGTH_SHORT).show();
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


        //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + File.separator;
        String path = Environment.getExternalStorageDirectory() + File.separator + "Mp3Download/";

        if (path != null) {
            return path;
        } else {
            path = activity.getApplication().getFilesDir() + File.separator + "Mp3Download/";

            return path;

        }


    }

    public void share_selected_song(String songName, String songAuthor) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "You should listen to this amazing song! \n \n " + songName + " - " + songAuthor
                + " \n \n https://play.google.com/store/apps/details?id=com.freemusicdownloader.mp3downloader");
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, activity.getResources().getText(R.string.app_name)));

    }
}