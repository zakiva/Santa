package com.example.zakiva.santa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.Drawing;
import com.example.zakiva.santa.Models.MainDrawingView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DrawingGame extends AppCompatActivity {

    private int MILLISECONDS;
    private int randomImage;
    private Button pen;
    private Button eraser;
    private Button doneButton;
    private MainDrawingView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_game);
        Drawing.initDrawingHelper();
        initFields();
        startGame();
    }

    //flow
    public void initFields () {
        MILLISECONDS = 6000;
        pen = (Button) findViewById(R.id.pen);
        eraser = (Button) findViewById(R.id.eraser);
        doneButton = (Button) findViewById(R.id.doneButton);
        v = (MainDrawingView) findViewById(R.id.single_touch_view);
        ArrayList<Integer> images = new ArrayList<Integer>(Arrays.asList(R.drawable.star700sq, R.drawable.bone700sq, R.drawable.heart700sq, R.drawable.house700sq, R.drawable.nike700sq, R.drawable.tree700sq));
        Collections.shuffle(images);
        randomImage = images.get(0);
    }

    public void startGame () {
        //show the source image
        v.setBackground(ResourcesCompat.getDrawable(getResources(), randomImage, null));
        final TextView stopper = (TextView) findViewById(R.id.stopper);


        new CountDownTimer(MILLISECONDS, 100) {

            public void onTick(long millisUntilFinished) {
                Log.d(MainActivity.TAG, ">>>>>>>>>> =millisUntilFinished " + millisUntilFinished);
                if (millisUntilFinished / 1000 == 0) {
                    v.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.draw_it, null));
                }
                stopper.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                stopper.setVisibility(View.GONE);
                startDraw();
            }
        }.start();
    }

    public void startDraw () {
        v.setBackgroundColor(Color.WHITE);
        v.setAllowDrawing(true);
        pen.setVisibility(View.VISIBLE);
        eraser.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.VISIBLE);
    }

    //listeners
    public void doneButtonClicked(View view) {
        Intent intent = new Intent(DrawingGame.this, Score.class);
        intent.putExtra("score", calcScore());
        startActivity(intent);
    }

    public void drawingModeClicked(View view) {
        int mode = Integer.parseInt(view.getTag().toString());

        if (!(v.setDrawingMode(mode)))
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

    //helpers

    public int calcScore () {
        Bitmap draw_bitmap = v.canvasBitmap;
        Bitmap source_bitmap = Drawing.convertImageToBitmap(randomImage, this);
        int [][] source_matrix = Drawing.convertBitmapToMatrix(source_bitmap);
        int [][] user_matrix = Drawing.convertBitmapToMatrix(draw_bitmap);
        int [] result = Drawing.compareMatrices(source_matrix, user_matrix);
        int good = result[0];
        int bad = result[1];
        //change this formula
        return ((good - bad + 500) * 10);
    }
}
