package net.noxcorp.noxim.foodfind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class FoodCardFragment extends Fragment {

    private Dish dish = new Dish();

    private OnFragmentInteractionListener mListener;

    public FoodCardFragment() {

    }

    public void setDish(Dish dish)
    {
        this.dish = dish;
        //setFromDish(dish);
    }

    private void setFromDish(Dish dish)
    {
        setName(dish.name);
        setDescription(dish.description);
        setRestaurant(dish.restaurant);
        setPrice(dish.priceInEuros);
        setImage(dish.previewImage);
    }



    public static FoodCardFragment newInstance() {
        FoodCardFragment fragment = new FoodCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    TextView fn;
    public void setName(String text)
    {
        if(v != null && isAdded())
            fn.setText(text);
    }
    TextView rn;
    public void setRestaurant(String text)
    {
        if(v != null && isAdded())
            rn.setText(text);
    }
    TextView fd;
    public void setDescription(String text){
        if(v != null && isAdded())
            fd.setText(text);
    }
    TextView fp;
    public void setPrice(float price)
    {
        NumberFormat formatter = new DecimalFormat("###,###,###.##");
        String text = formatter.format(price) + "\u20ac";
        if(v != null && isAdded())
            fp.setText(text);
    }
    ImageView iv;
    public void setImage(String text)
    {
        if(v == null || !isAdded())
        {
            Log.e("FoodFindDebug", "Fragment " + dish.name + " not valid or something?");
            new Exception().printStackTrace();
            return;
        }
        Log.e("FoodFindDebug", "Fragment valid");
        iv.setImageResource(iv.getContext().getResources().getIdentifier(text, "drawable", iv.getContext().getPackageName()));
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
        fn = (TextView) v.findViewById(R.id.cardFoodName);
        fd = (TextView) v.findViewById(R.id.cardFoodDescription);
        rn = (TextView) v.findViewById(R.id.cardRestaurantName);
        fp = (TextView) v.findViewById(R.id.cardFoodPrice);
        iv = (ImageView)v.findViewById(R.id.cardPreviewImage);


        setFromDish(dish);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
