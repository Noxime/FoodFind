package net.noxcorp.noxim.foodfind;

import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;


import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Noxim on 10-Jul-16.
 */
public class Utils {

    public static ArrayList<Dish> sortByDistance(ArrayList<Dish> values, double latitude, double longitude)
    {
        ArrayList<Dish> result = new ArrayList<>();

        while(values.size() > 0)
        {
            float min = Float.MAX_VALUE;
            int index = 0;
            int iteration = 0;

            for(Dish currentDish : values)
            {
                float h = (float)distance(latitude, longitude, currentDish.latitude, currentDish.longitude);

                if(h < min)
                {
                    min = h;
                    index = iteration;
                }

                iteration++;
            }

            result.add(values.get(index));
            values.remove(index);
        }

        return result;
    }

    public static ArrayList<Dish> sortByScore(ArrayList<Dish> values)
    {
        ArrayList<Dish> result = new ArrayList<>();

        while(values.size() > 0)
        {
            float max = 0;
            int index = 0;
            int iteration = 0;

            for(Dish currentDish : values)
            {
                float h = currentDish.getScore(FilterFoods.priceWeighting / 100f, FilterFoods.distanceWeighting / 100f, FilterFoods.ratingWeighting / 100f);

                if(h > max)
                {
                    max = h;
                    index = iteration;
                }

                iteration++;
            }

            result.add(values.get(index));
            values.remove(index);
        }

        return result;
    }

    public static String convertStreamToString(java.io.InputStream is) {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }

    static int LOAD_COUNT = 1;
    public static String loadFile(String name)
    {
        AssetManager am = MainActivity.applicationContext.getApplicationContext().getAssets();
        String data = "";

        try {
            data = Utils.convertStreamToString(am.open(name));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Log.d("FoodFindDebug", LOAD_COUNT++ + " load event");
        return data;
    }

    public static SyncedFile[] parseFileIndex(String string)
    {
        ArrayList<SyncedFile> files = new ArrayList<>();
        String[] items = string.split(">");
        for(String i : items)
        {
            if(!i.contains(":"))
                continue;

            int type;
            if(i.split(":")[1].equalsIgnoreCase("TYPE_FOOD"))
            {
                type = SyncedFile.TYPE_FOOD;
            }
            else if(i.split(":")[1].equalsIgnoreCase("TYPE_RESTAURANT"))
            {
                type = SyncedFile.TYPE_RESTAURANT;
            }
            else
            {
                type = SyncedFile.TYPE_OTHER;
            }
            files.add(new SyncedFile(i.split(":")[0], type, loadFile(i.split(":")[0]), i.split(":")[2]));
        }

        return files.toArray(new SyncedFile[files.size()]);
    }

    public static boolean contains(String[] a, String s)
    {
        for(String v : a)
        {
            if(v.equalsIgnoreCase(s))
                return true;
        }

        return false;
    }

    public static LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(MainActivity.applicationContext);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                Log.e("FoodFindDebug", "Didn't find a location for" + strAddress);
                return new LatLng(0, 0);
            }
            Address location = null;

            try {
                location = address.get(0);
            }
            catch (IndexOutOfBoundsException e)
            {
                System.err.println("No addresses found");
            }
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public static String[] parseProperties(String property, String string)
    {
        ArrayList<String> result = new ArrayList<>();
        String[] items = string.split(">");

        for(String i : items)
        {
            if(i.split("=")[0].equalsIgnoreCase(property))
            {
                result.add(i.split("=")[1]);
            }
        }


        return result.toArray(new String[result.size()]);
    }

    public static double toRad(double x)
    {
        return x * Math.PI / 180;
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2)
    {

        double R = 6378137; //Earths mean radius in meters
        double dLat = toRad(lat2 - lat1);
        double dLng = toRad(lng2 - lng1);
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) +
                   Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                   Math.sin(dLng / 2.0) * Math.sin(dLng / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        double d = R * c;

        return d; // returns the distance in meter
    }

}
