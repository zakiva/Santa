package com.example.zakiva.santa.Helpers;

/**
 * Created by zakiva on 9/25/16.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.zakiva.santa.*;
import com.example.zakiva.santa.Models.MainDrawingView;


public class Drawing {

    public static int SIZE;
    public static int JUMP;

    public static void initDrawingHelper() {
        SIZE = MainDrawingView.SIZE;
        JUMP = MainDrawingView.JUMP;
    }

    public static void printMatrix(int[][] matrix) {
        int JUMP_PRINT = 5;
        String line;
        for (int i = 0; i < SIZE; i += JUMP_PRINT) {
            line = "";
            for (int j = 0; j < SIZE; j += JUMP_PRINT) {

                if (matrix[i][j] > 0) {
                    line = line + "*";
                } else {
                    line = line + " ";
                }
            }
            Log.d(MainActivity.TAG, line);
        }
    }

    public static Bitmap convertImageToBitmap(int image, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), image, options);
        return b;
    }

    public static int[][] convertBitmapToMatrix(Bitmap bitmap) {

        int[][] matrix = new int[SIZE][SIZE];
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();
        int pixel, alpha;

        //Log.d(MainActivity.TAG, "from convertBitmapToMatrix h = " + h);
        //Log.d(MainActivity.TAG, "from convertBitmapToMatrix w = " + w);

        for (int i = 0; i < h; i += JUMP) {
            for (int j = 0; j < w; j += JUMP) {

                pixel = bitmap.getPixel(j, i);
                alpha = Color.alpha(pixel);

                if (pixel < -1 && alpha == 255) {
                    matrix[i][j] = 1;

                    //int colour = bitmap.getPixel(j,i);
                    //int red = Color.red(colour);
                    //int blue = Color.blue(colour);
                    //int green = Color.green(colour);
                    //int alpha = Color.alpha(colour);

                    //Log.d(MainActivity.TAG, "J,I= " + bitmap.getPixel(j,i));
                    // Log.d(MainActivity.TAG, "red= " + red);
                    //Log.d(MainActivity.TAG, "blue= " + blue);
                    // Log.d(MainActivity.TAG, "green= " + green);
                    // Log.d(MainActivity.TAG, "alpha= " + alpha);

                } else {
                    //default valus is 0
                }
            }
        }
        return matrix;
    }

    public static void drawBitmap(Bitmap bitmap, Context context, View v) {

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

        for (int i = 0; i < h; i += JUMP_FOR_DRAW) {
            for (int j = 0; j < w; j += JUMP_FOR_DRAW) {
                newBmp.setPixel(j, i, bitmap.getPixel(j, i));
            }
        }

        Drawable d = new BitmapDrawable(context.getResources(), newBmp);
        v.setBackground(d);
    }


    // EXTRAS

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static int [] compareMatrices(int [][] source, int [][] matrix) {

        int white_source = 0, white_matrix = 0, black_source = 0, black_matrix = 0, equal = 0, diff = 0;
        int equal_delta = 0, bad_black_delta = 0;
        int DELTA = 7;

        for (int i = 0; i < SIZE; i += JUMP) {
            for (int j = 0; j < SIZE; j += JUMP) {
                if (matrix[i][j] > 0) {
                    black_matrix++;
                } else {
                    white_matrix++;
                }
                if (source[i][j] > 0) {
                    black_source++;
                } else {
                    white_source++;
                }
                if (matrix[i][j] == source[i][j] && matrix[i][j] == 1) {
                    equal++;
                } else {
                    diff++;
                }
                if (matrix[i][j] == 1) {
                    if (compareWithDelta(source, matrix, i, j, DELTA)) {
                        equal_delta++;
                    } else {
                        bad_black_delta++;
                    }
                }

            }
        }
        Log.d(MainActivity.TAG, ">>>>>>>>>>Comparing summary<<<<<<<<<<<");
        Log.d(MainActivity.TAG, "white source = " + white_source);
        Log.d(MainActivity.TAG, "white matrix = " + white_matrix);
        Log.d(MainActivity.TAG, "black source = " + black_source);
        Log.d(MainActivity.TAG, "black matrix = " + black_matrix);
        Log.d(MainActivity.TAG, "equal = " + equal);
        Log.d(MainActivity.TAG, "equal delta = " + equal_delta);
        Log.d(MainActivity.TAG, "bad black delta = " + bad_black_delta);
        Log.d(MainActivity.TAG, "diff = " + diff);

        Log.d(MainActivity.TAG, ">>>>>>>>>>After Compare, Source = <<<<<<<<<<<");
        Drawing.printMatrix(source);

        int[] a = new int[2];
        a[0] = equal_delta;
        a[1] = bad_black_delta;
        return a;
    }

    public static boolean compareWithDelta (int [][] source, int [][] matrix, int i, int j, int DELTA) {

        int limit = DELTA*JUMP;

        if (i-limit < 0 || j-limit < 0 || i + limit > SIZE || j + limit > SIZE )
            return matrix[i][j] == source[i][j] && matrix[i][j] == 1;

        for (int k = -1 * (DELTA - 1); k < DELTA; k++) {
            for (int l = -1 * (DELTA - 1); l < DELTA; l++) {
                if (source[i+k*JUMP][j+l*JUMP] == 1){ // hit !!! init env.
                    for (int m = -1 * (DELTA - 2); m < DELTA - 1; m++) {
                        for (int n = -1 * (DELTA - 2); n < DELTA - 1; n++) {
                            source[i+n*JUMP][j+m*JUMP] = 0;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }





}






    /*
        public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
     */



    /*

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
    */


    /*
        public void updateMatrixWithDelta (int i, int j) {

        int DELTA = 3;
        int limit = DELTA*JUMP;

        if (i-limit < 0 || j-limit < 0 || i + limit > SIZE || j + limit > SIZE || drawingMode == 1) {
            matrix[i][j] = drawingMode;
            return;
        }

        for (int k = -1 * (DELTA - 1); k < DELTA; k++) {
            for (int l = -1 * (DELTA - 1); l < DELTA; l++) {
                matrix[i + k * JUMP][j + l * JUMP] = 0;
            }
        }
    }
     */

        /*
    public void printPixels () {
        Log.d(MainActivity.TAG, "10,10 = " + bitmap.getPixel(10,10));
        Log.d(MainActivity.TAG, "20,20 = " + bitmap.getPixel(20,20));
        Log.d(MainActivity.TAG, "30,30 = " + bitmap.getPixel(30,30));
        Log.d(MainActivity.TAG, "40,40 = " + bitmap.getPixel(40,40));
        Log.d(MainActivity.TAG, "50,50 = " + bitmap.getPixel(50,50));
        Log.d(MainActivity.TAG, "60,60 = " + bitmap.getPixel(60,60));

        for (int i =0; i<10; i++){
            for (int j=0; j<10; j++){
                Log.d(MainActivity.TAG, "(400,400), i = " + i + ", j = " + j + ", pixel:" + bitmap.getPixel(400 + i ,400 + j));
            }
        }


        for (int i = 0; i < 500; i++){
            for (int j = 0; j < 500; j++){
                if(bitmap.getPixel(i,j) > 0) {
                    Log.d(MainActivity.TAG, "x,y = " + i + "," +  j);
                    Log.d(MainActivity.TAG, "pixel = " + bitmap.getPixel(i,j));
                }
            }
        }
     } */


