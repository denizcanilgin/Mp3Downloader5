package com.freemusicdownloader.mp3downloader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class GlobalData {

    public static Uri uri;
    public static String musicName;
    public static Intent ıntent;
    public static MediaPlaybackService mediaPlaybackService;
    public static ArrayList<String> songList;
    public static int songListIndex;
    public static int Counter;
    public static Boolean clickNext;
    public static Boolean clickPrevious;
    public static Bitmap songPicture;
    public static boolean repeatSong = false;
    public static boolean isGalleryUpdate;
    public static boolean isFinished;

    public static boolean isIsFinished() {
        return isFinished;
    }

    public static void setIsFinished(boolean isFinished) {
        GlobalData.isFinished = isFinished;
    }

    public static boolean isIsGalleryUpdate() {
        return isGalleryUpdate;
    }

    public static void setIsGalleryUpdate(boolean isGalleryUpdate) {
        GlobalData.isGalleryUpdate = isGalleryUpdate;
    }

    public static int songDuration;

    public static boolean isRepeatSong() {
        return repeatSong;
    }

    public static int getSongDuration() {
        return songDuration;
    }

    public static void setSongDuration(int songDuration) {
        GlobalData.songDuration = songDuration;
    }

    public static void setRepeatSong(boolean repeatSong) {
        GlobalData.repeatSong = repeatSong;
    }

    public static Bitmap getSongPicture() {
        return songPicture;
    }

    public static void setSongPicture(Bitmap songPicture) {
        GlobalData.songPicture = songPicture;
    }

    public static Boolean getClickNext() {
        return clickNext;
    }

    public static void setClickNext(Boolean clickNext) {
        GlobalData.clickNext = clickNext;
    }

    public static Boolean getClickPrevious() {
        return clickPrevious;
    }

    public static void setClickPrevious(Boolean clickPrevious) {
        GlobalData.clickPrevious = clickPrevious;
    }

    public static int getCounter() {
        return Counter;
    }

    public static void setCounter(int counter) {
        Counter = counter;
    }

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
