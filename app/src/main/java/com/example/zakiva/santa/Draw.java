package com.example.zakiva.santa;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.zakiva.santa.Models.MainDrawingView;
import com.example.zakiva.santa.Helpers.Drawing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Draw extends AppCompatActivity {

    public static int SIZE;
    public static int JUMP;
    public int randomImage;
    private Button pen;
    private Button eraser;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        Drawing.initDrawingHelper();
        initFields();
    }

    public void initFields () {
        SIZE = MainDrawingView.SIZE;
        JUMP = MainDrawingView.JUMP;
        pen = (Button) findViewById(R.id.pen);
        eraser = (Button) findViewById(R.id.eraser);
        v = findViewById(R.id.single_touch_view);
        ((MainDrawingView)v).setAllowDrawing(true);
        ArrayList <Integer> images = new ArrayList<Integer>(Arrays.asList(R.drawable.bone700sq, R.drawable.heart700sq, R.drawable.house700sq, R.drawable.nike700sq, R.drawable.tree700sq));
        Collections.shuffle(images);
        randomImage = images.get(0);
    }

    public void printPixelsClicked(View view) {
        //do nothing for now
    }

    public void convertImageToBitmapClicked(View view) {
        v.setBackground(ResourcesCompat.getDrawable(getResources(), randomImage, null));

        //redundant
       // Bitmap bitmap = Drawing.convertImageToBitmap(randomImage, this);
       // Drawable d = new BitmapDrawable(getResources(), bitmap);
       // v.setBackground(d);
       // Drawing.drawBitmap(bitmap, this, v);
    }

    public void compareImagesClicked(View view) {

        Bitmap draw_bitmap = ((MainDrawingView) v).canvasBitmap;
        Bitmap source_bitmap = Drawing.convertImageToBitmap(randomImage, this);

        int [][] source_matrix = Drawing.convertBitmapToMatrix(source_bitmap);
        int [][] user_matrix = Drawing.convertBitmapToMatrix(draw_bitmap);

        int blackSource = Drawing.countBlackPixels(source_matrix);

        Drawing.printMatrix(source_matrix);

        Log.d(MainActivity.TAG, "black source original = " + blackSource);
        int [] result = Drawing.compareMatrices(source_matrix, user_matrix);

        double new_for = (int) (((double) (blackSource - result[2]) / blackSource) * 1000) - 0.5 * result[1];
        Log.d(MainActivity.TAG, "new_for = " + new_for);

        TextView good = ((TextView) findViewById(R.id.goodPoints));
        TextView bad = ((TextView) findViewById(R.id.badPoints));
        int delta = result[0] - result[1];
        if (delta < 0)
            delta = 0;
        double formula = ((double) delta / blackSource) * 1000;
        int score = (int) formula;
        good.setText("Good: " + result[0]);
        bad.setText("Bad: " + result[1] + " For: " + calcScore(result));
    }

    public int calcScore (int [] result ) {
        //Bitmap draw_bitmap = v.canvasBitmap;
        //Bitmap source_bitmap = Drawing.convertImageToBitmap(randomImage, this);
        int [][] source_matrix = Drawing.convertImageToMatrix(randomImage, this);
        int blackSource = Drawing.countBlackPixels(source_matrix);
        int blackSourceAfterCompare = result[2];
        int badBlackPixels = result[1];
        double formula = ((double) (blackSource - blackSourceAfterCompare) / blackSource) * 1000 - 0.5 * badBlackPixels;
        int score = (int) formula;
        return score < 0 ?  0 : score;
    }

    public void hideClicked(View view) {
        v.setBackgroundColor(Color.WHITE);
    }

    public void drawingModeClicked(View view) {
        int mode = Integer.parseInt(view.getTag().toString());

        if (!(((MainDrawingView) v).setDrawingMode(mode)))
            return;

        if (mode == 0) {
            pen.setBackgroundResource(R.drawable.pen_icon);
            eraser.setBackgroundResource(R.drawable.eraser_icon_color);
        }
        else {
            pen.setBackgroundResource(R.drawable.pen_icon_color);
            eraser.setBackgroundResource(R.drawable.eraser_icon);
        }
    }

    public void bitmapBbuttonClicked(View view) {
        Bitmap b = ((MainDrawingView) v).canvasBitmap;
        int [][] m = Drawing.convertBitmapToMatrix(b);
        Drawing.printMatrix(m);
    }

    public void restartButtonClicked(View view) {
        ((MainDrawingView) v).restartDrawing();
    }
}
