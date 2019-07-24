package com.example.checfsmenu;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Objects;

public class Food implements Serializable {

    private String name;
    private Bitmap image;


    public Food(String name,Bitmap image) {
        this.name = name;
        this.image=image;
    }

    public Food(String name) {
        this.name = name;
        this.image = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

   // public int searchMethod(ArrayList<Food> foodObjectList){
     //       String name=foodObjectList.contain
    //}
    //TODO search method w.r.t name
    // return index of searched food name

    // list.get(returnedIndex)

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", image=" + image +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return getName().equals(food.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
