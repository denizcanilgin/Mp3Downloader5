package com.freemusicdownloader.mp3downloader;

import android.net.Uri;

public class GlobalData {

    public static Uri uri;
    public static String musicName;

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
