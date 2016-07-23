package net.noxcorp.noxim.foodfind;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.cert.PKIXBuilderParameters;
import java.util.ArrayList;

public class FilterFoods extends DialogFragment {
    static int priceWeighting    = 50;
    static int distanceWeighting = 50;
    static int ratingWeighting   = 50;

    static boolean veganSelected       = false;
    static boolean eggSelected         = false;
    static boolean glutenSelected      = false;
    static boolean milkproteinSelected = false;
    static boolean nutsSelected        = false;
    static boolean molluscsSelected    = false;
    static boolean crustaceansSelected = false;
    static boolean fishSelected        = false;
    static boolean lactoseSelected     = false;
    static boolean soySelected         = false;

    public static void saveSettings()
    {
        SharedPreferences sharedPreferences = MainActivity.mainActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("priceWeighting",    priceWeighting);
        editor.putInt("distanceWeighting", distanceWeighting);
        editor.putInt("ratingWeighting",   ratingWeighting);

        editor.putBoolean("veganSelected",       veganSelected);
        editor.putBoolean("eggSelected",         eggSelected);
        editor.putBoolean("glutenSelected",      glutenSelected);
        editor.putBoolean("milkproteinSelected", milkproteinSelected);
        editor.putBoolean("nutsSelected",        nutsSelected);
        editor.putBoolean("molluscsSelected",    molluscsSelected);
        editor.putBoolean("crustaceansSelected", crustaceansSelected);
        editor.putBoolean("fishSelected",        fishSelected);
        editor.putBoolean("lactoseSelected",     lactoseSelected);
        editor.putBoolean("soySelected",          soySelected);


        editor.commit();
    }
    public static void readSettings()
    {
        SharedPreferences sharedPref = MainActivity.mainActivity.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 50;
        priceWeighting    = sharedPref.getInt("priceWeighting",    defaultValue);
        distanceWeighting = sharedPref.getInt("distanceWeighting", defaultValue);
        ratingWeighting   = sharedPref.getInt("ratingWeighting",   defaultValue);

        veganSelected       = sharedPref.getBoolean("veganSelected",       false);
        eggSelected         = sharedPref.getBoolean("eggSelected",         false);
        glutenSelected      = sharedPref.getBoolean("glutenSelected",      false);
        milkproteinSelected = sharedPref.getBoolean("milkproteinSelected", false);
        nutsSelected        = sharedPref.getBoolean("nutsSelected",        false);
        molluscsSelected    = sharedPref.getBoolean("molluscsSelected",    false);
        crustaceansSelected = sharedPref.getBoolean("crustaceansSelected", false);
        fishSelected        = sharedPref.getBoolean("fishSelected",        false);
        lactoseSelected     = sharedPref.getBoolean("lactoseSelected",     false);
        soySelected         = sharedPref.getBoolean("soySelected",         false);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMultiChoiceItems(new CharSequence[]
                {
                        "Only vegan",
                        "No eggs",
                        "No gluten",
                        "No milk protein",
                        "No nuts",
                        "No mollusks",
                        "No crustaceans",
                        "No fish",
                        "No lactose",
                        "No soy",


                },
                new boolean[]
                {
                        veganSelected,
                        eggSelected,
                        glutenSelected,
                        milkproteinSelected,
                        nutsSelected,
                        molluscsSelected,
                        crustaceansSelected,
                        fishSelected,
                        lactoseSelected,
                        soySelected
                },
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {

                        switch (which){
                            case 0:
                                veganSelected = isChecked;
                                break;
                            case 1:
                                eggSelected  = isChecked;
                                break;
                            case 2:
                                glutenSelected   = isChecked;
                                break;
                            case 3:
                                milkproteinSelected = isChecked;
                                break;
                            case 4:
                                nutsSelected = isChecked;
                                break;
                            case 5:
                                molluscsSelected = isChecked;
                                break;
                            case 6:
                                crustaceansSelected = isChecked;
                                break;
                            case 7:
                                fishSelected = isChecked;
                                break;
                            case 8:
                                lactoseSelected = isChecked;
                                break;
                            case 9:
                                soySelected = isChecked;
                                break;

                        }

                    }
                });
                builder
                .setPositiveButton("Filter foods", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.filterDishes(veganSelected, eggSelected, glutenSelected, milkproteinSelected, nutsSelected, molluscsSelected, crustaceansSelected, fishSelected, lactoseSelected, soySelected);
                        saveSettings();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        LinearLayout layout = new LinearLayout(MainActivity.applicationContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        SeekBar priceWeightingSeekBar = new SeekBar(MainActivity.applicationContext);
        priceWeightingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priceWeighting = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        priceWeightingSeekBar.setProgress(priceWeighting);
        TextView priceTextView = new TextView(MainActivity.applicationContext);
        priceTextView.setText("Price importance");
        priceTextView.setTextColor(Color.BLACK);
        priceTextView.setPadding(10, 10, 10, 10);

        SeekBar distanceWeightingSeekBar = new SeekBar(MainActivity.applicationContext);
        distanceWeightingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceWeighting = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        distanceWeightingSeekBar.setProgress(distanceWeighting);
        TextView distanceTextView = new TextView(MainActivity.applicationContext);
        distanceTextView.setText("Distance importance");
        distanceTextView.setTextColor(Color.BLACK);
        distanceTextView.setPadding(10, 10, 10, 10);

        SeekBar ratingWeightingSeekBar = new SeekBar(MainActivity.applicationContext);
        ratingWeightingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ratingWeighting = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ratingWeightingSeekBar.setProgress(ratingWeighting);
        TextView ratingTextView = new TextView(MainActivity.applicationContext);
        ratingTextView.setText("Rating importance");
        ratingTextView.setTextColor(Color.BLACK);
        ratingTextView.setPadding(10, 10, 10, 10);

        layout.addView(priceTextView);
        layout.addView(priceWeightingSeekBar);
        layout.addView(distanceTextView);
        layout.addView(distanceWeightingSeekBar);
        layout.addView(ratingTextView);
        layout.addView(ratingWeightingSeekBar);

        builder.setView(layout);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
