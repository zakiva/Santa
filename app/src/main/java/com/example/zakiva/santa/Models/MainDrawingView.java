package com.example.zakiva.santa.Models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.zakiva.santa.MainActivity;

/**
 * Created by zakiva on 9/15/16.
 */

public class MainDrawingView extends View {

    private Paint paint;
    private Path path;
    //private Bitmap bitmap;
    //private int [][] matrix;
    public static int SIZE = 700;
    public static int JUMP = 5;
    private static final float RATIO = 1f / 1f;
    private Context context;
    private int drawingMode;

    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    public Bitmap canvasBitmap;
    //canvas paint
    private Paint canvasPaint;

    public MainDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
       // this.setDrawingCacheEnabled(true);

        paint = new Paint();
        path = new Path();

        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        this.context = context;

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        //int screenWidth = metrics.widthPixels;
        //int screenHeight = metrics.heightPixels;
        //SIZE = screenWidth;

        drawingMode = 1;
        //matrix = new int[SIZE][SIZE];

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
        //updateMatrixWithDelta(yRounded, xRounded);

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

    public void setDrawingMode (int mode) {
        this.drawingMode = mode;
        if (drawingMode == 0) {
            paint.setStrokeWidth(20f);
            paint.setColor(Color.WHITE);
            //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {
            paint.setStrokeWidth(5f);
            paint.setColor(Color.BLACK);
            //paint.setXfermode(null);
        }
    }


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

        // get height and width of screen
        //int screenWidth = displayMetrics.widthPixels;
        //int screenHeight = displayMetrics.heightPixels;
    }
}
