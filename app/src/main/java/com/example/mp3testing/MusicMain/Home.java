package com.example.mp3testing.MusicMain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mp3testing.ActiveMusic;
import com.example.mp3testing.Model.SongModel;
import com.example.mp3testing.PlayerIndex.MyIndex;
import com.example.mp3testing.R;
import com.example.mp3testing.Settings;

import java.io.File;
import java.util.ArrayList;

public class Home extends Fragment implements MusicAdapter.clickItemListener {
    RecyclerView rvMusic;
    ArrayList<SongModel> music;
    MusicAdapter musicAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        music = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(Settings.switchColor){
            view.setBackgroundResource(R.color.black);
        }
        else{
            view.setBackgroundResource(R.color.bg_main);
        }
        rvMusic = view.findViewById(R.id.rvMusic);
        scanMp3();
        musicAdapter = new MusicAdapter(this::onItemClick,music,getContext());
        rvMusic.setAdapter(musicAdapter);
        rvMusic.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    public void scanMp3(){
        if(checkPermission() == false){
            requestPermission();
            return;
        }
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.IS_DOWNLOAD,
                MediaStore.Audio.Media.ALBUM
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";
        Cursor cursor = getActivity().getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
        while(cursor.moveToNext()){
            SongModel song = new SongModel(
                    cursor.getInt(4),
                    cursor.getString(1),
                    cursor.getString(0),
                    cursor.getString(3),
                    cursor.getString(2),0,
                    cursor.getString(5));
            if(new File(song.getPath()).exists())
                music.add(song);
        }
    }
    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }
    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(getContext(),"SCAN",Toast.LENGTH_SHORT).show();
        }else
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(rvMusic!=null){
            rvMusic.setAdapter(new MusicAdapter(this::onItemClick, music,getActivity()));
        }
    }

    @Override
    public void onItemClick(int position, SongModel music) {
        MyIndex.getInstance();
        MyIndex.currentIndex = position;
        Intent intent = new Intent(getContext(), ActiveMusic.class);
        intent.putExtra("musics", this.music);
        startActivity(intent);
    }
}