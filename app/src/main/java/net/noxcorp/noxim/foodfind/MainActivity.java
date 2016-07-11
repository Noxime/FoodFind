package net.noxcorp.noxim.foodfind;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static double latestLatitude  = 60.0;
    public static double latestLongitude = 24.0;
    public static FoodCardFragment[] fragments;

    private Dish[] loadDishes() {
        AssetManager am = getApplicationContext().getAssets();
        String data = "";
        try {
            data = Utils.convertStreamToString(am.open("foodData.data"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Dish> d = new ArrayList<>();

        String[] restaurants = data.trim().split("<restaurantSplitter>");
        Log.i("FoodFindDebug", "found " + restaurants.length + " restaurants");
        for (int i = 0; i < restaurants.length; i++) {
            String[] foods = restaurants[i].trim().split("<foodSplitter>");
            Log.i("FoodFindDebug", " found " + foods.length + " foods in " + restaurants[i]);
            for (int j = 0; j < foods.length; j++) {
                String[] properties = foods[j].trim().split("<propertySplitter>");
                Log.i("FoodFindDebug", "  found " + properties.length + " properties in " + foods[j]);
                if (properties.length < 13)
                    break;

                d.add(new Dish(properties[0].trim(),                      //Name
                        properties[1].trim(),                             //Description
                        properties[2].trim(),                             //Restaurant
                        Boolean.parseBoolean(properties[3].trim()),       //Lactose
                        Boolean.parseBoolean(properties[4].trim()),       //Gluten
                        Boolean.parseBoolean(properties[5].trim()),       //Vegan
                        Float.parseFloat(properties[6].trim()),           //Price
                        Boolean.parseBoolean(properties[7].trim()),       //Breakfast
                        Boolean.parseBoolean(properties[8].trim()),       //Brunch
                        Boolean.parseBoolean(properties[9].trim()),       //Lunch
                        Boolean.parseBoolean(properties[10].trim()),      //Dinner
                        properties[11].trim(),                            //Image
                        Double.parseDouble(properties[12].split(":")[0]), //Latitude
                        Double.parseDouble(properties[12].split(":")[1])  //Longitude
                        ));

            }
        }

        Log.i("FoodFindDebug", "found " + d.size() + " dishes");
        //d[0] = new Dish("Big Mac", "Iconic burger with no ketchup", "McDonalds", true, false, 4.10f, false, true, true, true, "bigmac_transparent");
        return d.toArray(new Dish[d.size()]);
    }

    static Dish[] dishes;
    static MainActivity m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dishes = loadDishes();
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i("FoodFindDebug", "lat: " + location.getLatitude() + " long: " + location.getLongitude());
                latestLatitude  = location.getLatitude();
                latestLongitude = location.getLongitude();
                for(int i = 0; i < fragments.length; i++)
                {
                    fragments[i].updateDistance();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionInfo.PROTECTION_NORMAL);

        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        if (findViewById(R.id.fragmentList) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            fragments = new FoodCardFragment[dishes.length];

            for (int i = 0; i < dishes.length; i++) {
                // Create a new Fragment to be placed in the activity layout
                fragments[i] = new FoodCardFragment();
                fragments[i].setDish(dishes[i]);
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                fragments[i].setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentList, fragments[i], dishes[i].name + " " + dishes[i].restaurant).commit();
                //getSupportFragmentManager().beginTransaction().attach(fragments[i]);
                //((LinearLayout)this.findViewById(R.id.fragmentList));
            }
        }
        m = this;
        //new FilterFoods().show(getFragmentManager(), "FoodFindDebug");
    }

    public static void filterDishes(boolean lactose, boolean gluten, boolean vegan)
    {
        ((LinearLayout)m.findViewById(R.id.fragmentList)).removeAllViews();
        ArrayList<Dish> filteredDishes = new ArrayList<>();
        for(int i = 0; i < dishes.length; i++)
        {
            if(lactose)
                if(!dishes[i].lactoseFree)
                    continue;
            if(gluten)
                if(!dishes[i].glutenFree)
                    continue;
            if(vegan)
                if(!dishes[i].isVegan)
                    continue;
            filteredDishes.add(dishes[i]);
        }

        fragments = new FoodCardFragment[filteredDishes.size()];

        for (int i = 0; i < filteredDishes.size(); i++) {
            // Create a new Fragment to be placed in the activity layout
            fragments[i] = new FoodCardFragment();
            fragments[i].setDish(filteredDishes.get(i));
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            fragments[i].setArguments(m.getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            m.getSupportFragmentManager().beginTransaction().add(R.id.fragmentList, fragments[i], filteredDishes.get(i).name + " " + filteredDishes.get(i).restaurant).commit();
            //getSupportFragmentManager().beginTransaction().attach(fragments[i]);
            //((LinearLayout)this.findViewById(R.id.fragmentList));
        }
    }
}
