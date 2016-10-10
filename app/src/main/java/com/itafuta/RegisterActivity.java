package com.itafuta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {


//    FirebaseDatabase firebase;


    //-------------CAMERA--------
    private ArrayList<Image> images = new ArrayList<>();

    private int REQUEST_CODE_PICKER = 2000;
    ImageView mLoadedImage;
    //-------------CAMERA--------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_dialog_layout);

//        Firebase.setAndroidContext(this);
//        firebase = new Firebase("https://*firebase-url*.firebaseio.com/");


        //------------------Submit button
        Button submitBtn = (Button) findViewById(R.id.btn_submit_registration);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "Registration submitted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_plumber:
                if (checked){

                }
                // Put some meat on the sandwich
                else{

                }
                // Remove the meat
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            // do your logic ....

            if (images.size()!= 0) {

                Image x = images.get(0);
                String filePath = x.getPath();
                Toast.makeText(RegisterActivity.this, ""+x.getPath(), Toast.LENGTH_SHORT).show();

                mLoadedImage.setVisibility(View.VISIBLE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                mLoadedImage.setImageBitmap(bitmap);
            }

        }
    }

}
