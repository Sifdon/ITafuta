package com.itafuta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    Bitmap bitmap;

    //-------------CAMERA--------
    private ArrayList<Image> images = new ArrayList<>();

    private int REQUEST_CODE_PICKER = 2000;
    ImageView mLoadedImage;

    Image x;

    String filePath;
    //-------------CAMERA--------

    int checksCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_dialog_layout);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        //mDbRef = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword("james2@yahoo.com", "james1234")
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Auth failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        //Addingprovider's data on launch
        FirebaseUser user = mAuth.getCurrentUser();



        Map<String, Object> provider = new HashMap<String, Object>();



        provider.put("email", user.getEmail());
        provider.put("name", "James Odongo");
        provider.put("cellphone", +254-87654321);
        provider.put("profPhoto", "This is some image");
        provider.put("rating", 0);

        //provider.put("region", "Kawangware");
          Map<String, Object> providerOccupation = new HashMap<String, Object>();
          providerOccupation.put("1", "Ruiru");
          providerOccupation.put("2", "Wanyee");
          providerOccupation.put("3", "Kikuyu");
          mDatabase.child("providers").child(user.getUid()).child("region").setValue(providerOccupation);

        mDatabase.child("providers").child(user.getUid()).updateChildren(provider);
        DatabaseReference jj = mDatabase.child("providers").child(user.getUid()).child("region").getRef();
        jj.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int x = (int) dataSnapshot.getChildrenCount();

                Toast.makeText(RegisterActivity.this, x+"" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mAuth.signInWithEmailAndPassword("james2@yahoo.com", "james1234")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            //Incase you get error signing in
                            Toast.makeText(RegisterActivity.this, "Error in signing in", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            //TextView mText = (TextView) findViewById(R.id.txtDummy);
                            //mText.setText("");
                            Toast.makeText(RegisterActivity.this, "You have been signed in james", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        if (mAuth.getCurrentUser() != null) {
            // User is logged in
            TextView mText = (TextView) findViewById(R.id.txtDummy);

            FirebaseUser cUser = mAuth.getCurrentUser();
            mText.setText(cUser.getDisplayName());
        }


        //signInAnonymously();
        //-----------------------------

        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    TextView mDummy = (TextView) findViewById(R.id.txtDummy);
                    //user.getDisplayName();
                    mDummy.setText(user.getUid());

                    Toast.makeText(RegisterActivity.this, "You were authenticated", Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    TextView mDummy = (TextView) findViewById(R.id.txtDummy);
                    mDummy.setText(R.string.app_name);

                    Toast.makeText(RegisterActivity.this, "Not authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        };
        //----------------------

        /*
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(RegisterActivity.this, "You were authenticated", Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(RegisterActivity.this, "Not authenticated", Toast.LENGTH_SHORT).show();

                }
                // ...
            }
        };
        */

        //------------------Submit button
        Button submitBtn = (Button) findViewById(R.id.btn_submit_registration);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "Registration submitted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                //signInAnonymously();
                mAuth.createUserWithEmailAndPassword("james@yahoo.com", "james1234")
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Auth failed",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                /*
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // User is signed in
                            Toast.makeText(RegisterActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        } else {
                            // User is signed out
                            Toast.makeText(RegisterActivity.this, "This user is still ANONYMOUS", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "onAuthStateChanged:signed_out");
                        }
                        // ...
                    }
                };
                */



            }
        });




        Button upLoadImage = (Button) findViewById(R.id.btnUploadImage);
        mLoadedImage = (ImageView) findViewById(R.id.imgLoadedImage);

        assert upLoadImage!= null;
        upLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(RegisterActivity.this, "REGISTRATION SUBMITTED", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(RegisterActivity.this,  CategoriesActivity.class);
//                startActivity(intent);
                ImagePicker.create(RegisterActivity.this)
                        .folderMode(true) // folder mode (false by default)
                        .folderTitle("Folder") // folder selection title
                        .imageTitle("Tap to select") // image selection title
                        .single() // single mode
                        .multi() // multi mode (default mode)
                        .limit(10) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .origin(images) // original selected images, used in multi mode
                        .start(REQUEST_CODE_PICKER); // start image picker activity with request code

            }
        });


    }




    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        checksCount = 0;//clicks

        //if (checksCount <= 3) {
            // Check which checkbox was clicked
            switch (view.getId()) {

                case R.id.checkbox_plumber:
                    if (checked) {
                        checksCount ++;
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Put some meat on the sandwich
                    else {
//                    checksCount = checksCount - 1;
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the meat
                    break;
                case R.id.checkbox_carpenter:
                    if (checked) {
                        checksCount ++;
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Put some meat on the sandwich
                    else {
//                    checksCount = checksCount - 1;
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the meat
                    break;
                case R.id.checkbox_painter:
                    if (checked) {
                        checksCount ++;
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Put some meat on the sandwich
                    else {
//                    checksCount = checksCount - 1;
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the meat
                    break;
                case R.id.checkbox_houseagents:
                    if (checked) {
                        checksCount = checksCount ++;
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Put some meat on the sandwich
                    else {
//                    checksCount = checksCount - 1;
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the meat
                    break;
            }
        }
    /*

    else{
            Toast.makeText(RegisterActivity.this, "Only three roles allowed", Toast.LENGTH_SHORT).show();
        }
    }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            // do your logic ....

            if (images.size()!= 0) {

                x  = images.get(0);
                filePath = x.getPath();
                Toast.makeText(RegisterActivity.this, ""+x.getPath(), Toast.LENGTH_SHORT).show();

                mLoadedImage.setVisibility(View.VISIBLE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                bitmap = BitmapFactory.decodeFile(filePath, options);
                storeImageToFirebase();
                previewStoredFirebaseImage();
                mLoadedImage.setImageBitmap(bitmap);

            }

        }
    }


    private void storeImageToFirebase() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
        options.inSampleSize = 16; // shrink it down otherwise we will use stupid amounts of memory
        //options.inSampleSize = 2; // This wa eaating my memory


        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

        // we finally have our base64 string version of the image, save it.
        mDatabase.child("pic").setValue(base64Image);
        System.out.println("Stored image with length: " + bytes.length);
    }
    //Fetchs images and displays it
    private void previewStoredFirebaseImage() {
        mDatabase.child("pic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String base64Image = (String) snapshot.getValue();
                byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
                mLoadedImage.setImageBitmap(
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

    private void signInAnonymously() {

        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(RegisterActivity.this, "I have authenticated you",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END signin_anonymously]
    }


}
