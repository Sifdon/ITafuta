package com.itafuta;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCategories.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCategories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCategories extends Fragment {
    //----------MY DATA----------------------
    RecyclerView mRecyclerView;
    LinearLayout glLayout;

    private final String catName[] = {
            "Carpet Cleaning",
            "Carpenter",
            "Electrician",
            "Mechanic",
            "Garbage Collectors",
            "House Agents",
            "Painter",
            "Water Delivery Lorry",
            "Mama Nguo",
            "Gardener/Grass Cutting",
            "Mover/Canter/Pick-up",
            "Plumber"
    };

    private final int catImage[] = {
            R.drawable.ic_carpet_cleaning,
            R.drawable.ic_carpenter,
            R.drawable.ic_electrician_96,
            R.drawable.ic_mechanic,
            R.drawable.ic_garbage_collector,
            R.drawable.ic_house_agents_96,
            R.drawable.ic_painter,
            R.drawable.ic_water_delivery_lorry,
            R.drawable.briefcase,
            R.drawable.briefcase,
            R.drawable.briefcase,
            R.drawable.briefcase
    };
    //----------MY DATA----------------------

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;





    private OnFragmentInteractionListener mListener;

    public FragmentCategories() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCategories.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCategories newInstance(String param1, String param2) {
        FragmentCategories fragment = new FragmentCategories();
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

        //Get the layout
        glLayout = (LinearLayout) inflater.inflate(R.layout.fragment_fragment_categories, container, false);

        // Inflate the layout for this fragment
        initViews();

        //return inflater.inflate(R.layout.activity_categories, container, false);
        return glLayout;
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

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initViews();
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


    private void initViews() {
        //Reference the RecyclerView
        mRecyclerView = (RecyclerView) glLayout.findViewById(R.id.categoryRecyclerViewFF);
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);

        //Reference a layoutManager to use
        //Setting the dynamic number of columns



        //Try Fix width error


        //gets the screen width in dips and then divides by 300 (or whatever width you're using for your adapter's layout).
        //So smaller phones with 300-500 dip width only display one column,
//      //tablets 2-3 columns etc. Simple, fuss free and without downside, as far as I can see.
        //Display display = getActivity().getWindowManager().getDefaultDisplay(); //The original
        Display display =  getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 200);
//        if (dpWidth < 600) {
//            columns = Math.round(dpWidth / 200);
//        }else{
//            columns = 4;
//        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), columns);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final ArrayList<CategoriesObject> categories = prepareData();
        CategoriesRecyclerViewAdapter adapter = new CategoriesRecyclerViewAdapter(categories);
        mRecyclerView.setAdapter(adapter);

        //************************click content*********************
        mRecyclerView.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(getActivity(), mRecyclerView,
                new MainActivity.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        CategoriesObject pressedCategory = categories.get(position);
                        Toast.makeText(getActivity(), pressedCategory.getCatTitle() + " CLICKED", Toast.LENGTH_SHORT).show();
                        Intent goToCategory = new Intent(getActivity(), MainActivity.class);
//                        String catTitle = pressedCategory.getCatTitle();
//                        goToCategory.putExtra("CAT_TITLE", catTitle);
                        startActivity(goToCategory);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        CategoriesObject toastData = categories.get(position);
                        Toast.makeText(getActivity(), toastData.getCatTitle() + " CATEGORY", Toast.LENGTH_SHORT).show();
                    }
                }

        ));

        //************************END click content*********************


    }


    //Method to add data to the list of categories
    private ArrayList<CategoriesObject> prepareData() {
        ArrayList<CategoriesObject> allCategories = new ArrayList<>();
        for (int i = 0; i < catName.length; i++) {

            //Create instance of Category object and set its name and image
            CategoriesObject oneCategory = new CategoriesObject();
            oneCategory.setCatImage(catImage[i]);
            oneCategory.setCatTitle(catName[i]);

            //Add that item to the list of all categories
            allCategories.add(oneCategory);
        }

        return allCategories;
    }
}
