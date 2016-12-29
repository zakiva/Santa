package com.example.zakiva.santa.Models;

import android.app.Activity;
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
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.example.zakiva.santa.DrawingGame;
import com.example.zakiva.santa.Helpers.Drawing;
import com.example.zakiva.santa.MainActivity;

import java.util.ArrayList;

/**
 * Created by zakiva on 9/15/16.
 */

public class MainDrawingView extends View {

    private Paint paint;
    private Path path;
    //private Bitmap bitmap;
    //private int [][] matrix;
    public static int SIZE_PIXELS;
    public static int dpSize = 360;
    public static int JUMP = 5;
    private static final float RATIO = 1f / 1f;
    public static Context context;
    private int drawingMode;
    private boolean drawingNow;
    private boolean allowDrawing;
    private ArrayList<Path> pathsUndo;
    private static int verticalMarginDp = 30;
    private static int verticalMarginPixels;
    public static int screenHeightPixels;
    public static int screenWidthPixels;
    public static int drawingAreaHeight;
    public static int density;
    public static int densityFactor;
    int stroke;

    //private Activity drawingGameActivity;

    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    public Bitmap canvasBitmap;
    //canvas paint
    private Paint canvasPaint;

    public MainDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
       // this.setDrawingCacheEnabled(true);

        this.context = context;
        //drawingGameActivity = activity;


        verticalMarginPixels = (int) Drawing.convertDpToPixel(verticalMarginDp, context);

        DisplayMetrics displaymetrics = new DisplayMetrics();
       // drawingGameActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        screenHeightPixels = metrics.heightPixels;
        screenWidthPixels = metrics.widthPixels;
        drawingAreaHeight = screenHeightPixels - 2 * verticalMarginPixels; // top + bottom

        SIZE_PIXELS = screenWidthPixels;



        Log.d(MainActivity.TAG, "&&&&&&&&&&&screenWidthPixels: " +  screenWidthPixels);
        Log.d(MainActivity.TAG, "&&&&&&&&&&&screenHeightPixels: " + screenHeightPixels);
        Log.d(MainActivity.TAG, "&&&&&&&&&&&sSIZE PIXELS: " + SIZE_PIXELS);
        Log.d(MainActivity.TAG, "&&&&&&&&&&&verticalMarginPixels: " + verticalMarginPixels);
        Log.d(MainActivity.TAG, "&&&&&&&&&&&drawingAreaHeight: " + drawingAreaHeight);



        paint = new Paint();
        path = new Path();

