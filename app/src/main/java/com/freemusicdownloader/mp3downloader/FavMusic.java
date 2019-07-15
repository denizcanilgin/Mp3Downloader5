package com.freemusicdownloader.mp3downloader;

public class FavMusic {

    public String MusicName;
    public String MusicURL;
    public String MusicAuthor;
    public String MusicDuration;

    public FavMusic(String musicName, String musicURL, String musicAuthor, String musicDuration) {
        MusicName = musicName;
        MusicURL = musicURL;
        MusicAuthor = musicAuthor;
        MusicDuration = musicDuration;
    }

    public String getMusicName() {
        return MusicName;
    }

    public void setMusicName(String musicName) {
        MusicName = musicName;
    }

    public String getMusicURL() {
        return MusicURL;
    }

    public void setMusicURL(String musicURL) {
        MusicURL = musicURL;
    }

    public String getMusicAuthor() {
        return MusicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        MusicAuthor = musicAuthor;
    }

    public String getMusicDuration() {
        return MusicDuration;
    }

    public void setMusicDuration(String musicDuration) {
        MusicDuration = musicDuration;
    }
}
