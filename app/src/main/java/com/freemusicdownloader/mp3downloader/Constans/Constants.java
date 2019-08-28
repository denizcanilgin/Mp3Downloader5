package com.freemusicdownloader.mp3downloader.Constans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.freemusicdownloader.mp3downloader.R;

public class Constants {

    public static String GENRES_ACOUSTIC = "aHR0cDovL21wM3BuLmluZm8vdG9wL2Fjb3VzdGljX3ZvY2FsLw";
    public static String GENRES_BASSDRUM = "aHR0cHM6Ly90dm94eS5uZXQvdG9wL2RydW1fYmFzcy8";
    public static String GENRES_JAZZ = "aHR0cDovL21wM3BuLmluZm8vdG9wL2phenpfYmx1ZXMv";
    public static String GENRES_CLASSICAL = "aHR0cDovL21wM3BuLmluZm8vdG9wL2NsYXNzaWNhbC8";
    public static String GENRES_RELAX = "aHR0cDovL21wM3BuLmluZm8vdG9wL3JlbGF4Lw";
    public static String GENRES_ROCK = "aHR0cDovL21wM3BuLmluZm8vdG9wL3JvY2sv";
    public static String GENRES_METAL = "aHR0cDovL21wM3BuLmluZm8vdG9wL21ldGFsLw";
    public static String GENRES_HOMEPAGES = "aHR0cHM6Ly90dm94eS5uZXQvc2VhcmNoL3MvZi9yaWhhbm5hLw";




    public interface ACTION {
        public static String MAIN_ACTION = "com.freemusicdownloader.mp3downloader.action.main";
        public static String INIT_ACTION = "com.freemusicdownloader.mp3downloader.action.init";
        public static String PREV_ACTION = "com.freemusicdownloader.mp3downloader.action.prev";
        public static String PLAY_ACTION = "com.freemusicdownloader.mp3downloader.action.play";
        public static String PAUSE_ACTION = "com.freemusicdownloader.mp3downloader.action.pause";
        public static String NEXT_ACTION = "com.freemusicdownloader.mp3downloader.action.next";
        public static String STARTFOREGROUND_ACTION = "com.freemusicdownloader.mp3downloader.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.freemusicdownloader.mp3downloader.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 1907;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_album_white_400_128dp, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }


}
