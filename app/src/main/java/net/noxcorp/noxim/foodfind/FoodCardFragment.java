package net.noxcorp.noxim.foodfind;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodCardFragment extends Fragment implements View.OnClickListener {

    private Dish dish = new Dish();

    private OnFragmentInteractionListener mListener;

    public FoodCardFragment()
    {

    }

    public void setDish(Dish dish) {
        this.dish = dish;
        if(v != null && isAdded())
            setFromDish(dish);
    }

    private void setFromDish(Dish dish) {
        setName(dish.name);
        setRating(dish.rating);
        setRestaurant(dish.restaurant);
        setPrice(dish.priceInEuros);
        setImage(dish.previewImage);
        setDistance((float) Utils.distance(dish.latitude, dish.longitude, MainActivity.latestLatitude, MainActivity.latestLongitude));
        setScore(dish.getScore(FilterFoods.priceWeighting, FilterFoods.distanceWeighting, FilterFoods.ratingWeighting));
    }

    boolean onViewCreateUpdateDistance = false;
    public void updateDistance() {
        if(v == null)
        {
            onViewCreateUpdateDistance = true;
        }
        else {
            setFromDish(dish);
        }
    }

    public static FoodCardFragment newInstance() {
        FoodCardFragment fragment = new FoodCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    TextView fname;
    public void setName(String text) {
        if (v != null && isAdded())
            fname.setText(text);
    }

    TextView rname;
    public void setRestaurant(String text) {
        if (v != null && isAdded())
            rname.setText(text);
    }
    TextView fscor;
    public void setScore(int score) {
        if (v != null && isAdded())
            fscor.setText(score + "fp");
    }

    TextView fpric;
    public void setPrice(float price) {
        NumberFormat formatter = new DecimalFormat("###,###,###.##");
        String text = formatter.format(price) + "\u20ac";
        if (v != null && isAdded())
            fpric.setText(text);
    }

    TextView frati;
    public void setRating(float rating)
    {
        String text = rating + "\u2605";
        if(v != null && isAdded())
            frati.setText(text);
    }

    ImageView iview;
    public void setImage(String text)
    {
        if(v == null || !isAdded())
        {
            Log.w("FoodFindDebug", "Fragment " + dish.name + " not valid or something? v == null: " + (v == null) + " !isAdded(): " + !isAdded());
            new Exception().printStackTrace();
            return;
        }
        iview.setImageResource(iview.getContext().getResources().getIdentifier(text, "drawable", iview.getContext().getPackageName()));
    }

    TextView fdist;
    public void setDistance(float distance)
    {
        if(v != null && isAdded())
            if(distance < 1000)
                fdist.setText((int)distance + "m");
            else
                fdist.setText((int)(distance / 1000) + "km");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_cardview, container, false);
        fname = (TextView) v.findViewById(R.id.cardFoodName);
        rname = (TextView) v.findViewById(R.id.cardRestaurantName);
        fpric = (TextView) v.findViewById(R.id.cardFoodPrice);
        iview = (ImageView)v.findViewById(R.id.cardPreviewImage);
        fdist = (TextView) v.findViewById(R.id.cardFoodDistance);
        frati = (TextView) v.findViewById(R.id.cardFoodRating);
        fscor = (TextView) v.findViewById(R.id.cardFoodScore);

        setFromDish(dish);

        CardView cv = (CardView)v.findViewById(R.id.card_view);
        cv.setOnClickListener(this);
        onViewCreateUpdateDistance = false;
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */

        //setFromDish(dish);
        //getActivity().findViewById()
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Log.i("FoodFindDebug", "Touched " + dish.name);
        new FoodInfo().setDish(dish).show(MainActivity.mainActivity.getFragmentManager(), "FoodFindDebug");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
