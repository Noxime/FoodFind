package net.noxcorp.noxim.foodfind;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static double latestLatitude  = 60.0;
    public static double latestLongitude = 24.0;
    public static FoodCardFragment[] fragments;
    public static Context c;
    public static SyncedFile[] files;

    private ArrayList<Dish> loadDishes() {
        ArrayList<Dish> dishes = new ArrayList<>();
        files = Utils.parseFileIndex(Utils.loadFile("FileIndex.txt"));

        for(SyncedFile f : files)
        {
            if(f.type == SyncedFile.TYPE_RESTAURANT) {
                String[] names        = Utils.parseProperties("name",        f.contents);
                String[] ratings      = Utils.parseProperties("rating",      f.contents);
                String[] phonenumbers = Utils.parseProperties("phonenumber", f.contents);
                String[] addresses    = Utils.parseProperties("address",     f.contents);
                String[] coordinates  = Utils.parseProperties("coordinates", f.contents);
                String[] openhours    = Utils.parseProperties("openhours",   f.contents);
                String[] foods        = Utils.parseProperties("foods",       f.contents);

                int restaurantIndex = 0;

                for(String foodList : foods) {

                    String[] parsedFoods = foodList.split(":");

                    for (String foodFilename : parsedFoods) {

                        for (SyncedFile searchFile : files) {

                            if (foodFilename.trim().equalsIgnoreCase(searchFile.filename.trim())) {


                                String name        =                  Utils.parseProperties("name",        searchFile.contents)[0].trim();
                                String description =                  Utils.parseProperties("description", searchFile.contents)[0].trim();
                                String ingredients =                  Utils.parseProperties("ingredients", searchFile.contents)[0].trim();
                                String image       =                  Utils.parseProperties("image",       searchFile.contents)[0].trim();
                                float  price       = Float.parseFloat(Utils.parseProperties("price",       searchFile.contents)[0].trim());
                                float  rating      = Float.parseFloat(Utils.parseProperties("rating",      searchFile.contents)[0].trim());
                                int    vegan       = Integer.parseInt(Utils.parseProperties("vegan",       searchFile.contents)[0].trim());
                                int    egg         = Integer.parseInt(Utils.parseProperties("egg",         searchFile.contents)[0].trim());
                                int    gluten      = Integer.parseInt(Utils.parseProperties("gluten",      searchFile.contents)[0].trim());
                                int    milkprotein = Integer.parseInt(Utils.parseProperties("milkprotein", searchFile.contents)[0].trim());
                                int    nuts        = Integer.parseInt(Utils.parseProperties("nuts",        searchFile.contents)[0].trim());
                                int    molluscs    = Integer.parseInt(Utils.parseProperties("molluscs",    searchFile.contents)[0].trim());
                                int    crustaceans = Integer.parseInt(Utils.parseProperties("crustaceans", searchFile.contents)[0].trim());
                                int    fish        = Integer.parseInt(Utils.parseProperties("fish",        searchFile.contents)[0].trim());
                                int    lactose     = Integer.parseInt(Utils.parseProperties("lactose",     searchFile.contents)[0].trim());
                                int    soy         = Integer.parseInt(Utils.parseProperties("soy",         searchFile.contents)[0].trim());

                                dishes.add(new Dish(name, description, ingredients, names[restaurantIndex].trim(), phonenumbers[restaurantIndex].trim(),
                                                    price, rating, lactose, gluten, vegan, egg, milkprotein,
                                                    nuts, molluscs, crustaceans, fish, soy, image,
                                                    addresses[restaurantIndex].trim(),
                                                    Double.parseDouble(coordinates[restaurantIndex].split(":")[0].trim()),
                                                    Double.parseDouble(coordinates[restaurantIndex].split(":")[1].trim())));

                            }
                        }
                    }

                    restaurantIndex++;
                }
            }
        }

        dishes = Utils.sortByDistance(dishes, latestLatitude, latestLongitude);
        return dishes;
    }

    static ArrayList<Dish> dishes;
    static MainActivity m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c = getApplicationContext();


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                latestLatitude  = location.getLatitude();
                latestLongitude = location.getLongitude();
                for(int i = 0; i < fragments.length; i++)
                {
                    fragments[i].updateDistance();
                }

                dishes = Utils.sortByDistance(dishes, latestLatitude, latestLongitude);


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

        dishes = loadDishes();

        if (findViewById(R.id.fragmentList) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            fragments = new FoodCardFragment[dishes.size()];

            for (int i = 0; i < dishes.size(); i++) {
                // Create a new Fragment to be placed in the activity layout
                fragments[i] = new FoodCardFragment();
                fragments[i].setDish(dishes.get(i));
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                fragments[i].setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentList, fragments[i], dishes.get(i).name + " " + dishes.get(i).restaurant).commit();
                //getSupportFragmentManager().beginTransaction().attach(fragments[i]);
                //((LinearLayout)this.findViewById(R.id.fragmentList));
            }
        }
        m = this;
    }

    public static void updateFragments(ArrayList<Dish> newDishes)
    {
        newDishes = Utils.sortByDistance(newDishes, latestLatitude, latestLongitude);

        ((LinearLayout)m.findViewById(R.id.fragmentList)).removeAllViews();

        fragments = new FoodCardFragment[newDishes.size()];

        for (int i = 0; i < newDishes.size(); i++) {
            // Create a new Fragment to be placed in the activity layout
            fragments[i] = new FoodCardFragment();
            fragments[i].setDish(newDishes.get(i));
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            fragments[i].setArguments(m.getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            m.getSupportFragmentManager().beginTransaction().add(R.id.fragmentList, fragments[i], newDishes.get(i).name + " " + newDishes.get(i).restaurant).commit();
            //getSupportFragmentManager().beginTransaction().attach(fragments[i]);
            //((LinearLayout)this.findViewById(R.id.fragmentList));
        }
    }

    public static void filterDishes(boolean lactose, boolean gluten, boolean vegan)
    {
        //TODO: Add more filters
        ArrayList<Dish> filteredDishes = new ArrayList<>();

        for(Dish currentDish : dishes)
        {
            if(vegan)
                if(currentDish.isVegan == Dish.NO)
                    continue;
            if(gluten)
                if(currentDish.hasGluten != Dish.NO)
                    continue;
            if(lactose)
                if(currentDish.hasLactose != Dish.NO)
                    continue;

            filteredDishes.add(currentDish);
        }

        updateFragments(filteredDishes);

    }
}
