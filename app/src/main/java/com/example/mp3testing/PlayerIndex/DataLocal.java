package com.example.mp3testing.PlayerIndex;

import android.content.Context;

public class DataLocal {
    private static final String PREF_FIRST_INSTALL="PREF_FIRST_INSTALL";

    private static  DataLocal instance;
    private SharePref sharePref;

    public static void init(Context context)
    {
        instance = new DataLocal();
        instance.sharePref = new SharePref(context);
    }

}
