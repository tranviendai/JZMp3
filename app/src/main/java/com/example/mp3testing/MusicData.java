package com.example.mp3testing;

import android.app.Application;

import com.example.mp3testing.Model.CategoryModel;
import com.example.mp3testing.Model.SongModel;
import com.example.mp3testing.PlayerIndex.DataLocal;
import com.example.mp3testing.Sqlite.DbCategory;
import com.example.mp3testing.Sqlite.DbSong;

import java.util.ArrayList;

public class MusicData extends Application {
    DbCategory db;
    DbSong dbSong;
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocal.init(getApplicationContext());

        db = new DbCategory(this);
        db.createTable();
        dbSong = new DbSong(this);
        dbSong.createTable();
    }
}
