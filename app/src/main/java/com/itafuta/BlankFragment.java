package com.itafuta;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import  android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private LinearLayout llLayout;


    //MY FRAGMENT DATA TEST
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    //Dummy Data
    ArrayList<ProviderData> results = new ArrayList<ProviderData> ();

    //

    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_blank, container, false);
        //*********** ADDING THE RECYCERVIEW *******

        llLayout = (LinearLayout) inflater.inflate(R.layout.fragment_blank, container, false);

        mRecyclerView = (RecyclerView) llLayout.findViewById(R.id.provider_recycler_viewFF); //References the recycler
        assert mRecyclerView != null;//To avoid null pointer
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ProviderRecycerViewAdapter(updateUi());
        mRecyclerView.setAdapter(mAdapter);

        //************************click content*********************
        mRecyclerView.addOnItemTouchListener(new CategoriesActivity.RecyclerTouchListener(getActivity(), mRecyclerView,
                        new CategoriesActivity.ClickListener() {
                            @Override
                            public void onClick (View view,int position){
                                ProviderData toastData = results.get(position);
                                Toast.makeText(getActivity(), toastData.toString() + " CLICKED", Toast.LENGTH_SHORT).show();
                                Intent goToSp = new Intent(getActivity(), ServiceProviderActivity.class);

                                String userName = toastData.getUsername();
                                goToSp.putExtra("USER_NAME", userName);
                                startActivity(goToSp);
                            }

                            @Override
                            public void onLongClick (View view,int position){
                                ProviderData toastData = results.get(position);
                                Toast.makeText(getActivity(), toastData.toString() + " is long pressed", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
        );

        //************************END click content*********************

        //*********** END OF ADDING THE RECYCERVIEW *******




        return llLayout;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    //Method to add dummy data to our Arraylist of items
    private  ArrayList<ProviderData> updateUi(){

        for (int i=0; i< 3; i++){

            //Get random numbers and convert them to float
            double a = (double)(Math.random()*5);
            DecimalFormat c = new DecimalFormat("#.0");
            String last = c.format(a);
            float f = Float.parseFloat(last);

            //ProviderData newData = new ProviderData(R.drawable.user+"", "Kawangware, Lavington, Karen", "Plumber, Electrician", "Evans Ouma", "-", f);
//image details name favcount
            //results.add(i, newData);
        }
        return results;
    }



    //############## CLICKING ITEMS ON RECYCLER ################
    //*************GESTURE DETECTOR***********
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        //private MainActivity.ClickListener clickListener;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    //*************END GESTURE DETECTOR***********
    //############## END CLICKING ITEMS ON RECYCLER ###########
}
