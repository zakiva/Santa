package com.example.zakiva.santa.Models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.zakiva.santa.*;


import com.example.zakiva.santa.MainActivity;

/**
 * Created by zakiva on 9/15/16.
 */

public class MainDrawingView extends View {

    public static final String TAG = ">>>>>>>Debug: ";
    private Paint paint;
    private Path path;
    private Bitmap bitmap;
    private int [][] matrix;
    public static int SIZE = 700;
    public static int JUMP = 5;
    private static final float RATIO = 1f / 1f;
    private Context context;
    private int drawingMode;

    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    //canvas paint
    private Paint canvasPaint;



    public MainDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
       // this.setDrawingCacheEnabled(true);

        paint = new Paint();
        path = new Path();

        paint.setAntiAlias(true);
        paint.setStrokeWidth(11f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        this.context = context;

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        //SIZE = screenWidth;

        drawingMode = 1;
        matrix = new int[SIZE][SIZE];

        canvasPaint = new Paint(Paint.DITHER_FLAG);
        canvasBitmap = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
   }

    @Override
    protected void onDraw(Canvas canvas) {
        //bitmap = this.getDrawingCache(true);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Get the coordinates of the touch event.
        float eventX = event.getX();
        float eventY = event.getY();

        int x = (int) eventX;
        int y = (int) eventY;
        int xRounded = roundUp(x);
        int yRounded = roundUp(y);
        x = xRounded;
        y = yRounded;

        if (x >= SIZE || y >= SIZE || x < 0 || y < 0)
            return false;
        if (xRounded >= SIZE || yRounded >= SIZE || xRounded < 0 || yRounded < 0)
            return false;

        //matrix[yRounded][xRounded] = drawingMode; // set pixel to 1 or 0
        updateMatrixWithDelta(yRounded, xRounded);

        Log.d(MainActivity.TAG, "touched: " + x +"," + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Set a new starting point
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                // Connect the points
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(path, paint);
                path.reset();
                break;
            default:
                return false;
        }

        // Makes our view repaint and call onDraw
        invalidate();
        return true;
    }

    int roundUp(int n) {
        return (n + JUMP - 1) / JUMP * JUMP;
    }

    public void printMatrix() {
        Draw.printMatrix(matrix);
    }

    public int [] compareMatrices(int [][] source) {

        int white_source = 0, white_matrix = 0, black_source = 0, black_matrix = 0, equal = 0, diff = 0;
        int equal_delta = 0, bad_black_delta = 0;
        int DELTA = 5;

        for (int i = 0; i < SIZE; i += JUMP){
            for (int j = 0; j < SIZE; j += JUMP){
                if(matrix[i][j] > 0) {
                    black_matrix++;
                }
                else {
                    white_matrix++;
                }
                if(source[i][j] > 0) {
                    black_source++;
                }
                else {
                    white_source++;
                }
                if(matrix[i][j] == source[i][j] && matrix[i][j] == 1) {
                    equal++;
                }
                else {
                    diff++;
                }
                if (matrix[i][j] == 1) {
                    if (compareWithDelta(source, i, j, DELTA)) {
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

        int [] a = new int [2];
        a[0] = equal_delta;
        a[1] = bad_black_delta;
        return a;
    }

    public boolean compareWithDelta (int [][] source, int i, int j, int DELTA) {

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





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d(MainActivity.TAG, "ONMESEUERREE!!!!!!!!!!!!!!!!!!!!!!");


        // int width = getMeasuredWidth();
        // int height = getMeasuredHeight();

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //set w&h manually by SIZE:
        width = SIZE;
        height = SIZE;

        Log.d(MainActivity.TAG, "1111 width, height = " + width + " " + height);


        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        int maxWidth = (int) (heigthWithoutPadding * RATIO);
        int maxHeight = (int) (widthWithoutPadding / RATIO);

        if (widthWithoutPadding > maxWidth) {
            width = maxWidth + getPaddingLeft() + getPaddingRight();
        } else {
            height = maxHeight + getPaddingTop() + getPaddingBottom();
        }

        Log.d(MainActivity.TAG, "222 width, height = " + width + " " + height);

        setMeasuredDimension(width, height);
    }

    public void setDrawingMode (int mode) {
        this.drawingMode = mode;
        Log.d(MainActivity.TAG, "this.drawingMode = " + this.drawingMode);

        if (drawingMode == 0) {
            paint.setStrokeWidth(20f);
            paint.setColor(Color.WHITE);
            //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {
            paint.setStrokeWidth(11f);
            paint.setColor(Color.BLACK);
            //paint.setXfermode(null);
        }

    }
}
