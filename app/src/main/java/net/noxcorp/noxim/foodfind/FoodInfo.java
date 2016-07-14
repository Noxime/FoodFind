package net.noxcorp.noxim.foodfind;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Noxim on 11-Jul-16.
 */
public class FoodInfo extends DialogFragment {

    private Dish dish;
    private View v;
    private TextView  viewName;
    private TextView  viewDesc;
    private TextView  viewRest;
    private ImageView viewImge;

    public FoodInfo setDish(Dish d)
    {
        dish = d;
        return this;
    }

    private void updateDish()
    {
        viewName.setText(dish.name);
        viewRest.setText(dish.restaurant);
        viewDesc.setText(dish.description);
        viewImge.setImageResource(v.getContext().getResources().getIdentifier(dish.previewImage, "drawable", v.getContext().getPackageName()));
    }
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_cardview, container, false);
        viewName = (TextView) v.findViewById(R.id.food_info_name);
        viewRest = (TextView) v.findViewById(R.id.food_info_restaurant);
        viewDesc = (TextView) v.findViewById(R.id.food_info_description);
        viewImge = (ImageView)v.findViewById(R.id.food_info_image);

        //setFromDish(dish);

        return v;
    }
*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_food_info, null);
        builder.setView(v)
        .setPositiveButton("Open in Maps", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Implement google maps
                Log.i("FoodFindDebug", "Positive button pressed");

                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", dish.latitude, dish.longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                v.getContext().startActivity(intent);
                //MainActivity.c
            }
        })
        .setNegativeButton("Cancel", null)

        ;

        viewName = (TextView) v.findViewById(R.id.food_info_name);
        viewRest = (TextView) v.findViewById(R.id.food_info_restaurant);
        viewDesc = (TextView) v.findViewById(R.id.food_info_description);
        viewImge = (ImageView)v.findViewById(R.id.food_info_image);

        updateDish();
        return builder.create();
    }
}
