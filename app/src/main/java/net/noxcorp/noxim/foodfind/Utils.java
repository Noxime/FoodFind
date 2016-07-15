package net.noxcorp.noxim.foodfind;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Noxim on 10-Jul-16.
 */
public class Utils {

    public static String convertStreamToString(java.io.InputStream is) {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
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

    public static String loadFile(String name)
    {
        AssetManager am = MainActivity.c.getApplicationContext().getAssets();
        String data = "";
        try {
            data = Utils.convertStreamToString(am.open(name));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return data;
    }

    public static SyncedFile[] parseFileIndex(String string)
    {
        ArrayList<SyncedFile> files = new ArrayList<>();
        String[] items = string.split(">");
        for(String i : items)
        {
            int type = 0;
            if(i.split(":")[1] == "TYPE_DISH")
            {
                type = SyncedFile.TYPE_DISH;
            }
            else if(i.split(":")[1] == "TYPE_RESTAURANT")
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

    public static String[] parseProperties(String property, String string)
    {
        ArrayList<String> result = new ArrayList<>();
        String[] items = string.split(">");

        for(String i : items)
        {
            if(i.split("=")[0] == property)
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
        Log.i("FoodFindDebug", "Distance calculated: lat1: " + lat1 + " lng1: " + lng1 + " lat2: " + lat2 + " lng2: " + lng2 + " distance: " + d);
        return d; // returns the distance in meter
    }

}
