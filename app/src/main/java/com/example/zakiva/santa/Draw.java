package com.example.zakiva.santa;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import com.example.zakiva.santa.Models.*;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zakiva.santa.Models.MainDrawingView;
import com.google.android.gms.vision.text.Text;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Draw extends AppCompatActivity {

    public static final String TAG = ">>>>>>>Debug: ";
    public static int SIZE;
    public static int JUMP;
    public int randomImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        SIZE = MainDrawingView.SIZE;
        JUMP = MainDrawingView.JUMP;
        ArrayList <Integer> images = new ArrayList<Integer>(Arrays.asList(R.drawable.bone700sq, R.drawable.heart700sq, R.drawable.house700sq, R.drawable.nike700sq, R.drawable.tree700sq));
        Collections.shuffle(images);
        randomImage = images.get(0);

        // get height and width of screen
        //int screenWidth = displayMetrics.widthPixels;
        //int screenHeight = displayMetrics.heightPixels;
    }

    public void printPixelsClicked(View view) {
        View v = ((View) findViewById(R.id.single_touch_view));
      //  ((MainDrawingView) v).printPixels();
        ((MainDrawingView) v).printMatrix();

    }

    public void convertImageToBitampClicked(View view) {
        Bitmap bitmap = convertImageToBitmap(randomImage);
        drawBitmap(bitmap);
    }

    public Bitmap convertImageToBitmap(int image) {

        String line;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), image, options);

        int h = icon.getHeight();
        int w = icon.getWidth();

        /*

        for (int i = 0; i < h; i += JUMP){
            line = "";
            for (int j = 0; j < w; j += JUMP){

              // Log.d(MainActivity.TAG, "i,j" + i + ","+j +"  = " + icon.getPixel(j,i));

                if(icon.getPixel(j,i) < -1) {

                    line = line + "*";
                }
                else {
                    line = line + " ";
                }
            }
            Log.d(MainActivity.TAG, line);
        }
        */

       // Log.d(MainActivity.TAG, getBytesFromBitmap(icon).toString());

        return icon;
    }

    public void drawBitmap (Bitmap bitmap) {

        int JUMP_FOR_DRAW = 1;

        int h = bitmap.getHeight();
        int w = bitmap.getWidth();

        Log.d(MainActivity.TAG, "from drawBitmap h = " + h);
        Log.d(MainActivity.TAG, "from drawBitmap w = " + w);

        // Create a bitmap of the same size
        Bitmap newBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
// Create a canvas  for new bitmap
        Canvas c = new Canvas(newBmp);

// Draw your old bitmap on it.
        c.drawBitmap(bitmap, 0, 0, new Paint());

        for (int i = 0; i < h; i += JUMP_FOR_DRAW){
            for (int j = 0; j < w; j += JUMP_FOR_DRAW){

                newBmp.setPixel(j,i,bitmap.getPixel(j,i));

                //if you want ot cut the image set any condition here:

                /*

                if(i>300) {
                    newBmp.setPixel(j,i,-1);
                }
                else {
                    newBmp.setPixel(j,i,bitmap.getPixel(j,i));
                }

                */
            }
        }


        Drawable d = new BitmapDrawable(getResources(), newBmp);
        //ImageView v = ((ImageView) findViewById(R.id.imageView));
        View v = ((View) findViewById(R.id.single_touch_view));
        v.setBackground(d);
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public int [][] convertBitmapToMatrix (Bitmap bitmap) {

        int [][] matrix = new int[SIZE][SIZE];
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();

        Log.d(MainActivity.TAG, "from convertBitmapToMatrix h = " + h);
        Log.d(MainActivity.TAG, "from convertBitmapToMatrix w = " + w);

        for (int i = 0; i < h; i += JUMP){
            for (int j = 0; j < w; j += JUMP){

                if(bitmap.getPixel(j,i) < -1) {
                    matrix[i][j] = 1;

                }
                else {
                    //default valus is 0
               }
            }
        }
        return matrix;
    }

    public void compareImagesClicked(View view) {

        Bitmap source_bitmap = convertImageToBitmap(randomImage);
        int [][] source_matrix = convertBitmapToMatrix(source_bitmap);
        printMatrix(source_matrix);

        View v = ((View) findViewById(R.id.single_touch_view));
        int [] result = ((MainDrawingView) v).compareMatrices(source_matrix);

        TextView g = ((TextView) findViewById(R.id.goodPoints));
        TextView b = ((TextView) findViewById(R.id.badPoints));
        g.setText("Good Points: " + result[0]);
        b.setText("Bad Points: " + result[1]);
    }

    public static void printMatrixBits(int [][] matrix) {
        String line;
        for (int i = 0; i < SIZE; i += 1){
            line = "";
            for (int j = 0; j < SIZE; j += 1){
                if(matrix[i][j] > 0) {
                    line = line + "1";
                }
                else {
                    line = line + "0";
                }
            }
            Log.d(MainActivity.TAG, line);
        }
    }



    public static void printMatrix(int [][] matrix) {
        int JUMP_PRINT = 5;
        String line;
        for (int i = 0; i < SIZE; i += JUMP_PRINT){
            line = "";
            for (int j = 0; j < SIZE; j += JUMP_PRINT){
                if(matrix[i][j] > 0) {
                    line = line + "*";
                }
                else {
                    line = line + " ";
                }
            }
            Log.d(MainActivity.TAG, line);
        }
    }

    public void hideClicked(View view) {
        View v = ((View) findViewById(R.id.single_touch_view));
        v.setBackgroundColor(Color.WHITE);
    }

    public void drawingModeClicked(View view) {
        View v = ((View) findViewById(R.id.single_touch_view));
        ((MainDrawingView) v).setDrawingMode(view.getTag().toString());
    }
}
