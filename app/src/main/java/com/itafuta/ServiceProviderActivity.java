package com.itafuta;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.HashMap;
import java.util.Map;

public class ServiceProviderActivity extends AppCompatActivity {

    private static final String TAG = "ServiceProviderActivity";

    private DatabaseReference mDatabase;
    ImageView finalUserImage;
    Button btnReport;
    TextView txtProviderContact;

    String providerUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//        int primary = getResources().getColor(R.color.colorPrimary);
//        int secondary = getResources().getColor(R.color.colorAccent);

        SlidrConfig config = new SlidrConfig.Builder()
                .primaryColor(getResources().getColor(R.color.holo_blue_bright))
                .secondaryColor(getResources().getColor(R.color.holo_blue_bright))
                .position(SlidrPosition.LEFT)
                //.position(SlidrPosition.LEFT|RIGHT|TOP|BOTTOM|VERTICAL|HORIZONTAL)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(false)
                .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
                .listener(new SlidrListener(){
                        @Override
                        public void onSlideStateChanged(int state) {

                        }

                        @Override
                        public void onSlideChange(float percent) {

                        }

                        @Override
                        public void onSlideOpened() {

                        }

                        @Override
                        public void onSlideClosed() {

                        }
                    })
                .build();
        Slidr.attach(this, config);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);


        btnReport = (Button) findViewById(R.id.btnReport);

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder.from(ServiceProviderActivity.this)
                        .setType("message/rfc822")
                        .addEmailTo("abc@ygmail.com")
                        .setText("This user is inappropriate")
                        .setChooserTitle("Report user")
                        .startChooser();
            }
        });

        finalUserImage = (ImageView) findViewById(R.id.finalProfilePhoto);
        txtProviderContact = (TextView) findViewById(R.id.txtProviderContact);

        //previewStoredFirebaseImage();

        //TextView finalUserName = (TextView) findViewById(R.id.broughtUserName);
        //TextView finalFabCount = (TextView) findViewById(R.id.broughtFavCount);
        final RatingBar finalRatingBar = (RatingBar) findViewById(R.id.ratingProvider);
        LikeButton mLikeButton = (LikeButton) findViewById(R.id.btnLike);
        assert mLikeButton != null;
        mLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                favouriteProvider();
                Log.d(TAG, "Provider has been FAVORITED by" + providerUid);
                //likeButton.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                unfavouriteProvider();
                Log.d(TAG, "User has UNFAVOURITED the provider");
                //likeButton.setLiked(false);
            }
        });

        //final ActionProcessButton mLike = (ActionProcessButton) findViewById(R.id.btnLike);

//        assert mFavourite != null;
//        assert mLike != null;
//        mLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mLike.setProgress(5);
//                mFavourite.setMode(ActionProcessButton.Mode.PROGRESS);
//            }
//        });



//        Bundle extras =  getIntent().getExtras();
//        //Getting the image brought
////        byte[] byteArray = extras.getByteArray("MyProviderPicture");
////        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
////        finalUserImage.setImageBitmap(bmp);
//
//        finalUserName.setText(extras.getString("MyUserName"));
////        finalFabCount.setText(extras.getString("MyFavCount"));




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final SimpleRatingBar mRatingProvider = (SimpleRatingBar) findViewById(R.id.btnRating);
        SimpleRatingBar.AnimationBuilder builder  = mRatingProvider.getAnimationBuilder()
                .setDuration(2000);
        builder.start();

        mRatingProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ServiceProviderActivity.this, ""+ mRatingProvider.getRating(), Toast.LENGTH_SHORT).show();

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //============== BUNDLE EXTRAS =====================
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("USER_NAME");
        String profilePhoto = extras.getString("PROVIDER_PHOTO");
        providerUid = extras.getString("PROVIDER_UID");

        //===============END BUNDLE ETXRAS ============================
        Toast.makeText(ServiceProviderActivity.this, "*Provider id*"+ providerUid +"**", Toast.LENGTH_SHORT).show();

        String providerContact;
        if(extras.getString("PROVIDER_CONTACT") != null){
            providerContact = extras.getString("PROVIDER_CONTACT");
            //txtProviderContact.setText(providerContact);
            txtProviderContact.setText(providerUid);
            getSupportActionBar().setSubtitle(providerContact);
        }else {
            txtProviderContact.setText("User doesn't have contact");
            getSupportActionBar().setSubtitle("No contact!");
        }
        byte[] profileImageAsBytes = Base64.decode(profilePhoto.getBytes(), Base64.DEFAULT);
        finalUserImage.setImageBitmap(BitmapFactory.decodeByteArray(profileImageAsBytes, 0, profileImageAsBytes.length));
        getSupportActionBar().setTitle(userName);

    }

    ////===========================
    //Fetches images and displays it on the user profile
    private void previewStoredFirebaseImage() {
        mDatabase.child("pic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String base64Image = (String) snapshot.getValue();
                byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
                finalUserImage.setImageBitmap(
                        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                );
                System.out.println("Downloaded image with length: " + imageAsBytes.length);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
//
//            @Override
//            public void onCancelled(FirebaseError error) {}
        });
    }
    ////===========================


    //========= Adding to favourites =====================================
    private void favouriteProvider(){
        //Loop the user id to favourite
        mDatabase.child("providers final").orderByChild(providerUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot pushedIdSnaps) {
                for (DataSnapshot userIdSnaps : pushedIdSnaps.getChildren()) {
                    if (userIdSnaps.hasChild(providerUid)) {
                        //Toast.makeText(ServiceProviderActivity.this, userIdSnaps.child("username").getValue().toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ServiceProviderActivity.this, "It has that item", Toast.LENGTH_SHORT).show();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("favourites", true);
                        userIdSnaps.getRef().child(providerUid).updateChildren(map);
                        Toast.makeText(ServiceProviderActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();

                    }
                }


                /*
                for (DataSnapshot userIdSnaps : pushedIdSnaps.getChildren()){
                    if(userIdSnaps.hasChild(providerUid)){
                        //Toast.makeText(ServiceProviderActivity.this, userIdSnaps.child("username").getValue().toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ServiceProviderActivity.this, "It has that item", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ServiceProviderActivity.this, "Not found!", Toast.LENGTH_SHORT).show();
                    }

                    /*
                    if((pushedIdSnaps.hasChild(providerUid)) && (pushedIdSnaps.child(providerUid).child("favourites").getValue().equals(""))) {
                        Map<String, Object> fav = new HashMap<String, Object>();
                        fav.put("favourites", "ONE FAVORITE");
                        pushedIdSnaps.child(providerUid).child("favourites").getRef().setValue("ONE FAVOURITE");
                        //userIdSnaps.child((providerUid)).getRef().updateChildren(fav);
                    }

                }
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(ServiceProviderActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
    }
    private void unfavouriteProvider(){
        //Remove the item entered
        mDatabase.child("providers final").orderByChild(providerUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot pushedIdSnaps) {
                for (DataSnapshot userIdSnaps : pushedIdSnaps.getChildren()) {
                    if (userIdSnaps.hasChild(providerUid)) {
                        //Toast.makeText(ServiceProviderActivity.this, userIdSnaps.child("username").getValue().toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ServiceProviderActivity.this, "It has that item", Toast.LENGTH_SHORT).show();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("favourites", false);
                        userIdSnaps.getRef().child(providerUid).updateChildren(map);
                        Toast.makeText(ServiceProviderActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(ServiceProviderActivity.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
    }



    //========= Adding to favourites =====================================

}
