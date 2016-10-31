package com.itafuta;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
//import com.roughike.bottombar.OnMenuTabClickListener;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements FragmentCategories.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    FragmentTransaction ft;
    FragmentManager fm;
    BlankFragment myFragment;
    FragmentCategories categoriesFragment;

    final Context context = this;
    AlertDialog.Builder alertDialogBuilder;

    private BottomBar mBottomBar;  //Bottom bar

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    //Dummy Data
    ArrayList<ProviderData> results = new ArrayList<ProviderData> ();
    ArrayList<ProviderData> results2 = new ArrayList<ProviderData> ();
    private DatabaseReference mDatabase;
    private DatabaseReference providerList;
    private DatabaseReference providerListxx;

    //MyTestdata
    ProviderData newData77;
    String sampleUsername;

    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        providerList = mDatabase.child("providers final").child("KV9iscYBP2gH3y1YHcl").child("FbxkB7gt3WS58ugOLD4MyaPF5MI3").child("username");

        //Toast.makeText(MainActivity.this, providerList.getKey(), Toast.LENGTH_SHORT).show();


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //fetchMyData();
        //Toast.makeText(MainActivity.this, sampleUsername, Toast.LENGTH_SHORT).show();


        if(user!=null) {
            Toast.makeText(MainActivity.this, user.getEmail() + "--" +user.getUid(), Toast.LENGTH_SHORT).show();
            String userId = user.getUid();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("USERNAME", "VictorNew");
            map.put("nickname", "newUserNameTest");
            map.put("rate", 2);
            map.put("email", user.getEmail());


            mDatabase.child("users").child(userId).setValue(map);
        }





        //My FRAGMENT
        myFragment = new BlankFragment();
        categoriesFragment = new FragmentCategories();

        //==========MY FRAGMENT===========
        //Always Categories on launch
        fm = getFragmentManager();
        //================================
        //============END FRAGMENT



//        TextView titleBar = (TextView) findViewById(R.id.categoryTitle);

//        getSupportActionBar().setLogo(R.drawable.logo); //Adds logo to toolbar


//        Bundle extras = getIntent().getExtras();
//        String myString = extras.getString("CAT_TITLE");

//        titleBar.setText(myString);


        //*********** ADDING THE RECYCERVIEW *******

        mRecyclerView = (RecyclerView) findViewById(R.id.provider_recycler_view); //References the recycler
        assert mRecyclerView != null;//To avoid null pointer
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mAdapter = new ProviderRecycerViewAdapter(updateUi()); //Updates dummy data
        mAdapter = new ProviderRecycerViewAdapter(fetchAllProviders());
        mRecyclerView.setAdapter(mAdapter);

        //************************click content*********************
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView,
                new ClickListener() {
                            @Override
                            public void onClick (View view,int position){
                                ProviderData toastData = results.get(position);
                                Toast.makeText(MainActivity.this, toastData.toString() + " CLICKED", Toast.LENGTH_SHORT).show();
                                Intent goToSp = new Intent(MainActivity.this, ServiceProviderActivity.class);

                                String userName = toastData.getProvName();
                                goToSp.putExtra("USER_NAME", userName);
                                startActivity(goToSp);
                            }

                            @Override
                            public void onLongClick (View view,int position){
                                ProviderData toastData = results.get(position);
                                Toast.makeText(getApplicationContext(), toastData.toString() + " is long pressed", Toast.LENGTH_SHORT).show();
                            }
                        }
        )
        );

        //************************END click content*********************

        //*********** END OF ADDING THE RECYCERVIEW *******


//        final TextView myText = (TextView) findViewById(R.id.wazimu);

//        assert myText != null;
//        myText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent goToSp = new Intent(MainActivity.this, ServiceProviderActivity.class);
//                String userName = myText.getText().toString();
//
//                goToSp.putExtra("MyUserName", userName);
//                startActivity(goToSp);
//
//            }
//        });


