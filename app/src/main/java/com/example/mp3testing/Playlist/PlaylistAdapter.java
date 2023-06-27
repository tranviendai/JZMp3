package com.example.mp3testing.Playlist;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.example.mp3testing.Category.CategoriesAdapter;
import com.example.mp3testing.Model.CategoryModel;
import com.example.mp3testing.Model.SongModel;
import com.example.mp3testing.MusicMain.MusicAdapter;
import com.example.mp3testing.PlayerIndex.SharePref;
import com.example.mp3testing.R;
import com.example.mp3testing.Settings;
import com.example.mp3testing.Sqlite.DbSong;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistVH> implements Filterable {
    public static ArrayList<SongModel> playlists = new ArrayList<>();
    public static ArrayList<SongModel> songsFilter;
    itemClickListener itemClick;
    Context context;

    public PlaylistAdapter(ArrayList<SongModel> playlists, itemClickListener itemClick, Context context,ArrayList<CategoryModel> category) {
        this.playlists = playlists;
        this.itemClick = itemClick;
        this.context = context;
        this.songsFilter = playlists;
        CategoriesAdapter.categories = category;
    }

    @NonNull
    @Override
    public PlaylistVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_row, parent, false);
        if(Settings.switchColor){
            view.setBackgroundResource(R.color.black);
        }
        else{
            view.setBackgroundResource(R.color.bg_main);
        }
        return new PlaylistVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistVH holder, @SuppressLint("RecyclerView") int position) {
        SongModel playList = songsFilter.get(position);
        if(Playlist.tempCateID == playList.getIdCategory()){
            holder.musicName.setText(playList.getNameMusic());
            holder.musicSinger.setText(playList.getSinger());
            holder.btRemove.setText("-");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(position,playList);
                }
            });
            holder.btRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaylistAdapter.playlists.remove(playList);
//                    Playlist.dbPlaylist.removeSong(songsFilter.remove(playList));
                    SharePref.shareSong(context.getApplicationContext(), PlaylistAdapter.playlists);
                    notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "Delete Song: " + playList.getNameMusic(),Toast.LENGTH_SHORT).show();
                }
            });}
       else{
            holder.musicSinger.setHeight(0);
            holder.imgFlag.setVisibility(View.GONE);
            holder.btRemove.setVisibility(View.GONE);
            holder.musicName.setHeight(0);
            holder.musicSinger.setWidth(0);
            holder.btRemove.setWidth(0);
            holder.musicName.setWidth(0);
        }
    }


    @Override
    public int getItemCount() {
        return songsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new SongFilter();
    }
    class SongFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            if (charString.isEmpty()){
                songsFilter = playlists;
            }
            else
            {
                List<SongModel> filteredList = new ArrayList<>();
                for (SongModel r: playlists){
                    if(r.getNameMusic().toLowerCase().contains(charString.toLowerCase()) ||
                            r.getSinger().contains(constraint) || r.getDuration().contains(constraint))
                    {
                        filteredList.add(r);
                    }
                }
                songsFilter = (ArrayList<SongModel>) filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = songsFilter;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            songsFilter = (ArrayList<SongModel>) results.values;
            notifyDataSetChanged();
        }
    }

    class PlaylistVH extends RecyclerView.ViewHolder{
        ImageView imgFlag;
        TextView musicName, musicSinger,namePlaylist;
        Button btRemove;
        public PlaylistVH(@NonNull View itemView) {
            super(itemView);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            musicName = itemView.findViewById(R.id.musicName);
            musicSinger = itemView.findViewById(R.id.musicSinger);
            namePlaylist = itemView.findViewById(R.id.textView);
            btRemove = itemView.findViewById(R.id.btAdd);
        }
    }
    interface itemClickListener{
        void onItemClick(int pos,SongModel song);
    }
}