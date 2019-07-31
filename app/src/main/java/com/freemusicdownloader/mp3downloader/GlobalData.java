package com.freemusicdownloader.mp3downloader;

import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

public class GlobalData {

    public static Uri uri;
    public static String musicName;
    public static Intent ıntent;
    public static MediaPlaybackService mediaPlaybackService;
    public static ArrayList<String> songList;
    public static int songListIndex;

    public static String GENRES_DECODER;

    public static String getGenresDecoder() {
        return GENRES_DECODER;
    }

    public static void setGenresDecoder(String genresDecoder) {
        GENRES_DECODER = genresDecoder;
    }

    public static int getSongListIndex() {
        return songListIndex;
    }

    public static void setSongListIndex(int songListIndex) {
        GlobalData.songListIndex = songListIndex;
    }

    public static ArrayList<String> getSongList() {
        return songList;
    }

    public static void setSongList(ArrayList<String> songList) {
        GlobalData.songList = songList;
    }

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
