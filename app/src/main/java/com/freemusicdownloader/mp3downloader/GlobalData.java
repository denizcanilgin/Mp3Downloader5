package com.freemusicdownloader.mp3downloader;

import android.content.Intent;
import android.net.Uri;

public class GlobalData {

    public static Uri uri;
    public static String musicName;
    public static Intent ıntent;
    public static MediaPlaybackService mediaPlaybackService;

    public static MediaPlaybackService getMediaPlaybackService() {
        return mediaPlaybackService;
    }

    public static void setMediaPlaybackService(MediaPlaybackService mediaPlaybackService) {
        GlobalData.mediaPlaybackService = mediaPlaybackService;
    }

    public static Intent getIntent() {
        return ıntent;
    }

    public static void setIntent(Intent ıntent) {
        GlobalData.ıntent = ıntent;
    }

    public static String getMusicName() {
        return musicName;
    }

    public static void setMusicName(String musicName) {
        GlobalData.musicName = musicName;
    }


    public static Uri getUri() {
        return uri;
    }

    public static void setUri(Uri uri) {
        GlobalData.uri = uri;
    }
}
