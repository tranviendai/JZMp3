package com.example.mp3testing.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.mp3testing.Category.Categories;
import com.example.mp3testing.Category.CategoriesAdapter;
import com.example.mp3testing.Model.SongModel;

import java.util.ArrayList;

public class DbSong {
    String dbName = "Mp3OfflineDB";
    String tbSong = "tbSong";
    Context context;

    public DbSong(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDB(){
        return context.openOrCreateDatabase(dbName,Context.MODE_PRIVATE,null);
    }

    public void closeDB(SQLiteDatabase db){
        db.close();
    }

    public void createTable(){
        SQLiteDatabase db = openDB();
        String sql = "create table if not exists " + tbSong + "(" +
                "id integer primary key autoincrement, " +
                "path text, " + "nameMusic text, " + "singer text,album text, " +
                "duration text, " + "idCategory integer references tbCategory(id))";
        db.execSQL(sql);
        closeDB(db);
    }
    public ArrayList<SongModel> getAllSong(){
        ArrayList<SongModel> tmp = new ArrayList<>();
        SQLiteDatabase db = openDB();
        String sql = "SELECT * FROM " + tbSong;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String path = cursor.getString(1);
            String nameMusic = cursor.getString(2);
            String singer = cursor.getString(3);
            String duration = cursor.getString(4);
            int idCategory = cursor.getInt(5);
            String album = cursor.getString(6);
            tmp.add(new SongModel(id, path,nameMusic,singer,duration,idCategory,album));
        }
        closeDB(db);
        return tmp;
    }

    public SongModel insertSong(SongModel song){
        SQLiteDatabase db = openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("path",song.getPath());
        contentValues.put("nameMusic",song.getNameMusic());
        contentValues.put("singer",song.getSinger());
        contentValues.put("duration", song.getDuration());
        contentValues.put("idCategory", song.getIdCategory());
        contentValues.put("album", song.getAlbum());
        closeDB(db);
        return song;
    }
    public void removeSong(boolean song){
        SQLiteDatabase db = openDB();
        closeDB(db);
    }

}
