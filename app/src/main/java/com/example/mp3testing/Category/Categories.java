package com.example.mp3testing.Category;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mp3testing.Model.CategoryModel;
import com.example.mp3testing.PlayerIndex.MyIndex;
import com.example.mp3testing.Playlist.Playlist;
import com.example.mp3testing.R;
import com.example.mp3testing.Settings;
import com.example.mp3testing.Sqlite.DbCategory;

import java.util.ArrayList;


public class Categories extends Fragment implements CategoriesAdapter.clickItemListener {
    RecyclerView rvCategory;
    CategoriesAdapter categoryAdapter;
    public static ArrayList<CategoryModel> categories;
    DbCategory db;
    public static ArrayList<CategoryModel> tempCate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        categories = new ArrayList<>();
        db = new DbCategory(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
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
        categories.addAll(db.getAllCategory());
        rvCategory = view.findViewById(R.id.rvCategory);
        categoryAdapter = new CategoriesAdapter(categories, getActivity(), this::onItemClick);
        rvCategory.setAdapter(categoryAdapter);
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
        tempCate = categories;
    }

    public void initData() {
        categories.clear();
        categories.addAll(db.getAllCategory());
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.category_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnuInsert) {
            CategoryModel category = new CategoryModel();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Insert Playlist");
            builder.setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.category_input, null);
            EditText edtName = view.findViewById(R.id.edtName);
            builder.setView(view);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    category.setNameCategory(edtName.getText().toString());
                    db.insertCategory(category);
                    categories.add(category);
                    categoryAdapter.notifyDataSetChanged();
                    initData();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        if (categories.size() > 0) {
            initData();
        }
        super.onResume();
    }

    @Override
    public void onItemClick(int position, CategoryModel playlist) {
        MyIndex.getInstance();
        MyIndex.currentIndex = position;
        Intent intent = new Intent(getActivity(), Playlist.class);
        intent.putExtra("idPlaylist", playlist.getId());
        intent.putExtra("namePlaylist",playlist.getNameCategory());
        startActivity(intent);
    }
}