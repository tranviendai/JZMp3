package com.example.mp3testing.PlayerIndex;

import android.content.Context;
import android.media.MediaPlayer;

public class MyIndex {
    static MediaPlayer instance;
    public static Boolean shuffle = false, repeat = false;

    public  static MediaPlayer getInstance()
    {
        if(instance==null)
        {
            instance = new MediaPlayer();
        }
        return instance;
    }
    public  static  int currentIndex=-1;
}
