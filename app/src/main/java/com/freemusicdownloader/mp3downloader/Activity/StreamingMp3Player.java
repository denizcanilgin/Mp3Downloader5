package com.freemusicdownloader.mp3downloader.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.freemusicdownloader.mp3downloader.Constans.GlobalData;
import com.freemusicdownloader.mp3downloader.R;
import com.kaopiz.kprogresshud.KProgressHUD;

public class StreamingMp3Player extends Activity implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {

    private ImageButton buttonPlayPause;
    private SeekBar seekBarProgress;
    private TextView musicName, musicAuthor, textViewDuration, elapsedTimeTextView;
    private KProgressHUD kProgressHUD;
    BroadcastReceiver receiverElapsedTime2;


    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class

    private final Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streamingmp3player);

        initView();

        new StreamingMp3().execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

    }

    private void initView() {
        buttonPlayPause = findViewById(R.id.ButtonTestPlayPause);
        musicName = findViewById(R.id.textViewTitle);
        musicAuthor = findViewById(R.id.textViewArtist);
        textViewDuration = findViewById(R.id.textViewDuration);
        elapsedTimeTextView = findViewById(R.id.textViewElapsedTime);
        kProgressHUD = new KProgressHUD(StreamingMp3Player.this);

        String musicNamee = new GlobalData().getMusicName();
        String musicauthorr = new GlobalData().getMusicSecondName();
        String textViewDurationn = new GlobalData().getStreamMusicTime();
        musicName.setText(musicNamee);
        musicAuthor.setText(musicauthorr);
        textViewDuration.setText(textViewDurationn);
        buttonPlayPause.setOnClickListener(this);

        seekBarProgress = (SeekBar) findViewById(R.id.SeekBarTestPlay);
        seekBarProgress.setMax(99);
        seekBarProgress.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ButtonTestPlayPause) {

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                buttonPlayPause.setImageResource(R.drawable.ic_pause_circle);
            } else {
                mediaPlayer.pause();
                buttonPlayPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.SeekBarTestPlay) {

            SeekBar sb = (SeekBar) v;
            int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
            mediaPlayer.seekTo(playPositionInMillisecconds);

        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        buttonPlayPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBarProgress.setSecondaryProgress(percent);
    }

    class StreamingMp3 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kProgressHUD = KProgressHUD.create(StreamingMp3Player.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Song Loading ...")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setAutoDismiss(true)
                    .setDimAmount(0.5f)
                    .show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            try {

                mediaPlayer.setDataSource(new GlobalData().getTextSongURL());
                mediaPlayer.prepare();

                mediaFileLengthInMilliseconds = mediaPlayer.getDuration();

                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    buttonPlayPause.setImageResource(R.drawable.ic_pause_circle);
                } else {
                    mediaPlayer.pause();
                    buttonPlayPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            primarySeekBarProgressUpdater();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            kProgressHUD.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }



}

