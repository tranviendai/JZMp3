package com.example.mp3testing.Model;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    int id;
    String nameCategory;

    public CategoryModel(){}
    public CategoryModel(int id, String nameCategory) {
        this.id = id;
        this.nameCategory = nameCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
}