        paint.setAntiAlias(true);
        stroke = context.getResources().getDisplayMetrics().densityDpi / 60;
        Log.d(MainActivity.TAG, "stroke  = " +  stroke);
        paint.setStrokeWidth((float) stroke);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);


       // Resources resources = context.getResources();
        //DisplayMetrics metrics = resources.getDisplayMetrics();
        //int screenWidth = metrics.widthPixels;
        //int screenHeight = metrics.heightPixels;
        //SIZE_PIXELS = screenWidth;

        drawingMode = 1;
        drawingNow = false;
        allowDrawing = false;
        //matrix = new int[SIZE_PIXELS][SIZE_PIXELS];

        canvasPaint = new Paint(Paint.DITHER_FLAG);
        canvasBitmap = Bitmap.createBitmap(screenWidthPixels, drawingAreaHeight, Bitmap.Config.ALPHA_8);
        drawCanvas = new Canvas(canvasBitmap);

        this.pathsUndo = new ArrayList<>();

        density = metrics.densityDpi;
        densityFactor = density / 320 > 0 ? density / 320 : 1;



        Log.d(MainActivity.TAG, "@@@@@@@@@@@@@@density = " +  density);
        Log.d(MainActivity.TAG, "@@@@@@@@@@@@@@density / 320 = " +  density / 320);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        //bitmap = this.getDrawingCache(true);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!allowDrawing)
            return false;
        // Get the coordinates of the touch event.
        float eventX = event.getX();
        float eventY = event.getY();

        int x = (int) eventX;
        int y = (int) eventY;
        int xRounded = roundUp(x);
        int yRounded = roundUp(y);
        x = xRounded;
        y = yRounded;

        if (x >= screenWidthPixels || y >= drawingAreaHeight || x < 0 || y < 0) {
            if (drawingNow)
                end_motion();
            invalidate();
            return false;
        }
        if (xRounded >= screenWidthPixels || yRounded >= drawingAreaHeight || xRounded < 0 || yRounded < 0) {
            if (drawingNow)
                end_motion();
            invalidate();
            return false;
        }

        //matrix[yRounded][xRounded] = drawingMode; // set pixel to 1 or 0
        //updateMatrixWithDelta(yRounded, xRounded);

        Log.d(MainActivity.TAG, "touched: " + x +"," + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Set a new starting point
                path.moveTo(x, y);
                drawingNow = true;
                break;
            case MotionEvent.ACTION_MOVE:
                // Connect the points
                if (drawingNow)
                    path.lineTo(x, y);
                else {
                    path.moveTo(x, y);
                    drawingNow = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                drawPoint(x, y);
                end_motion();
                break;
            default:
                return false;
        }
        // Makes our view repaint and call onDraw
        invalidate();
        return true;
    }

    public void drawPoint(int x, int y) {
        path.lineTo((float) (x+0), (float) (y+0.5));
        path.lineTo((float) (x+0.3), (float) (y+0.3));
        path.lineTo((float) (x+0.5), (float) (y+0));
        path.lineTo((float) (x+0.3), (float) (y-0.3));
        path.lineTo((float) (x+0), (float) (y-0.5));
        path.lineTo((float) (x-0.3), (float) (y-0.3));
        path.lineTo((float) (x-0.5), (float) (y-0));
        path.lineTo((float) (x-0.3), (float) (y+0.3));
        path.lineTo((float) (x+0), (float) (y+0.5));
    }

    public void end_motion () {
        drawCanvas.drawPath(path, paint);
        if (drawingMode == 1)
            addPathToUndoList();
        else
            path = new Path();
        drawingNow = false;
        //path.reset();
    }

    public void addPathToUndoList () {
        pathsUndo.add(0, path);
        path = new Path();
    }

    public static int roundUp(int n) {
        return (n + JUMP - 1) / JUMP * JUMP;
    }

    //return true iff we can change the mode (no drawing at the moment)
    public boolean setDrawingMode (int mode) {
        if (drawingNow)
            return false;
        this.drawingMode = mode;
        updatePaintWithMode();
        return true;
    }

    public void updatePaintWithMode () {
        if (drawingMode == 0) {
            paint.setStrokeWidth(22f);
            paint.setColor(Color.WHITE);
            //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            //paint.setAlpha(250);
        }
        else {
            paint.setStrokeWidth(5f);
            paint.setColor(Color.BLACK);
            //paint.setAlpha(0);
            //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
            //paint.setXfermode(null);
        }
    }

    public void setAllowDrawing (boolean allow) {
        allowDrawing = allow;
    }

    public void restartDrawing () {
        end_motion();
        invalidate();
        canvasBitmap = Bitmap.createBitmap(screenWidthPixels, drawingAreaHeight, Bitmap.Config.ALPHA_8);
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        drawCanvas = new Canvas(canvasBitmap);
        this.pathsUndo = new ArrayList<>();
    }

    public void undoLastPath () {
        Log.d(MainActivity.TAG, "paths.size() = " +  pathsUndo.size());
        if (pathsUndo.size()==0)
            return;
        Path p = pathsUndo.get(0);
        pathsUndo.remove(0);
        paint.setStrokeWidth(7f);
        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // in order to remove
        drawCanvas.drawPath(p, paint);
        updatePaintWithMode();
        paint.setXfermode(null); // go back to black - drawing mode
        //path.reset();
        //invalidate();
    }

    //cancel onMeasure

    /*


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

        //set w&h manually by SIZE_PIXELS:
        width = SIZE_PIXELS;
        height = SIZE_PIXELS;

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

        // get height and width of screen
        //int screenWidth = displayMetrics.widthPixels;
        //int screenHeight = displayMetrics.heightPixels;
    }

    */


}
