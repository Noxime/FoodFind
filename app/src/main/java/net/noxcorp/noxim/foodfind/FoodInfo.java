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
    private TextView  viewRati;
    private ImageView viewImge;

    public FoodInfo setDish(Dish d)
    {
        dish = d;
        return this;
    }

    private String copyCharacter(char c, int count)
    {
        String result = "";

        for(int i = 0; i < count; i++)
        {
            result += c;
        }

        return result;
    }

    private void updateDish()
    {
        ((TextView)v.findViewById(R.id.food_info_name           )).setText(dish.name       );
        ((TextView)v.findViewById(R.id.food_info_restaurant     )).setText(dish.restaurant );
        ((TextView)v.findViewById(R.id.food_info_description    )).setText(dish.description);
        ((TextView)v.findViewById(R.id.food_info_ingredients    )).setText(dish.ingredients);
        ((TextView)v.findViewById(R.id.food_info_rating         )).setText(copyCharacter('★', (int) Math.ceil(dish.rating)));
        ((TextView)v.findViewById(R.id.food_info_is_vegan       )).setText(dish.isVegan        == Dish.YES ? "Yes" : (dish.isVegan        == Dish.MAYBE ? "Maybe" : "No"));
        ((TextView)v.findViewById(R.id.food_info_has_egg        )).setText(dish.hasEgg         == Dish.YES ? "Yes" : (dish.hasEgg         == Dish.MAYBE ? "Maybe" : "No"));
        ((TextView)v.findViewById(R.id.food_info_has_gluten     )).setText(dish.hasGluten      == Dish.YES ? "Yes" : (dish.hasGluten      == Dish.MAYBE ? "Maybe" : "No"));
        ((TextView)v.findViewById(R.id.food_info_has_milkprotein)).setText(dish.hasMilkProtein == Dish.YES ? "Yes" : (dish.hasMilkProtein == Dish.MAYBE ? "Maybe" : "No"));
        ((TextView)v.findViewById(R.id.food_info_has_nuts       )).setText(dish.hasNuts        == Dish.YES ? "Yes" : (dish.hasNuts        == Dish.MAYBE ? "Maybe" : "No"));
        ((TextView)v.findViewById(R.id.food_info_has_molluscs   )).setText(dish.hasMolluscs    == Dish.YES ? "Yes" : (dish.hasMolluscs    == Dish.MAYBE ? "Maybe" : "No"));
        ((TextView)v.findViewById(R.id.food_info_has_crustaceans)).setText(dish.hasCrustaceans == Dish.YES ? "Yes" : (dish.hasCrustaceans == Dish.MAYBE ? "Maybe" : "No"));
        ((TextView)v.findViewById(R.id.food_info_has_fish       )).setText(dish.hasFish        == Dish.YES ? "Yes" : (dish.hasFish        == Dish.MAYBE ? "Maybe" : "No"));
        ((TextView)v.findViewById(R.id.food_info_has_lactose    )).setText(dish.hasLactose     == Dish.YES ? "Yes" : (dish.hasLactose     == Dish.MAYBE ? "Maybe" : "No"));
        ((TextView)v.findViewById(R.id.food_info_has_soy        )).setText(dish.hasSoy         == Dish.YES ? "Yes" : (dish.hasSoy         == Dish.MAYBE ? "Maybe" : "No"));
        ((ImageView)v.findViewById(R.id.food_info_image         )).setImageResource(v.getContext().getResources().getIdentifier(dish.previewImage, "drawable", v.getContext().getPackageName()));



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
                //MainActivity.applicationContext
            }
        })
        .setNegativeButton("Cancel", null)
        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dish.phonenumber.trim()));
                v.getContext().startActivity(intent);

            }
        })

        ;

        viewName = (TextView) v.findViewById(R.id.food_info_name);
        viewRest = (TextView) v.findViewById(R.id.food_info_restaurant);
        viewDesc = (TextView) v.findViewById(R.id.food_info_description);
        viewRati = (TextView) v.findViewById(R.id.food_info_rating);
        viewImge = (ImageView)v.findViewById(R.id.food_info_image);

        updateDish();
        return builder.create();
    }
}
