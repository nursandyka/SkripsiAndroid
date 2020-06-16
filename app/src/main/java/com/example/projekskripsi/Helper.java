package com.example.projekskripsi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Helper {

    public static void Log(String msg){
        Log.d("LOG", msg);
    }

    public static void Error(String msg){
        Log.d("ERROR", msg);
    }

    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Matrix getTransformationMatrix(final int srcWidth, final int srcHeight, final int dstWidth, final int dstHeight, final boolean maintainAspectRatio) {
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

    public static String resizeImageToBase64(Context ctx, Uri imageUri, int imageSize){
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(ctx.getContentResolver().openFileDescriptor(imageUri,"r").getFileDescriptor());
        } catch (FileNotFoundException e) {
            Helper.Error(e.toString());
        }
        Bitmap bitmapRaw = BitmapFactory.decodeStream(inputStream);

        Matrix matrix = getTransformationMatrix(bitmapRaw.getWidth(), bitmapRaw.getHeight(),
                imageSize, imageSize, false);

        int[] intValues = new int[imageSize * imageSize];
        //int bytePerChannel = tensor.dataType() == DataType.UINT8 ? 1 : BYTES_PER_CHANNEL;
        //Log.d("TEST", "bytePerChannel : "+bytePerChannel);
        ByteBuffer imgData = ByteBuffer.allocateDirect(1 * imageSize * imageSize * 3 * 8);
        imgData.order(ByteOrder.nativeOrder());

        Bitmap bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmapRaw, matrix, null);

        return bitmapToBase64(bitmap);
    }

}
