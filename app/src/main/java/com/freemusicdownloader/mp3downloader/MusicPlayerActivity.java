package com.freemusicdownloader.mp3downloader;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class MusicPlayerActivity extends AppCompatActivity {
    boolean isBinded = false;
    MediaPlaybackService mediaPlaybackService;

    BroadcastReceiver receiverElapsedTime;
    BroadcastReceiver receiverCompleted;

    ViewGroup rootView;
    ImageButton buttonPlayPause, buttonNext, buttonPrevious;
    ImageButton buttonStop;
    ImageView albumArt;
    TextView titleTextView;
    TextView artistTextView;
    TextView elapsedTimeTextView;
    TextView durationTextView;
    AppCompatSeekBar elapsedTimeSeekBar;
    FloatingActionButton fab;
    ArrayList<String> gallerySongList;
    int songListIndex;
    public ServiceConnection connection;
    private NotificationManagerCompat notificationManager;

    int elapsedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        notificationManager = NotificationManagerCompat.from(this);

        rootView = (ViewGroup) findViewById(android.R.id.content);
        buttonPlayPause = (ImageButton) findViewById(R.id.imageButtonPlayPause);
        buttonNext = (ImageButton) findViewById(R.id.imageButtonNext);
        buttonPrevious = (ImageButton) findViewById(R.id.imageButtonPrevious);
        albumArt = (ImageView) findViewById(R.id.albumArt);
        titleTextView = (TextView) findViewById(R.id.textViewTitle);
        artistTextView = (TextView) findViewById(R.id.textViewArtist);
        elapsedTimeTextView = (TextView) findViewById(R.id.textViewElapsedTime);
        durationTextView = (TextView) findViewById(R.id.textViewDuration);
        elapsedTimeSeekBar = (AppCompatSeekBar) findViewById(R.id.seekBar);

        gallerySongList = new GlobalData().getSongList();
        songListIndex = new GlobalData().getSongListIndex();

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mediaPlaybackService = ((MediaPlaybackService.IDBinder) service).getService();
                new GlobalData().setMediaPlaybackService(mediaPlaybackService);
                isBinded = true;
                initInfos(mediaPlaybackService.getFile());

                Uri selectedtrack = new GlobalData().getUri();
                Log.i("receiveUri", "" + selectedtrack);
                try {
                    mediaPlaybackService.init(selectedtrack);

                    initInfos(selectedtrack);
                    Intent serviceIntent = new Intent(MusicPlayerActivity.this, MediaPlaybackService.class);
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(serviceIntent);
                } catch (Exception e) {
                    Log.i("erorororo", "" + e);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBinded = false;
            }
        };

        receiverElapsedTime = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean a = mediaPlaybackService.isPlaying();
                Log.i("playingresult", "" + a);
                if (!a) {
                    buttonPlayPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    new GlobalData().setMediaPlaybackService(mediaPlaybackService);
                }
                elapsedTime = intent.getIntExtra(MediaPlaybackService.MPS_MESSAGE, 0);
                updateElapsedTime(elapsedTime);
            }
        };

        receiverCompleted = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };


        buttonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GlobalData().setMediaPlaybackService(mediaPlaybackService);
                int resId;
                if (mediaPlaybackService.isPlaying()) {
                    resId = R.drawable.ic_play_circle_filled_black_24dp;
                    mediaPlaybackService.pause();

                } else {
                    resId = R.drawable.ic_pause_circle;
                    mediaPlaybackService.play();
                }
                buttonPlayPause.setImageResource(resId);
            }
        });
        buttonPlayPause.setEnabled(true);


        elapsedTimeSeekBar.setEnabled(false);
        elapsedTimeSeekBar.setProgress(0);
        elapsedTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlaybackService.seekTo(seekBar.getProgress());
            }
        });

        songNext();
        songPrevious();

    }

    @Override
    protected void onResume() {

        getApplicationContext().bindService(new Intent(getApplicationContext(),
                MediaPlaybackService.class), connection, BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiverElapsedTime,
                new IntentFilter(MediaPlaybackService.MPS_RESULT)
        );
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverCompleted,
                new IntentFilter(MediaPlaybackService.MPS_COMPLETED)

        );

        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void songNext() {

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    clearInfos();
                    songListIndex++;
                    if (gallerySongList.size() == songListIndex) {
                        songListIndex = 0;
                    }
                    // Stuff that updates the UI
                    mediaPlaybackService.init(Uri.parse(gallerySongList.get(songListIndex)));
                    initInfos(Uri.parse(gallerySongList.get(songListIndex)));
                    Intent serviceIntent = new Intent(MusicPlayerActivity.this, MediaPlaybackService.class);
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(serviceIntent);

                    File file = new File(gallerySongList.get(songListIndex)+"");
                    new GlobalData().setMusicName(file.getName());

                } catch (Exception e) {
                    Log.i("erorrrrrrrrrrrr", "" + e);
                }
            }
        });
    }

    public void songPrevious() {

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getApplicationContext(), "Clicked Previous" + songListIndex, Toast.LENGTH_SHORT).show();
                try {
                    clearInfos();
                    songListIndex--;
                    if (songListIndex < 0) {

                        songListIndex = gallerySongList.size() - 1;
                    }

                    mediaPlaybackService.init(Uri.parse(gallerySongList.get(songListIndex)));
                    initInfos(Uri.parse(gallerySongList.get(songListIndex)));
                    Intent serviceIntent = new Intent(MusicPlayerActivity.this, MediaPlaybackService.class);
                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(serviceIntent);

                    File file = new File(gallerySongList.get(songListIndex)+"");
                    new GlobalData().setMusicName(file.getName());


                } catch (Exception e) {
                    Log.i("erorrrrrrrrrrrr", "" + e);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverElapsedTime);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverCompleted);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri selectedtrack = data.getData();
            mediaPlaybackService.init(selectedtrack);
            initInfos(selectedtrack);
        }
    }

    private String secondsToString(int time) {
        time = time / 1000;
        return String.format("%2d:%02d", time / 60, time % 60);
    }

    private void initInfos(Uri uri) {
        if (uri != null) {
            FFmpegMediaMetadataRetriever mData = new FFmpegMediaMetadataRetriever();
            mData.setDataSource(this, uri);

            File file = new File(uri+"");

            int duration = Integer.parseInt(mData.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));

                titleTextView.setText(file.getName());
                artistTextView.setText(mData.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST));

            durationTextView.setText(secondsToString(duration));

            elapsedTimeSeekBar.setMax(duration);
            elapsedTimeSeekBar.setEnabled(true);

            try {
                byte art[] = mData.getEmbeddedPicture();
                if (art != null) {
                    Bitmap image = BitmapFactory.decodeByteArray(art, 0, art.length);
                    albumArt.setImageBitmap(image);
                } else {
                    albumArt.setImageDrawable(getResources().getDrawable(R.drawable.ic_album_white_400_128dp));
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private void updateElapsedTime(int elapsedTime) {
        elapsedTimeSeekBar.setProgress(elapsedTime);
        elapsedTimeTextView.setText(secondsToString(elapsedTime));

        if (mediaPlaybackService.isPlaying()) {
            buttonPlayPause.setEnabled(true);
            buttonPlayPause.setImageResource(R.drawable.ic_pause_circle);
        }
    }

    private void clearInfos() {
        durationTextView.setText("");
        elapsedTimeTextView.setText("");
        titleTextView.setText("-");
        artistTextView.setText("-");
        elapsedTime = 0;
        elapsedTimeSeekBar.setEnabled(false);
        elapsedTimeSeekBar.setProgress(0);
        albumArt.setImageResource(R.drawable.ic_album_white_400_128dp);
        buttonPlayPause.setEnabled(false);
        buttonPlayPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);

    }

}
