package com.example.projekskripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Element;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutionException;

public class Main4Activity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 1;
    ImageView mImageview;
    Button mChooseBtn;
    private static final int PICK_IMAGE = 100;
    Uri ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mImageview = findViewById(R.id.image_view3);
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

    public Matrix getTransformationMatrix(final int srcWidth,
                                                  final int srcHeight,
                                                  final int dstWidth,
                                                  final int dstHeight,
                                                  final boolean maintainAspectRatio) {
        final Matrix matrix = new Matrix();

        if (srcWidth != dstWidth || srcHeight != dstHeight) {
            final float scaleFactorX = dstWidth / (float) srcWidth;
            final float scaleFactorY = dstHeight / (float) srcHeight;

            if (maintainAspectRatio) {
                final float scaleFactor = Math.max(scaleFactorX, scaleFactorY);
                matrix.postScale(scaleFactor, scaleFactor);
            } else {
                matrix.postScale(scaleFactorX, scaleFactorY);
            }
        }
        matrix.invert(new Matrix());
        return matrix;
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private class DownloadFilesTask extends AsyncTask<String,Integer,String > {
        protected String doInBackground(String... urls) {
            OutputStreamWriter outputPost = null;
            InputStream stream = null;
            String result = null;
            try {
               // Log.d("BS64", urls[0]);
                //Log.d("BS64", urls[1]);
                JSONObject data = new JSONObject();
                data.put("img",urls[1]);
                URL url = new URL(urls[0]);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setRequestProperty("Content-Type","application/json");
                client.setDoOutput(true);
                client.setDoInput(true);
                client.connect();

                outputPost = new OutputStreamWriter(client.getOutputStream());
                outputPost.write(data.toString());
                outputPost.flush();

                stream = client.getInputStream();
                result = convertStreamToString(stream);


//                outputPost = new BufferedOutputStream(client.getOutputStream());
//                //writeStream(outputPost);
//                Log.d("OS",outputPost.toString());
//                outputPost.flush();
//                outputPost.close();
            } catch (MalformedURLException e) {
                Log.d("ERROR",e.toString());
            } catch (IOException e) {
                Log.d("ERROR",e.toString());
            } catch (Exception e) {
                Log.d("ERROR",e.toString());
            }
            return result;
        }
        protected void onProgressUpdate(Integer... progress) {
           // setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
           // Log.d("BS64", result);
        }
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
                    mImageview.setImageURI(selectedImage);

                    int inputSize =64;
                    Log.d("TTT", selectedImage.getEncodedPath());
                    Log.d("TTT", selectedImage.getPath());

                    InputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(_context.getContentResolver().openFileDescriptor(selectedImage,"r").getFileDescriptor());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmapRaw = BitmapFactory.decodeStream(inputStream);

                    Matrix matrix = getTransformationMatrix(bitmapRaw.getWidth(), bitmapRaw.getHeight(),
                            inputSize, inputSize, false);

                    int[] intValues = new int[inputSize * inputSize];
                    //int bytePerChannel = tensor.dataType() == DataType.UINT8 ? 1 : BYTES_PER_CHANNEL;
                    //Log.d("TEST", "bytePerChannel : "+bytePerChannel);
                    ByteBuffer imgData = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 8);
                    imgData.order(ByteOrder.nativeOrder());

                    Bitmap bitmap = Bitmap.createBitmap(inputSize, inputSize, Bitmap.Config.ARGB_8888);
                    final Canvas canvas = new Canvas(bitmap);
                    canvas.drawBitmap(bitmapRaw, matrix, null);

                    String bs64 = bitmapToBase64(bitmap);
                    //Log.d("BS64", bs64);
                    //mImageview.setImageBitmap(bitmap);

                    AsyncTask result =  new DownloadFilesTask().execute("http://192.168.43.130:5000/img",bs64);
                    try {
                        Log.d("BS64", result.get().toString());
                    } catch (ExecutionException e) {
                        Log.d("ERROR", e.toString());
                    } catch (InterruptedException e) {
                        Log.d("ERROR", e.toString());
                    }
                    break;
            }


    }

}
