package com.example.mp3testing.Category;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3testing.Model.CategoryModel;
import com.example.mp3testing.Playlist.PlaylistAdapter;
import com.example.mp3testing.R;
import com.example.mp3testing.Settings;
import com.example.mp3testing.Sqlite.DbCategory;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {
    public static ArrayList<CategoryModel> categories = new ArrayList<>();
    Context context;
    clickItemListener itemClick;
    public static CategoryModel tempCategory = new CategoryModel();
    public static DbCategory db;

    public CategoriesAdapter(ArrayList<CategoryModel> categories, Context context, clickItemListener itemClick) {
        this.categories = categories;
        this.context = context;
        this.itemClick = itemClick;
        db = new DbCategory(context.getApplicationContext());
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_gird, parent, false);
        if(Settings.switchColor){
            view.setBackgroundResource(R.color.black);
        }
        else{
            view.setBackgroundResource(R.color.bg_main);
        }
        return new CategoryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categories.get(position);
        holder.nameCategory.setText(category.getNameCategory());
        if(Settings.switchColor){
            holder.imgTheme.setAlpha((float) 0.3);
        }
        else{
            holder.imgTheme.setAlpha((float) 0.1);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onItemClick(position, category);
            }
        });
        holder.igUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Update Playlist");
                builder.setCancelable(false);
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View view = inflater.inflate(R.layout.category_input, null);
                final EditText edtName =view.findViewById(R.id.edtName);
                edtName.setText(category.getNameCategory());
                builder.setView(view);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        category.setNameCategory(edtName.getText().toString());
                        db.updateCategory(category);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        holder.igDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete Playlist");
                builder.setCancelable(false);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        categories.remove(category);
                        db.deleteCategory(category);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialogRemove = builder.create();
                alertDialogRemove.show();
            }
        });
        tempCategory = category;
    }
    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameCategory;
        ImageView igUpdate,igDelete, imgTheme;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategory = itemView.findViewById(R.id.txNameCategory);
            igUpdate = itemView.findViewById(R.id.igUpdate);
            igDelete = itemView.findViewById(R.id.igDelete);
            imgTheme = itemView.findViewById(R.id.imgTheme);
        }
    }

    interface clickItemListener {
        void onItemClick(int position, CategoryModel category);
    }
}