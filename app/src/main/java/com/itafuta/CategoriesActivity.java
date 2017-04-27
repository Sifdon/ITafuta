package com.itafuta;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

    private final String catName[] = {
            "Carpet Cleaning",
            "Carpenter",
            "Electrician",
            "Mama Nguo",
            "Mechanic",
            "Garbage Collectors",
            "Gardener/Grass Cutting",
            "House Agents",
            "Mover/Canter/Pick-up",
            "Painter",
            "Plumber",
            "Water Delivery Lorry"

    };

    private final int catImage[] = {
            R.drawable.briefcase,
            R.drawable.briefcase,
            R.drawable.ic_electrician_96,
            R.drawable.briefcase,
            R.drawable.briefcase,
            R.drawable.ic_garbage_collector,
            R.drawable.briefcase,
            R.drawable.ic_house_agents_96,
            R.drawable.briefcase,
            R.drawable.briefcase,
            R.drawable.briefcase,
            R.drawable.briefcase,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        initViews();
    }

    private void initViews() {
        //Reference the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);

        //Reference a layoutManager to use
        //Setting the dynamic number of columns

        //gets the screen width in dips and then divides by 300 (or whatever width you're using for your adapter's layout).
        //So smaller phones with 300-500 dip width only display one column,
//      //tablets 2-3 columns etc. Simple, fuss free and without downside, as far as I can see.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 500);
//        if (dpWidth < 600) {
//            columns = Math.round(dpWidth / 200);
//        }else{
//            columns = 4;
//        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final ArrayList<CategoriesObject> categories = prepareData();
        CategoriesRecyclerViewAdapter adapter = new CategoriesRecyclerViewAdapter(categories);
        mRecyclerView.setAdapter(adapter);

        //************************click content*********************
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        CategoriesObject pressedCategory = categories.get(position);
                        Toast.makeText(CategoriesActivity.this, pressedCategory.getCatTitle() + " CLICKED", Toast.LENGTH_SHORT).show();
                        Intent goToCategory = new Intent(CategoriesActivity.this, MainActivity.class);
//                        String catTitle = pressedCategory.getCatTitle();
//                        goToCategory.putExtra("CAT_TITLE", catTitle);
                        startActivity(goToCategory);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        CategoriesObject toastData = categories.get(position);
                        Toast.makeText(getApplicationContext(), toastData.getCatTitle() + " CATEGORY", Toast.LENGTH_SHORT).show();
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


    //############## CLICKING ITEMS ON RECYCLER ################
    //*************GESTURE DETECTOR***********
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        //private MainActivity.ClickListener clickListener;
        private CategoriesActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final CategoriesActivity.ClickListener clickListener) {
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