//        LinearLayout sp = (LinearLayout) findViewById(R.id.card_view);
//        final TextView txtUserName = (TextView) findViewById(R.id.titlename);
//        final TextView txtFavCount = (TextView) findViewById(R.id.favCount);
//        ImageView imgUserImage = (ImageView) findViewById(R.id.provider_image);
//
//
//        if (imgUserImage != null) {
//            imgUserImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }


        //*********Start Bottom bar nav

        //-----------The new bottom menu----------


        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.bottomBarItemFavourites) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.



                    // add
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.my_fragment, categoriesFragment);
//                    ft.addToBackStack(null);
//                    ft.commit();

                }
                if (tabId == R.id.bottomBarItemSearch) {
                    Toast.makeText(MainActivity.this, "You selected " + tabId, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(i);


                }
                if (tabId ==  R.id.bottomBarItemHome) {
                    Toast.makeText(MainActivity.this, "You selected " + tabId, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                    ft = fm.beginTransaction();
//                    ft.remove(myFragment);
                    ft.replace(R.id.my_fragment, categoriesFragment);
                    ft.addToBackStack(null);
                    ft.commit();
//
//                    // add
//                    ft = fm.beginTransaction();
//                    ft.add(R.id.my_fragment, categoriesFragment);
//                    ft.addToBackStack(null);
//                    ft.commit();
                }
                if (tabId == R.id.bottomBarItemInfo) {
                    // add
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.my_fragment, myFragment);
//                    ft.addToBackStack(null);
//                    ft.commit();



                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Get you a service provider from your area...")
                            .setTitle("Natafuta")
                            .setIcon(R.drawable.logohomebtn)
                    // Add the buttons

                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.cancel();
                        }
                    }).create();

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

                if (tabId == R.id.bottomBarItemRegister) {
                    Log.d(TAG, "Going to login activity");
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }

            }


        });

        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.bottomBarItemHome) {
                    // The tab with id R.id.tab_favorites was reselected,
                    // change your content accordingly.
                    Toast.makeText(MainActivity.this, "You UNselected " + tabId, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //-------------- END BITTOM MENU -------




        /*
        //******** THE OLDER BOTTOM BAR
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noTopOffset();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                // The user selected item number one.
                if (menuItemId == R.id.bottomBarItemHome) {
                    Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                }

                if (menuItemId == R.id.bottomBarItemFavourites){}

                if (menuItemId ==R.id.bottomBarItemSearch){

                }

                if (menuItemId ==R.id.bottomBarItemInfo){
                    Intent goToSp = new Intent(MainActivity.this, ServiceProviderActivity.class);
//                    String userName = myText.getText().toString();
//                    goToSp.putExtra("MyUserName", userName);
                    startActivity(goToSp);
                }

                if (menuItemId ==R.id.bottomBarItemRegister){}
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemHome) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });*/

    }
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // Necessary to restore the BottomBar's state, otherwise we would
//        // lose the current tab on orientation change.
//        mBottomBar.onSaveInstanceState(outState);
//
//    }

    //********End bottom bar navigation


    //Method to add test realdata to our Arraylist of items
    //List all data
    Map<String, Object> immediateData;

    private  ArrayList<ProviderData> fetchAllProviders(){
        mDatabase.child("providers final").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot pushedSnap) {
                //pushedSnap give the id pushed
                for(DataSnapshot userIdSnaps : pushedSnap.getChildren()){
                    //userIdSnaps gives the userId registered
                    immediateData = (Map<String, Object>) userIdSnaps.getValue(); //Get a map with the data under Ids

                    //Loop the map and assign the data
                    for(int x= 0; x<immediateData.size(); x++){
                        //convert that data under userIdSnaps
                        ProviderData oneProvider = userIdSnaps.getValue(ProviderData.class);
                        results2.add(oneProvider);
                    }
                }
                //get data from root of providers final //They are pushed IDs
                //immediateData = (Map<String, Object>) dataSnapshot.getValue();//Gives a map of pushed ids
                //+++++++++++//////////


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return results2;
    }

    private  ArrayList<ProviderData> updateUi(){
        previewStoredFirebaseImage();



        //sampleUsername = "###############";

        for (int i=0; i< 10; i++){

            //Get random numbers and convert them to float
            double a = (double)(Math.random()*5);
            DecimalFormat  c = new DecimalFormat("#.0");
            String last = c.format(a);
            float f = Float.parseFloat(last);

            //int, int, int, string, string, string, string, float
            ProviderData newData1 = new ProviderData( R.drawable.finalimagetest1,", Kawangware, Dagoretti, Riara",  "Plumber, Electrician", "Evans Ouma", "-", f);
            ProviderData newData2 = new ProviderData( R.drawable.image_placeholder,"Kawangware, Lavington, Karen",  "Plumber, Electrician", "Johnson", "-", f);
            ProviderData newData3 = new ProviderData( R.drawable.finalimagetest3,"Umoja, Buru Buru",  "Plumber, Electrician", "Samuel China", "-", f);
            ProviderData newData4 = new ProviderData(R.drawable.logo_person,"Kawangware, Lavington, Karen",  "Plumber, Electrician", "Truphena Auma", "-", f);
            ProviderData newData5 = new ProviderData(R.drawable.finalimagetest1,"Kibera, Prestige, Ayany",  "Plumber, Electrician", "James Odongo", "-", f);
            ProviderData newData6 = new ProviderData(R.drawable.finalimagetest,"Ngong road, Kenyatta, Vall",  "Plumber, Electrician", "Evans Ouma", "-", f);

            ProviderData newData = new ProviderData(
                    //image details name favcount

                    R.drawable.displayprofimage,
                    "Kawangware, Lavington, Karen",
                    "Plumber, Electrician",
                    sampleUsername,
                    "-",
                    f
            );


            //results.add(i, newData77);
            results.add(i, newData);
            results.add(i, newData1);
            results.add(i, newData2);
            results.add(i, newData3);
            results.add(i, newData4);
            results.add(i, newData5);
        }
        return results;


    }
    //========================== Update atleast one value from firea =======================================


    private void fetchMyData(){


        //providerList.addValueEventListener(new ValueEventListener() {
        mDatabase.child("mac").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sampleUsername = (String) dataSnapshot.getValue();
                Toast.makeText(MainActivity.this, sampleUsername + "+++++", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    ////===========================
    //Fetchs images and displays it
    private void previewStoredFirebaseImage() {
        mDatabase.child("pic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                sampleUsername = (String) snapshot.getValue();
                Toast.makeText(MainActivity.this, sampleUsername, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
//
//            @Override
//            public void onCancelled(FirebaseError error) {}
        });
    }


    Map<String, Object> allProvidersMap;
    Map<String, Object> allProvidersDataMap;
    public void getOneProvider() {
        final DatabaseReference providerList = mDatabase.child("providers final");

        providerList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //This give the unique key because our current location immediate children are id generated  by push
                //
                double a = (double)(Math.random()*5);
                DecimalFormat  c = new DecimalFormat("#.0");
                String last = c.format(a);
                float f = Float.parseFloat(last);



                allProvidersMap = (Map<String, Object>) dataSnapshot.getValue();
                if (allProvidersMap.containsKey("KV9iscYBP2gH3y1YHcl") && allProvidersMap.get("KV9iscYBP2gH3y1YHcl") == "FbxkB7gt3WS58ugOLD4MyaPF5MI3") {

                    //Testing with James Mucheru
                    sampleUsername = providerList.child("KV9iscYBP2gH3y1YHcl/FbxkB7gt3WS58ugOLD4MyaPF5MI3/username").getKey();
                }

                ProviderData newData73 = new ProviderData(R.drawable.finalimagetest1,
                        ", Kawangware, Dagoretti, Riara",
                        "Plumber, Electrician",
                        "Crazy thing",
                        "-",
                        f);

                //for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                for (Object obj : allProvidersMap.values()) { //First loop the pushed ids

                        mDatabase.child("Crazy test").push().setValue(obj); //I want to write all userIds registered


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //=========================== End updating one value =========================================






    @Override
    public void onFragmentInteraction(Uri uri) {

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                Toast.makeText(getApplicationContext(),"You want to Login",Toast.LENGTH_LONG).show();
                if(user!= null){
                    mAuth.signOut();
                    item.setTitle("Logout");
                } else {
                    Intent goToLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(goToLogin);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

