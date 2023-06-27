package com.example.mp3testing.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.mp3testing.Model.CategoryModel;

import java.util.ArrayList;

public class DbCategory {
    String dbName = "Mp3OfflineDB";
    static String tbCategory = "tbCategory";
    Context context;

    public DbCategory(Context context) {
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
        String sql = "create table if not exists " + tbCategory + "(" +
                 "id integer primary key autoincrement, " + "nameCategory text )";
        db.execSQL(sql);
        closeDB(db);
    }

    public ArrayList<CategoryModel> getAllCategory(){
        ArrayList<CategoryModel> tmp = new ArrayList<>();
        SQLiteDatabase db = openDB();
        String sql = "SELECT * FROM " + tbCategory;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nameCategory = cursor.getString(1);
            tmp.add(new CategoryModel(id,nameCategory));
        }
        closeDB(db);
        return tmp;
    }

    public void insertCategory(CategoryModel playlist){
        SQLiteDatabase db = openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nameCategory", playlist.getNameCategory());
        if(db.insert(tbCategory,null,contentValues) > 0){
            Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show();
        }
        closeDB(db);
    }

    public void deleteCategory(CategoryModel playlist){
        SQLiteDatabase db = openDB();
        if(db.delete(tbCategory,"id =" + playlist.getId(),null) > 0){
            Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show();
        }
        closeDB(db);
    }

    public void updateCategory(CategoryModel playlist) {
        SQLiteDatabase db = openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nameCategory", playlist.getNameCategory());
        if(db.update(tbCategory,contentValues,"id = " + playlist.getId(),null) > 0){
            Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show();
        }
        closeDB(db);
    }
}
