package com.freemusicdownloader.mp3downloader;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.IOException;

public class MediaPlaybackService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    public static final String MPS_MESSAGE = "com.freemusicdownloader.mp3downloader.MediaPlaybackService.MESSAGE";
    public static final String MPS_RESULT = "com.freemusicdownloader.mp3downloader.MediaPlaybackService.RESULT";
    public static final String MPS_COMPLETED = "com.freemusicdownloader.mp3downloader.MediaPlaybackService.COMPLETED";

    MediaPlayer mMediaPlayer = null;
    Uri file;
    RemoteViews bigViews, views;

    private NotificationManager notifManager;
    NotificationCompat.Builder mBuilder;
    String NOTIFICATION_CHANNEL_ID = "com.freemusicdownloader.mp3downloader";

    private static final int NOTIF_ID = 1234;

    public class IDBinder extends Binder {

        MediaPlaybackService getService() {
            return MediaPlaybackService.this;
        }
    }

    IDBinder idBinder = new IDBinder();

    LocalBroadcastManager broadcastManager;
    MediaPlaybackService playbackService;

    private NotificationManagerCompat notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        views = new RemoteViews(getPackageName(), R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

        notificationManager = NotificationManagerCompat.from(this);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return idBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stop();
        return super.onUnbind(intent);
    }

    public void init(Uri file) {
        this.file = file;

        pause();

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(getApplicationContext(), file);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        Thread updateThread = new Thread(sendUpdates);
        updateThread.start();
    }

    private Runnable sendUpdates = new Runnable() {
        @Override
        public void run() {
            while (mMediaPlayer != null) {
                SystemClock.sleep(500);
                sendElapsedTime();
                try {
                    Thread.sleep(500);
                    MediaPlaybackService mediaPlaybackService = new GlobalData().getMediaPlaybackService();

                    if (mediaPlaybackService.isPlaying()) {
                        bigViews.setImageViewResource(R.id.status_bar_pause, R.drawable.ic_pause_circle_filled_black_24dp);
                        views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_circle_filled_black_24dp);
                        notificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mBuilder.build());

                    } else {
                        bigViews.setImageViewResource(R.id.status_bar_pause, R.drawable.ic_play_circle_filled_black_24dp);
                        views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_circle_filled_black_24dp);
                        notificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mBuilder.build());
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public void pause() {
        if (mMediaPlayer != null)
            mMediaPlayer.pause();
    }

    public void play() {
        if (mMediaPlayer != null)
            mMediaPlayer.start();
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            file = null;
        }
    }

    public void seekTo(int msec) {
        mMediaPlayer.seekTo(msec);
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public Uri getFile() {
        return file;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Intent intent = new Intent(MPS_COMPLETED);
        broadcastManager.sendBroadcast(intent);
    }

    private void sendElapsedTime() {
        // Utilisation du BroadcastReceiver local pour envoyer la durée passée
        Intent intent = new Intent(MPS_RESULT);
        if (mMediaPlayer != null)
            intent.putExtra(MPS_MESSAGE, mMediaPlayer.getCurrentPosition());

        broadcastManager.sendBroadcast(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            showNotification();
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i("click_event", "Clicked Previous");
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            play();
            Log.i("click_event", "Clicked Play");
        } else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)) {

            Log.i("click_event", "Clicked Pause");
            playbackService = new GlobalData().getMediaPlaybackService();
            Boolean a = playbackService.isPlaying();
            Log.i("infoooPlay", "" + a);
            if (playbackService.isPlaying()) {
                bigViews.setImageViewResource(R.id.status_bar_pause, R.drawable.ic_play_circle_filled_black_24dp);
                views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_circle_filled_black_24dp);
                pause();
            } else {
                bigViews.setImageViewResource(R.id.status_bar_pause, R.drawable.ic_pause_circle_filled_black_24dp);
                views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_circle_filled_black_24dp);
                play();
            }

            notificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mBuilder.build());


        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i("click_event", "Clicked Next");
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i("click_event", "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();

            stop();
            stopForeground(true);
            notificationManager.cancel(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE);
            System.exit(0);
        }

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification() {

        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));
        views.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));


        Intent notificationIntent = new Intent(this, MusicPlayerActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, MediaPlaybackService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, MediaPlaybackService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent pauseIntent = new Intent(this, MediaPlaybackService.class);
        pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0,
                pauseIntent, 0);

        Intent nextIntent = new Intent(this, MediaPlaybackService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, MediaPlaybackService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_play, pendingIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pendingIntent);

        views.setOnClickPendingIntent(R.id.status_bar_play, ppauseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_pause, ppauseIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
//
//        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
//        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        String songName = new GlobalData().getMusicName();
        Log.i("asdasd", songName);
        views.setTextViewText(R.id.status_bar_track_name, songName);
        bigViews.setTextViewText(R.id.status_bar_track_name, songName);

        views.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
        bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");

        String id = "channel_id";
        String title = "Channel_title";
        Intent intent;

        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = notifManager.getNotificationChannel(id);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(id, title, importance);
                notificationChannel.setSound(null, null);
                notifManager.createNotificationChannel(notificationChannel);
            }
            notificationChannel.setSound(null, null);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            mBuilder = new NotificationCompat.Builder(getApplicationContext(), id);
            mBuilder.setSmallIcon(R.drawable.ic_music_note)
                    .setAutoCancel(true)
                    .setCustomContentView(views)
                    .setCustomBigContentView(bigViews)
                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true);

            Notification notification = mBuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

        } else {

            mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_SERVICE)
                    .setChannelId(NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_music_note)
                    .setCustomContentView(views)
                    .setCustomBigContentView(bigViews)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true);


            Notification notification = mBuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
        }


    }


}
