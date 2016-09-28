package com.example.zakiva.santa;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.zakiva.santa.Models.MainDrawingView;
import com.example.zakiva.santa.Helpers.Drawing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


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
        ArrayList <Integer> images = new ArrayList<Integer>(Arrays.asList(R.drawable.star700sq, R.drawable.bone700sq, R.drawable.heart700sq, R.drawable.house700sq, R.drawable.nike700sq, R.drawable.tree700sq));
        Collections.shuffle(images);
        randomImage = images.get(0);
    }

    public void printPixelsClicked(View view) {
        //do nothing for now
    }

    public void convertImageToBitmapClicked(View view) {
        Bitmap bitmap = Drawing.convertImageToBitmap(randomImage, this);
        Drawing.drawBitmap(bitmap, this, v);
    }

    public void compareImagesClicked(View view) {

        Bitmap draw_bitmap = ((MainDrawingView) v).canvasBitmap;
        Bitmap source_bitmap = Drawing.convertImageToBitmap(randomImage, this);

        int [][] source_matrix = Drawing.convertBitmapToMatrix(source_bitmap);
        int [][] user_matrix = Drawing.convertBitmapToMatrix(draw_bitmap);

        Drawing.printMatrix(source_matrix);

        int [] result = Drawing.compareMatrices(source_matrix, user_matrix);

        TextView good = ((TextView) findViewById(R.id.goodPoints));
        TextView bad = ((TextView) findViewById(R.id.badPoints));
        good.setText("Good Points: " + result[0]);
        bad.setText("Bad Points: " + result[1]);
    }

    public void hideClicked(View view) {
        v.setBackgroundColor(Color.WHITE);
    }

    public void drawingModeClicked(View view) {
        int mode = Integer.parseInt(view.getTag().toString());
        if (mode == 0) {
            pen.setBackgroundResource(R.drawable.pen_icon);
            eraser.setBackgroundResource(R.drawable.eraser_icon_color);
        }
        else {
            pen.setBackgroundResource(R.drawable.pen_icon_color);
            eraser.setBackgroundResource(R.drawable.eraser_icon);
        }
        ((MainDrawingView) v).setDrawingMode(mode);
    }

    public void bitmapBbuttonClicked(View view) {
        Bitmap b = ((MainDrawingView) v).canvasBitmap;
        int [][] m = Drawing.convertBitmapToMatrix(b);
        Drawing.printMatrix(m);
    }


}
