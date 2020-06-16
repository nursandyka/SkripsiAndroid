package com.example.projekskripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.concurrent.ExecutionException;

public class SelectFromGalleryActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 1;
    ImageView mImageView;
    Button mChooseBtn;
    private static final int PICK_IMAGE = 100;
    Uri ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mImageView = findViewById(R.id.image_view3);
        mChooseBtn = findViewById(R.id.pilihgambar);

        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
    public void openGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        Context _context = getApplicationContext();
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    //Show image
                    mImageView.setImageURI(selectedImage);

                    //Target image size
                    int inputSize =64;

                    //resize image and convert to base64
                    String bs64 = Helper.resizeImageToBase64(_context,selectedImage,inputSize);

                    try {
                        // send bs64 to server and get result
                        AsyncTask result =  new ServerTask().execute(getString(R.string.server_url),bs64);
                        Helper.Log(result.get().toString());
                    } catch (ExecutionException e) {
                        Helper.Error(e.toString());
                    } catch (InterruptedException e) {
                        Helper.Error(e.toString());
                    } catch (Exception e){
                        Helper.Error(e.toString());
                    }
                    break;
            }


    }

}
