package net.noxcorp.noxim.foodfind;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FilterFoods extends DialogFragment {
    static boolean lactoseSelected = false;
    static boolean glutenSelected  = false;
    static boolean veganSelected   = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMultiChoiceItems(new CharSequence[]
                {
                        "Lactose free",
                        "Gluten free",
                        "Vegan"
                },
                new boolean[]
                {
                        lactoseSelected,
                        glutenSelected,
                        veganSelected
                },
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {

                        switch (which){
                            case 0:
                                lactoseSelected = isChecked;
                                break;
                            case 1:
                                glutenSelected  = isChecked;
                                break;
                            case 2:
                                veganSelected   = isChecked;
                                break;
                        }

                    }
                });
                builder
                .setPositiveButton("Filter foods", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.filterDishes(lactoseSelected, glutenSelected, veganSelected);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
