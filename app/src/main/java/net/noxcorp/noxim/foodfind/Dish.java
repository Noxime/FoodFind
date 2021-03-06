package net.noxcorp.noxim.foodfind;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Noxim on 21.6.2016.
 */
public class Dish {

    public static final int NO    = -1;
    public static final int MAYBE =  0;
    public static final int YES   =  1;


    public String  name;
    public String  description;
    public String  ingredients;
    public String  restaurant;
    public String  phonenumber;
    public float   priceInEuros;
    public float   rating;
    public int     hasLactose;
    public int     hasGluten;
    public int     isVegan;
    public int     hasEgg;
    public int     hasMilkProtein;
    public int     hasNuts;
    public int     hasMolluscs;
    public int     hasCrustaceans;
    public int     hasFish;
    public int     hasSoy;
    public String  previewImage;
    public String  address;
    public double  latitude;
    public double  longitude;

    public Dish()
    {
        this.name           = "Constructor";
        this.description    = "Constructor";
        this.ingredients    = "Constructor";
        this.restaurant     = "Constructor";
        this.phonenumber    = "Constructor";
        this.priceInEuros   = 1;
        this.rating         = 5;
        this.hasLactose     = NO;
        this.hasGluten      = NO;
        this.isVegan        = YES;
        this.hasEgg         = NO;
        this.hasMilkProtein = NO;
        this.hasNuts        = NO;
        this.hasMolluscs    = NO;
        this.hasCrustaceans = NO;
        this.hasFish        = NO;
        this.hasSoy         = NO;
        this.previewImage   = "uv_checker_large";
        this.address        = "Constructor";
        this.latitude       = 0;
        this.longitude      = 0;
    }

    public Dish(String name,
                String description,
                String ingredients,
                String restaurant,
                String phonenumber,
                float priceInEuros,
                float rating,
                int hasLactose,
                int hasGluten,
                int isVegan,
                int hasEgg,
                int hasMilkProtein,
                int hasNuts,
                int hasMolluscs,
                int hasCrustaceans,
                int hasFish,
                int hasSoy,
                String previewImage,
                String address,
                double latitude,
                double longitude) {

        this.name           = name;
        this.description    = description;
        this.ingredients    = ingredients;
        this.restaurant     = restaurant;
        this.phonenumber    = phonenumber;
        this.priceInEuros   = priceInEuros;
        this.rating         = rating;
        this.hasLactose     = hasLactose;
        this.hasGluten      = hasGluten;
        this.isVegan        = isVegan;
        this.hasEgg         = hasEgg;
        this.hasMilkProtein = hasMilkProtein;
        this.hasNuts        = hasNuts;
        this.hasMolluscs    = hasMolluscs;
        this.hasCrustaceans = hasCrustaceans;
        this.hasFish        = hasFish;
        this.hasSoy         = hasSoy;
        this.previewImage   = previewImage;
        this.address        = address;
        this.latitude       = latitude;
        this.longitude      = longitude;
    }

    public int getScore(float priceWeighting, float distanceWeighting, float ratingWeighting)
    {
        float normalizer = priceWeighting + distanceWeighting + ratingWeighting;
        priceWeighting /= normalizer;
        distanceWeighting /= normalizer;
        ratingWeighting /= normalizer;

        float distance = (float)Utils.distance(latitude, longitude, MainActivity.latestLatitude, MainActivity.latestLongitude);

        int distanceScore = (int)(100000 / (distance / 10f));

        int ratingScore = (int)(rating * 1000f);

        int priceScore = (int)(5000f / priceInEuros);

        Log.i("FoodFindDebug", distanceScore + " " + ratingScore + " " + priceScore);

        return (int)((float)distanceScore * distanceWeighting + (float)priceScore * priceWeighting + (float)ratingScore * ratingWeighting);
    }

}
