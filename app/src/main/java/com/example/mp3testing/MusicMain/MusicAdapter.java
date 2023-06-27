package com.example.mp3testing.MusicMain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mp3testing.Category.CategoriesAdapter;
import com.example.mp3testing.Model.CategoryModel;
import com.example.mp3testing.Model.SongModel;
import com.example.mp3testing.PlayerIndex.SharePref;
import com.example.mp3testing.Playlist.Playlist;
import com.example.mp3testing.Playlist.PlaylistAdapter;
import com.example.mp3testing.R;
import com.example.mp3testing.Settings;
import com.example.mp3testing.Sqlite.DbSong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> implements Filterable {
    clickItemListener itemClick;
    static ArrayList<SongModel> musics;
    ArrayList<SongModel> musicsFilter;
    Context context;
    public MusicAdapter(clickItemListener itemClick, ArrayList<SongModel> musics, Context context) {
        this.itemClick = itemClick;
        this.musics = musics;
        this.context = context;
        this.musicsFilter = musics;
//        Playlist.dbPlaylist = new DbSong(context);
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_row, parent, false);
        if(Settings.switchColor){
            view.setBackgroundResource(R.color.black);
        }
        else{
            view.setBackgroundResource(R.color.bg_main);
        }
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        SongModel music = musicsFilter.get(position);
        //Load album!
//        byte[] image = getAlbum(music.getAlbum());
//        if(image!=null){
//            Glide.with(context).asBitmap().load(image).into(holder.imgFlag);
//        }
//        else{
//            Glide.with(context).load(R.drawable.mp3logo);
//        }
        holder.musicName.setText(music.getNameMusic());
        holder.musicSinger.setText(music.getSinger());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onItemClick(position, music);
            }
        });
        holder.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Select Playlist");
                ArrayList<CategoryModel> categories = CategoriesAdapter.categories;
                ArrayList<String> list = new ArrayList<>();
                for (CategoryModel data : categories) {
                    list.add((data.getNameCategory()));
                }
                CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
                builder.setMultiChoiceItems(charSequences, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            music.setIdCategory(categories.get(which).getId());
                            PlaylistAdapter.playlists.add(music);
                            SharePref.shareSong(context.getApplicationContext(), PlaylistAdapter.playlists);
//                            Playlist.dbPlaylist.insertSong(music);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Add Song: " + music.getNameMusic() + "-> " + categories.get(which).getNameCategory(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            PlaylistAdapter.playlists.remove(music);
                            SharePref.shareSong(context.getApplicationContext(), PlaylistAdapter.playlists);
                            notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Đóng tiến trình", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    musicsFilter = musics;
                } else {
                    ArrayList<SongModel> filteredArrayList = new ArrayList<SongModel>();
                    for (int i = 0; i < musics.size(); i++) {
                        if (musics.get(i).getNameMusic().toLowerCase().contains(charString.toLowerCase())) {
                            filteredArrayList.add(musics.get(i));
                        }
                    }
                    musicsFilter = filteredArrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = musicsFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                musicsFilter = (ArrayList<SongModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFlag;
        TextView musicName, musicSinger;
        Button btAdd;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            musicName = itemView.findViewById(R.id.musicName);
            musicSinger = itemView.findViewById(R.id.musicSinger);
            btAdd = itemView.findViewById(R.id.btAdd);
        }
    }

//    public byte[] getAlbum(String uri){
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        if (Build.VERSION.SDK_INT >= 14){
//            try {
//                retriever.setDataSource(uri, new HashMap<String, String>());
//            } catch (RuntimeException ex) {}
//        }
//        else {
//            retriever.setDataSource(uri);
//        }
//        byte[] art = retriever.getEmbeddedPicture();
//        try {
//            retriever.release();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return art;
//    }

    interface clickItemListener {
        void onItemClick(int position, SongModel music);
    }
}