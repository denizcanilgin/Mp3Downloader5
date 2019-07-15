package com.freemusicdownloader.mp3downloader;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class GlobalClass extends Application {

    public static final String PREFS_NAME = "FAV_MUSICS";
    public static final String PREFS_SAVED = "FAV_MUSICS_SAVED";
    private List<FavMusic> favMusicList;

    public GlobalClass() {
        super();
    }

    public void addFavMusic (Context context, FavMusic favMusic) {
        List<FavMusic> favMusicList = getSavedFavSongs(context);
        if (favMusicList == null)
            favMusicList = new ArrayList<FavMusic>();
        favMusicList.add(favMusic);
        saveFavSongs(context, favMusicList);
    }


    public void removeFavSong(Context context, FavMusic favMusic) {
        ArrayList<FavMusic> favMusicsList = getSavedFavSongs(context);
        if (favMusicsList != null) {
            favMusicsList.remove(favMusic);
            saveFavSongs(context, favMusicsList);
        }
    }

    public void saveFavSongs(Context context, List<FavMusic> favMusicList) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson(); //create a json
        String jsonFavSongsList = gson.toJson(favMusicList); //convert list to json
        editor.putString(PREFS_SAVED, jsonFavSongsList); // store as json in shared pref
        editor.commit(); // commit

    }


    public ArrayList<FavMusic> getSavedFavSongs(Context context) {
        SharedPreferences settings;
        List<FavMusic> favMusicList;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(PREFS_SAVED)) {
            String jsonFavSongs = settings.getString(PREFS_SAVED, null);
            Gson gson = new Gson();
            FavMusic[] favMusicItems = gson.fromJson(jsonFavSongs,
                    FavMusic[].class);

            favMusicList = Arrays.asList(favMusicItems);
            favMusicList = new ArrayList<FavMusic>(favMusicList);
        } else
            return null;

        return (ArrayList<FavMusic>) favMusicList;
    }

    public List<FavMusic> getGlobalList() {
        return this.favMusicList;
    }

    public void setGlobalList(List<FavMusic> value) {
        this.favMusicList = value;
    }


}
