package com.example.mp3testing.Model;


import android.graphics.Bitmap;

import java.io.Serializable;

public class SongModel implements Serializable {
    int id;
    String path;
    String nameMusic,singer,duration;
    int idCategory;
    String album;

    public SongModel(){}
    public SongModel(int id, String path, String nameMusic, String singer, String duration,int idCategory, String album) {
        this.id = id;
        this.path = path;
        this.nameMusic = nameMusic;
        this.singer = singer;
        this.duration = duration;
        this.idCategory = idCategory;
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNameMusic() {
        return nameMusic;
    }

    public void setNameMusic(String nameMusic) {
        this.nameMusic = nameMusic;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

}


