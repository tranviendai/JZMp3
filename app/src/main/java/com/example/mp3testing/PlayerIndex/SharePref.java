package com.example.mp3testing.PlayerIndex;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mp3testing.Model.SongModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

public class SharePref {
    private  static final String MyShare="MyShare";
    private Context context;
    public SharePref(Context context)
    {
        this.context=context;
    }

    public static void shareSong(Context context, ArrayList<SongModel> song){
        Gson gson = new Gson();
        String json = gson.toJson(song);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(MyShare, json);
        editor.apply();
    }
    public static ArrayList<SongModel> shareReadSong(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String json = pref.getString(MyShare,"");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SongModel>>(){}.getType();
        ArrayList<SongModel> songs = gson.fromJson(json,type);
        return songs;
    }

}
