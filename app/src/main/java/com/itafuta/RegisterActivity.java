package com.itafuta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.RangeValueIterator;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emmasuzuki.easyform.EasyFormEditText;
import com.emmasuzuki.easyform.EasyTextInputLayout;
import com.google.android.gms.common.data.DataBufferObserver;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    DatabaseReference tempRef; //Where to store temporary data
    String tempRefKey;  //The key to the temporary data

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

    String base64ProfileImageOnline = "";
    String base64IdFrontImageOnline = "";
    String base64IdBackImageOnline = "";


    String finalUploadLocation;
    Map<String, Object> myMainData; // List of final occupations
    Map<String, Object> myOccupationsFinalUpload; // List of final occupations


    int checksCount = 0;
    int checksLimitCount;

    //Widgets references
    EasyFormEditText editSignupName;
    EasyFormEditText editSignupEmail;
    EasyFormEditText editSignupContact;
    EasyFormEditText editSignupPass;
    EasyFormEditText editSignupPassConfirm;


    Button btnUploadPhoto;
    Button btnUploadIdFront;
    Button btnUploadIdBack;
    Button btnSignup;

    Spinner spnLocation;

    Map<String, Object> myOccupations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_dialog_layout);
        Log.d(TAG, "You got into RegisterActivity");

        registrationContent = new ProviderRegistrationData();
        //ProviderRegistrationData(int  mImage, int mIdFront, int mIdBack, String mLocation, String mDetails, String mName, String mFavCount, float mProvRate)




        mDatabase = FirebaseDatabase.getInstance().getReference();
        tempRef = mDatabase.child("images loading").push(); //Temporary registration data place
        tempRefKey = tempRef.getKey();
        myOccupations = new HashMap<>();
        myMainData = new HashMap<>();
        myOccupationsFinalUpload = new HashMap<>();


        //mDatabase.keepSynced(true);
        //mDbRef = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "Instantianted firebase authentication");

        //Reference all widgets
        editSignupName = (EasyFormEditText) findViewById(R.id.edit_signup_name);
        editSignupContact = (EasyFormEditText) findViewById(R.id.edit_signup_contact);
        editSignupEmail = (EasyFormEditText) findViewById(R.id.edit_signup_email);
        editSignupPass = (EasyFormEditText) findViewById(R.id.edit_password);
        editSignupPassConfirm = (EasyFormEditText) findViewById(R.id.edit_password_confirm);

        btnUploadPhoto = (Button) findViewById(R.id.btnUploadProfPhoto);
        btnUploadIdBack = (Button) findViewById(R.id.btnUploadImageIdBack);
        btnUploadIdFront = (Button) findViewById(R.id.btnUploadImageIdFront);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        spnLocation = (Spinner) findViewById(R.id.spinnerPlacesRegistration);

        //upload profile photo
        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickingProfileImage();
            }
        });

        //Load front id image
        btnUploadIdFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickingIdFrontImage();
            }
        });

        //load back image
        btnUploadIdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickingIdBackImage();
            }
        });

        //Get item from the location spinner
        spnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemPlace = adapterView.getSelectedItem().toString();
                Toast.makeText(RegisterActivity.this, itemPlace, Toast.LENGTH_SHORT).show();
                tempRef.child("location").setValue(itemPlace);
                finalUploadLocation = itemPlace;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Submit registration data at sign up
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

    //======================Listen for extra clicked item===========================================
    CheckBox[] cba;
    CompoundButton.OnCheckedChangeListener cbListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkEnoughAndMakeDisabled(cba);
        }
    };

    private void checkEnoughAndMakeDisabled(CheckBox checkBoxes[]){
        checksLimitCount = 0;
        for (CheckBox cb:checkBoxes){
            cb.setEnabled(true);
            if (cb.isChecked()) checksLimitCount++;
        }
        //your variable
        if (3 <= checksLimitCount) {
            for (CheckBox cb:checkBoxes){
                if (!cb.isChecked())cb.setEnabled(false);
            }
            Toast.makeText(RegisterActivity.this, "Only three roles allowed", Toast.LENGTH_SHORT).show();
        }
    }
    //======================Listen for extra clicked item===========================================



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
                        myOccupations.put("plumber", true);
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Put some meat on the sandwich
                    else {
//                    checksCount = checksCount - 1;
                        myOccupations.remove("plumber");
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the meat
                    break;
                case R.id.checkbox_carpenter:
                    if (checked) {
                        checksCount ++;
                        myOccupations.put("carpenter", true);
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Put some meat on the sandwich
                    else {
//                    checksCount = checksCount - 1;
                        myOccupations.remove("carpenter");
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the meat
                    break;
                case R.id.checkbox_painter:
                    if (checked) {
                        checksCount ++;
                        myOccupations.put("painter", true);
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Put some meat on the sandwich
                    else {
//                    checksCount = checksCount - 1;
                        myOccupations.remove("painter");
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the meat
                    break;
                case R.id.checkbox_houseagents:
                    if (checked) {
                        myOccupations.put("house agent", true);
                        tempRef.child("occupation").updateChildren(myOccupations);
                        checksCount = checksCount ++;
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Put some meat on the sandwich
                    else {
//                    checksCount = checksCount - 1;
                        myOccupations.remove("house agent");
                        Toast.makeText(RegisterActivity.this, checksCount + "", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the meat
                    break;
            }



        //======================== Set the checkbox limits to 3 ================================
        cba = new CheckBox[]{
                (CheckBox) findViewById(R.id.checkbox_plumber),
                (CheckBox) findViewById(R.id.checkbox_carpenter),
                (CheckBox) findViewById(R.id.checkbox_painter),
                (CheckBox) findViewById(R.id.checkbox_houseagents),
                (CheckBox) findViewById(R.id.checkbox_carpetcleaning),
                (CheckBox) findViewById(R.id.checkbox_mamanguo),
                (CheckBox) findViewById(R.id.checkbox_garbagecollectors),
                (CheckBox) findViewById(R.id.checkbox_mechanic),
                (CheckBox) findViewById(R.id.checkbox_mover),
                (CheckBox) findViewById(R.id.checkbox_waterdelivery)
        };
        //here set onCheckedChange for all your checkboxes
        for (CheckBox cb:cba) {
            cb.setOnCheckedChangeListener(cbListener);
        }

        //========================  End the checked listener =====================================

    /*
    else{
            Toast.makeText(RegisterActivity.this, "Only three roles allowed", Toast.LENGTH_SHORT).show();
        }
    */
    }

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
        base64ProfileImageOnline = base64ProfileImage;
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
        base64IdFrontImageOnline = base64IdFrontImage;
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
        base64IdBackImageOnline = base64IdBackImage;
        temporalImageUpload(base64IdBackImage);
    }

    public void temporalImageUpload(String tempImage){
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

    String fullname;
    String contact;
    public void validate(){
        Log.d(TAG, "Validating user input for sign-up");
        fullname = editSignupName.getText().toString();
        contact = editSignupContact.getText().toString();
        String email = editSignupEmail.getText().toString();
        String password = editSignupPass.getText().toString();
        String passwordConfirm = editSignupPassConfirm.getText().toString();

        email = email.trim();
        password = password.trim();

        if(email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Make sure requred fields are not empty!", Toast.LENGTH_SHORT).show();
        }
        else  if(base64IdFrontImageOnline.isEmpty() || base64IdFrontImageOnline.isEmpty() || base64IdBackImageOnline.isEmpty() ){
            Toast.makeText(RegisterActivity.this, "Load all images!", Toast.LENGTH_SHORT).show();
        }

        else if(!password.equals(passwordConfirm)){
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

        // Get all the temporary data from the reference
        //And update it in the registration content during signup
        Log.d(TAG, "+++++++++++");
        getThatTempData();

        Log.d(TAG, "&&&&&&&&&&&&&&&&&&&&++++++++++++++++++++++++++++++++++++&&&&&&&&&&&&&&&&&&&&&");
        Log.d(TAG, "&&&&&&&&&&&&&&&&&&&&++++++++++++++++++++++++++++++++++++&&&&&&&&&&&&&&&&&&&&&");
        Log.d(TAG, "&&&&&&&&&&&&&&&&&&&&+++++++++++++++Images LOG+++++++++++++++++++++&&&&&&&&&&&&&&&&&&&&&");
        Log.d(TAG, base64ProfileImageOnline);
        Log.d(TAG, base64IdFrontImageOnline);
        Log.d(TAG, base64IdBackImageOnline);
        Log.d(TAG, "&&&&&&&&&&&&&&&&&&&&+++++++++++++++++IMAGES LOG+++++++++++++++++++&&&&&&&&&&&&&&&&&&&&&");
        Log.d(TAG, "&&&&&&&&&&&&&&&&&&&&++++++++++++++++++++++++++++++++++++&&&&&&&&&&&&&&&&&&&&&");
        Log.d(TAG, "&&&&&&&&&&&&&&&&&&&&++++++++++++++++++++++++++++++++++++&&&&&&&&&&&&&&&&&&&&&");

        registrationContent.setProvImage(base64ProfileImageOnline);
        registrationContent.setProvIdFront(base64IdFrontImageOnline);
        registrationContent.setProviderIdBack(base64IdBackImageOnline);
        registrationContent.setProvLocation(finalUploadLocation);

        //loadingOccupations();

        registrationContent.setProvOccupation(myOccupations); //This is a map of occupations
        registrationContent.setProvName(fullname);
        registrationContent.setProvFavCount("true");
        registrationContent.setProvRate(6);

        Map<String, Object> postValues = new HashMap<>();
        postValues.put("profPhoto", registrationContent.getProvImage());
        postValues.put("username", registrationContent.getProvName());
        postValues.put("idFront", registrationContent.getProvIdFront());
        postValues.put("idback", registrationContent.getProviderIdBack());
        postValues.put("location",  registrationContent.getProvLocation());
        postValues.put("occupation", registrationContent.getProvOccupation());
        postValues.put("favourite", registrationContent.getProvFavCount());
        postValues.put("rating", registrationContent.getProvRate());

        String uid= mAuth.getCurrentUser().getUid(); //Get the userid as at registration so that he is registered
        mDatabase.child("providers final").push().child(uid).updateChildren(postValues);

        //Update the occupations at the same time
        // occupationsList > [specificOccupation] > key
        if (!myOccupations.isEmpty()){
            //Key represent the key of each item in the myOccupations map
            // It represents a string of the name of the /
            for (String key : myOccupations.keySet()){
                mDatabase.child("occupations").child(key).child(uid).setValue(true);
            }
        }
    }

    public void getThatTempData(){
        Log.d(TAG, "=============Getting you temporary data=================");



        tempRef.addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               Log.d(TAG, "==============Inside Temporary data ===================");
                                               Log.d(TAG, "==============DATA SNASPSHOT Is ===" + dataSnapshot.getValue());
                                               myMainData = (Map<String, Object>) dataSnapshot.getValue();
                                               myOccupationsFinalUpload = (Map<String, Object>) dataSnapshot.getValue();
                                               Log.d(TAG, myMainData.get("location").toString());
                                               Log.d(TAG, myMainData.get("tempProfileImage").toString());
                                               Log.d(TAG, myMainData.get("tempIdFront").toString());
                                               Log.d(TAG, myMainData.get("tempIdBack").toString());
                                               /*
                                               registrationContent.setProvImage(base64ProfileImage);
                                               registrationContent.setProvIdFront(base64IdFrontImage);
                                               registrationContent.setProviderIdBack(base64IdBackImage);
                                               registrationContent.setProvLocation(finalUploadLocation);
                                               */

                                               /*
                                               finalUploadLocation = myMainData.get("location").toString();
                                               base64ProfileImageOnline = myMainData.get("tempProfileImage").toString();
                                               base64IdFrontImageOnline = myMainData.get("tempIdFront").toString();
                                               base64IdBackImageOnline = myMainData.get("tempIdBack").toString();
                                               */

                                               Log.d(TAG, "================= The above Data from your tempDta=========");


                                               /*
                                               if (("occupation").equals(dataSnapshot.getKey())){
                                                   //get items under occupations
                                                   //It is a list so get it wisely
                                                   loadingOccupations();
                                               }*/
                                           }

                                           @Override
                                           public void onCancelled(DatabaseError databaseError) {

                                           }
                                       }
                );

    }


    public void loadingOccupations(){
        //============== Adding final upload occupation code ==================
        final DatabaseReference mRefOccupation = mDatabase.child("image loading").child(tempRef.getKey()).child("occupation");
        mRefOccupation.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot occupationSnapshot) {
                        //Get a map of the occupations
                        //And turn it to a list
                        for(DataSnapshot myData : occupationSnapshot.getChildren()) {

                            myOccupationsFinalUpload = (Map<String, Object>)myData.getValue();
                            Toast.makeText(RegisterActivity.this, myOccupationsFinalUpload.get("plumber").toString(), Toast.LENGTH_SHORT).show();

                            //myOccupationsFinalUpload = new ArrayList<>(td.values());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        //============== Adding final upload occupation code ==================
    }
}
