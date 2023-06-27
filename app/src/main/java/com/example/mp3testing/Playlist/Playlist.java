package com.example.mp3testing.Playlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3testing.ActiveMusic;
import com.example.mp3testing.Category.CategoriesAdapter;
import com.example.mp3testing.Model.SongModel;
import com.example.mp3testing.PlayerIndex.MyIndex;
import com.example.mp3testing.PlayerIndex.SharePref;
import com.example.mp3testing.R;
import com.example.mp3testing.Settings;
import com.example.mp3testing.Sqlite.DbSong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Playlist extends AppCompatActivity implements PlaylistAdapter.itemClickListener {
    RecyclerView rvPlaylist;
    TextView txNamePlaylist;
    static PlaylistAdapter playlistAdapter;
    public static int tempCateID;
    public static int position;
    public static DbSong dbPlaylist;
    View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        view = findViewById(R.id.playlist);
        if(Settings.switchColor){
            view.setBackgroundResource(R.color.black);
        }
        else{
            view.setBackgroundResource(R.color.bg_main);
        }
        tempCateID = getIntent().getIntExtra("idPlaylist",position);
        txNamePlaylist = findViewById(R.id.textView);
        rvPlaylist = findViewById(R.id.rvPlaylistSongs);
        txNamePlaylist.setText(getIntent().getStringExtra("namePlaylist"));
        playlistAdapter.playlists = SharePref.shareReadSong(this);
        if(playlistAdapter.playlists == null){
            playlistAdapter.playlists = new ArrayList<>();
        }
//        playlistAdapter.playlists.addAll(dbPlaylist.getAllSong());
        playlistAdapter = new PlaylistAdapter(PlaylistAdapter.playlists, this::onItemClick, getApplicationContext(), CategoriesAdapter.categories);
        rvPlaylist.setAdapter(playlistAdapter);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(Playlist.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onItemClick(int pos, SongModel song) {
        MyIndex.getInstance();
        MyIndex.currentIndex = pos;
        Intent intent = new Intent(Playlist.this, ActiveMusic.class);
        intent.putExtra("musics", PlaylistAdapter.playlists);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView searchView =(SearchView) menu.findItem(R.id.searchList).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                playlistAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                playlistAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.sort){
            Collections.sort(playlistAdapter.songsFilter, new Comparator<SongModel>() {
                @Override
                public int compare(SongModel left, SongModel right) {
                    return left.getNameMusic().compareTo(right.getNameMusic());
                }
            });
            playlistAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}
