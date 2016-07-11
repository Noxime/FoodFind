package net.noxcorp.noxim.foodfind;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        Log.e("FoodFindDebug", "Distance calculated: lat1: " + lat1 + " lng1: " + lng1 + " lat2: " + lat2 + " lng2: " + lng2 + " distance: " + d);
        return d; // returns the distance in meter
    }

}
