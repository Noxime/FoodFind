package net.noxcorp.noxim.foodfind;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Noxim on 21.6.2016.
 */
public class Dish {

    public String  name;
    public String  description;
    public String  restaurant;
    public boolean lactoseFree;
    public boolean glutenFree;
    public boolean isVegan;
    public float   priceInEuros;
    public boolean isBreakfast;
    public boolean isBrunch;
    public boolean isLunch;
    public boolean isDinner;
    public String  previewImage;

    public  Dish()
    {
        this.name         = "FoodName";
        this.description  = "FoodDescription";
        this.restaurant   = "Restaurant";
        this.lactoseFree  = true;
        this.glutenFree   = true;
        this.isVegan      = true;
        this.priceInEuros = 13.37f;
        this.isBreakfast  = true;
        this.isBrunch     = true;
        this.isLunch      = true;
        this.isDinner     = true;
        this.previewImage = "FoodImage";
    }

    public Dish(String name, String description, String restaurant, boolean lactoseFree, boolean glutenFree, boolean vegan, float priceInEuros, boolean isBreakfast, boolean isBrunch, boolean isLunch, boolean isDinner, String previewImage)
    {
        this.name         = name;
        this.description  = description;
        this.restaurant   = restaurant;
        this.lactoseFree  = lactoseFree;
        this.glutenFree   = glutenFree;
        this.isVegan      = vegan;
        this.priceInEuros = priceInEuros;
        this.isBreakfast  = isBreakfast;
        this.isBrunch     = isBrunch;
        this.isLunch      = isLunch;
        this.isDinner     = isDinner;
        this.previewImage = previewImage;
    }
}
