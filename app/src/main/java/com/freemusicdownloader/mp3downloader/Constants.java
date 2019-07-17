package com.freemusicdownloader.mp3downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constants {
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
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_album_art, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}
