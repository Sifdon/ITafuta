package com.itafuta;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;

public class ServiceProviderActivity extends AppCompatActivity {

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



        ImageView finalUserImage = (ImageView) findViewById(R.id.finalProfilePhoto);
        //TextView finalUserName = (TextView) findViewById(R.id.broughtUserName);
        //TextView finalFabCount = (TextView) findViewById(R.id.broughtFavCount);
        final RatingBar finalRatingBar = (RatingBar) findViewById(R.id.ratingProvider);
        LikeButton mLikeButton = (LikeButton) findViewById(R.id.btnLike);
        assert mLikeButton != null;
        mLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);

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
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("USER_NAME");
        getSupportActionBar().setTitle(userName);
    }
}
