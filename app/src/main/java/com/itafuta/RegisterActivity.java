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

import com.emmasuzuki.easyform.EasyFormEditText;
import com.emmasuzuki.easyform.EasyTextInputLayout;
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
    private static final String TAG = "RegisterActivity";

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ProviderRegistrationData registrationContent;

    Bitmap bitmap;

    //-------------CAMERA--------
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<Image> imagesIdFront = new ArrayList<>();
    private ArrayList<Image> imagesIdBack = new ArrayList<>();

    private int REQUEST_CODE_PICKER = 2000;
    private int REQUEST_CODE_PICKER_ID_FRONT = 2001;
    private int REQUEST_CODE_PICKER_ID_BACK = 2002;
    ImageView mLoadedImage;
    Image x, y, z;



    String filePath;
    //-------------CAMERA--------


    //Images
    //String base64Image;
    String base64ProfileImage;
    String base64IdFrontImage;
    String base64IdBackImage;

    int checksCount;

    //Widgets references
    EasyFormEditText editSignupEmail;
    EasyFormEditText editSignupPass;
    EasyFormEditText editSignupPassConfirm;
    EasyFormEditText editSignupUsername;

    Button btnUploadPhoto;
    Button btnUploadIdFront;
    Button btnUploadIdBack;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_dialog_layout);
        Log.d(TAG, "You got into RegisterActivity");

        registrationContent = new ProviderRegistrationData();
        //ProviderRegistrationData(int  mImage, int mIdFront, int mIdBack, String mLocation, String mDetails, String mName, String mFavCount, float mProvRate)




        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.keepSynced(true);
        //mDbRef = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "Instantianted firebase authentication");

        //Reference all widgets
        editSignupEmail = (EasyFormEditText) findViewById(R.id.edit_signup_email);
        editSignupPass = (EasyFormEditText) findViewById(R.id.edit_password);
        editSignupPassConfirm = (EasyFormEditText) findViewById(R.id.edit_password_confirm);

        btnUploadPhoto = (Button) findViewById(R.id.btnUploadProfPhoto);
        btnUploadIdBack = (Button) findViewById(R.id.btnUploadImageIdBack);
        btnUploadIdFront = (Button) findViewById(R.id.btnUploadImageIdFront);
        btnSignup = (Button) findViewById(R.id.btn_signup);

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickingProfileImage();
            }
        });

        btnUploadIdFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickingIdFrontImage();
            }
        });

        btnUploadIdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickingIdBackImage();
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });



        /*
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
        */


        //Addingprovider's data on launch



        //==========================================================================================
        //=================== Start where I was updating my things =================================
        /*

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



        */
        //====================End This was updating my things=======================================
        //==========================================================================================


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

        /*
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




            }
        });
        //======== end submit button
        */


        mLoadedImage = (ImageView) findViewById(R.id.imgLoadedImage);

        //-------------Upload inmages buttons -----------------------------------
        /*
        Button upLoadImage = (Button) findViewById(R.id.btnUploadImage);


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
                        .limit(1) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .origin(images) // original selected images, used in multi mode
                        .start(REQUEST_CODE_PICKER); // start image picker activity with request code

            }
        });
        */
    }



    //============== LAUNCHING AN IMAGE PICKER ACTIVITY
    public void pickingProfileImage(){
        ImagePicker.create(RegisterActivity.this)
                .folderMode(true) // folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(3) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .origin(images) // original selected images, used in multi mode
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }
    public void pickingIdFrontImage(){
        ImagePicker.create(RegisterActivity.this)
                .folderMode(true) // folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(3) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .origin(images) // original selected images, used in multi mode
                .start(REQUEST_CODE_PICKER_ID_FRONT); // start image picker activity with request code
    }
    public void pickingIdBackImage(){
        ImagePicker.create(RegisterActivity.this)
                .folderMode(true) // folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(3) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .origin(images) // original selected images, used in multi mode
                .start(REQUEST_CODE_PICKER_ID_BACK); // start image picker activity with request code
    }

    //================= END IMAGE PICKERS =================================================





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

        base64ProfileImage = "--";
        base64IdFrontImage = "==";
        base64IdBackImage = "**";

        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {

            images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            // do your logic ....

            if (images.size()!= 0) {
                int lastItem = images.size() - 1;
                x  = images.get(lastItem);
                filePath = x.getPath();
                Toast.makeText(RegisterActivity.this, ""+x.getPath(), Toast.LENGTH_SHORT).show();

                mLoadedImage.setVisibility(View.VISIBLE);

                //Get the profile image base64 image as a string

                getProfileImage();
                previewStoredFirebaseImage();
                mLoadedImage.setImageBitmap(bitmap);

            }
        }
        if (requestCode == REQUEST_CODE_PICKER_ID_FRONT && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            // do your logic ....

            if (images.size()!= 0) {

                int lastItem = images.size() - 1;
                x  = images.get(lastItem);
                filePath = x.getPath();
                Toast.makeText(RegisterActivity.this, ""+x.getPath(), Toast.LENGTH_SHORT).show();

                mLoadedImage.setVisibility(View.VISIBLE);

                //Get the ID FRONT image base64 image as a string
                getIdFrontImage();
            }
        }
        if (requestCode == REQUEST_CODE_PICKER_ID_BACK && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            // do your logic ....

            if (images.size()!= 0) {

                int lastItem = images.size() - 1;
                x  = images.get(lastItem);
                filePath = x.getPath();
                Toast.makeText(RegisterActivity.this, ""+x.getPath(), Toast.LENGTH_SHORT).show();

                mLoadedImage.setVisibility(View.VISIBLE);

                //Get the ID FRONT image base64 image as a string
                getIdBackImage();
            }
        }
    }


    /*
    //================ STORING BASE64IMAGE TO FIREBASE ==============
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
        base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

        // we finally have our base64 string version of the image, save it.
        mDatabase.child("pic").setValue(base64Image);
        System.out.println("Stored image with length: " + bytes.length);
    }
    //================ STORING BASE64IMAGE TO FIREBASE ==============
    */
    // ================ STORING BASE64IMAGE TO FIREBASE ==============
    private void getProfileImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
        options.inSampleSize = 16; // shrink it down otherwise we will use stupid amounts of memory
        //options.inSampleSize = 2; // This wa eaating my memory


        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        //Base 64 image
        base64ProfileImage = Base64.encodeToString(bytes, Base64.DEFAULT);
        temporalImageUpload(base64ProfileImage);
        System.out.println("Stored image with length: " + bytes.length);
    }
    //================ STORING BASE64IMAGE TO FIREBASE ==============


    private void getIdFrontImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
        options.inSampleSize = 16; // shrink it down otherwise we will use stupid amounts of memory
        //options.inSampleSize = 2; // This wa eaating my memory


        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        // we finally have our base64 string version of the image.
        base64IdFrontImage = Base64.encodeToString(bytes, Base64.DEFAULT);
        temporalImageUpload(base64IdFrontImage);
    }

    private void getIdBackImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
        options.inSampleSize = 16; // shrink it down otherwise we will use stupid amounts of memory
        //options.inSampleSize = 2; // This wa eaating my memory


        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        // we finally have our base64 string version of the image, save it.
        base64IdBackImage = Base64.encodeToString(bytes, Base64.DEFAULT);
        temporalImageUpload(base64IdBackImage);

    }

    public void temporalImageUpload(String tempImage){
        DatabaseReference tempRef = mDatabase.child("images loading");

        if (tempImage.equals(base64ProfileImage)){
            tempRef.child("tempProfileImage").setValue(base64ProfileImage);
        }
        if (tempImage.equals(base64IdFrontImage)) {
            tempRef.child("tempIdFront").setValue(base64IdFrontImage);
        }
        if (tempImage.equals(base64IdBackImage)) {
            tempRef.child("tempIdBack").setValue(base64IdBackImage);
        }
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
        /*
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
        */
    }


    public void validate(){
        Log.d(TAG, "Validating user input for sign-up");
        String email = editSignupEmail.getText().toString();
        String password = editSignupPass.getText().toString();
        String passwordConfirm = editSignupPassConfirm.getText().toString();

        email = email.trim();
        password = password.trim();

        if(email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Make sure requred fields are not empty!", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(passwordConfirm)){
            Toast.makeText(RegisterActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        }else{
            //Validation success
            //registrationContent.setProvName(email);

            mDatabase.child("mac").setValue(email);

            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Log.d(TAG, "Failed to authenticate");
                                Toast.makeText(RegisterActivity.this, "Auth failed",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                Log.d(TAG, "User validated. Authenticated successfully!");

                                registerData();

                                mAuth.signOut();
                                Intent goToLogin = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(goToLogin);
                            }

                        }
                    });
        }
    }


    public void registerData(){
        Toast.makeText(RegisterActivity.this, base64IdBackImage, Toast.LENGTH_SHORT).show();
        registrationContent.setProvImage(base64ProfileImage);
        registrationContent.setProvIdFront(base64IdFrontImage);
        registrationContent.setProviderIdBack(base64IdBackImage);
        registrationContent.setProvLocation("LIMURU, kikuyu, Kawangware");
        registrationContent.setProvDetails("Carpenter");
        registrationContent.setProvName("My Name");
        registrationContent.setProvFavCount("true");
        registrationContent.setProvRate(6);

        Map<String, Object> postValues = new HashMap<>();
        postValues.put("profPhoto", registrationContent.getProvImage());
        postValues.put("username", registrationContent.getProvName());
        postValues.put("idFront", registrationContent.getProvIdFront());
        postValues.put("idback", registrationContent.getProviderIdBack());
        postValues.put("location",  registrationContent.getProvLocation());
        postValues.put("details", registrationContent.getProvName());
        postValues.put("favourite", registrationContent.getProvFavCount());
        postValues.put("rating", registrationContent.getProvRate());

        String uid= mAuth.getCurrentUser().getUid();
        mDatabase.child("providers final").push().child(uid).updateChildren(postValues);
    }
}
