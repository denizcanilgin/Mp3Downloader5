package com.freemusicdownloader.mp3downloader;

import android.media.MediaPlayer;
import android.net.Uri;

public class GlobalUri {

    public static Uri uri;
    public static MediaPlayer mediaPlayer;

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        GlobalUri.mediaPlayer = mediaPlayer;
    }

    public static Uri getUri() {
        return uri;
    }

    public static void setUri(Uri uri) {
        GlobalUri.uri = uri;
    }
}
