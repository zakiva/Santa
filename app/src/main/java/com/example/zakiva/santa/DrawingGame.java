package com.example.zakiva.santa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.Drawing;
import com.example.zakiva.santa.Models.MainDrawingView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DrawingGame extends AppCompatActivity {

    private int millisecondsToShow;
    private int randomImage;
    private Button pen;
    private Button eraser;
    private Button restart;
    private Button undo;
    private Button doneButton;
    private Button replaceHelper;
    private Button flashHelper;
    private Button clueHelper;
    private ImageView sourceImage;
    private MainDrawingView v;
    private int flashHelperLength;
    private ArrayList<Integer> images;
    private RelativeLayout activityBackground;

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
        millisecondsToShow = 2000;
        flashHelperLength = 2000;
        pen = (Button) findViewById(R.id.pen);
        eraser = (Button) findViewById(R.id.eraser);
        restart = (Button) findViewById(R.id.restart);
        undo = (Button) findViewById(R.id.undo);
        doneButton = (Button) findViewById(R.id.doneButton);
        flashHelper = (Button) findViewById(R.id.flashHelperButton);
        replaceHelper = (Button) findViewById(R.id.replaceDrawingHelperButton);
        clueHelper = (Button) findViewById(R.id.clueHelperButton);
        v = (MainDrawingView) findViewById(R.id.single_touch_view);
        //images = new ArrayList<Integer>(Arrays.asList(R.drawable.bone700sq, R.drawable.heart700sq, R.drawable.house700sq, R.drawable.nike700sq, R.drawable.tree700sq));
        images = new ArrayList<Integer>(Arrays.asList(R.drawable.rec_400));
        Collections.shuffle(images);
        randomImage = images.get(0);
        activityBackground = (RelativeLayout) findViewById(R.id.activityBackground);
        activityBackground.getBackground().setAlpha(0);
        sourceImage = (ImageView) findViewById(R.id.sourceImage);
    }

    public void startGame () {
        //show the source image
        //v.setBackground(ResourcesCompat.getDrawable(getResources(), randomImage, null));
        final TextView stopper = (TextView) findViewById(R.id.stopper);
        stopper.setVisibility(View.VISIBLE);


        new CountDownTimer(millisecondsToShow, 100) {

            public void onTick(long millisUntilFinished) {
                Log.d(MainActivity.TAG, ">>>>>>>>>> =millisUntilFinished " + millisUntilFinished);
               // cancel DRAW IT before starting gmae
                // if (millisUntilFinished / 1000 == 0) {
               //     v.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.draw_it, null));
               // }
                stopper.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                stopper.setVisibility(View.GONE);
                startDraw();
            }
        }.start();
    }

    public void startDraw () {
        //v.setBackgroundColor(Color.WHITE);
        //for checking only:
        //v.setBackground(ResourcesCompat.getDrawable(getResources(), randomImage, null));



        //sourceImage.setVisibility(View.GONE);


        v.setAllowDrawing(true);
        pen.setVisibility(View.VISIBLE);
        eraser.setVisibility(View.VISIBLE);
        restart.setVisibility(View.VISIBLE);
        undo.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.VISIBLE);
        replaceHelper.setVisibility(View.VISIBLE);
        clueHelper.setVisibility(View.VISIBLE);
        flashHelper.setVisibility(View.VISIBLE);
    }

    //listeners
    public void doneButtonClicked(View view) {

        v.setAllowDrawing(false);

        hideButtons();

        Log.d(MainActivity.TAG, ">>>>>>>>>> =before botmaps  ");

        Bitmap bitmap = Drawing.convertImageToBitmap(randomImage, this);
        Bitmap coloredBitmap = Drawing.convertBlackToColor(bitmap);

        Log.d(MainActivity.TAG, ">>>>>>>>>> =after bitmaps befrep drawbale  ");

        Drawable d = new BitmapDrawable(getResources(), coloredBitmap);
        sourceImage.setImageDrawable(d);
       // v.setBackground(d);

        Log.d(MainActivity.TAG, ">>>>>>>>>> =after draewbake before intent  ");


        final Intent intent = new Intent(DrawingGame.this, Score.class);
        intent.putExtra("score", calcScore());
        intent.putExtra("game", "drawing");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            public void run()
            {
                startActivity(intent);
            }
        }, 2000);
    }

    public void hideButtons () {
        pen.setVisibility(View.GONE);
        eraser.setVisibility(View.GONE);
        restart.setVisibility(View.GONE);
        undo.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);
        replaceHelper.setVisibility(View.GONE);
        clueHelper.setVisibility(View.GONE);
        flashHelper.setVisibility(View.GONE);
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

    public void restartDrawClicked(View view) {
        // change: pass it arguments instead
        showAlertDialogForRestart();
    }

    public void undoButtonClicked(View view) {
        v.undoLastPath();
    }

    //helpers

    public long calcScore () {
        //Bitmap draw_bitmap = v.canvasBitmap;
        //Bitmap source_bitmap = Drawing.convertImageToBitmap(randomImage, this);
        Log.d(MainActivity.TAG, ">>>>>>>>>> =convertImageToMatrix and print source matrix = ");
        int [][] source_matrix = Drawing.convertImageToMatrix(randomImage, this);
        Drawing.printMatrix(source_matrix, MainDrawingView.SIZE_PIXELS, MainDrawingView.SIZE_PIXELS);
        Log.d(MainActivity.TAG, ">>>>>>>>>> =convertBitmapToMatrix and print USER MATRIX =  ");

        int [][] user_matrix = Drawing.convertRectangleBitmapToMatrix(v.canvasBitmap);
        Drawing.printMatrix(user_matrix, MainDrawingView.drawingAreaHeight, MainDrawingView.screenWidthPixels);


        Log.d(MainActivity.TAG, ">>>>>>>>>> =convert rectangle matrix to square matrix and print SQUARE MATRIX =  ");

        int [][] square_user_matrix = Drawing.convertRectangleMatrixToSquareMatrix(user_matrix);

        Drawing.printMatrix(square_user_matrix, MainDrawingView.SIZE_PIXELS, MainDrawingView.SIZE_PIXELS);



        Log.d(MainActivity.TAG, ">>>>>>>>>> =countBlackPixels ");

        int blackSource = Drawing.countBlackPixels(source_matrix);
        Log.d(MainActivity.TAG, ">>>>>>>>>> =compareMatrices ");

        int [] result = Drawing.compareMatrices(source_matrix, square_user_matrix);
        Log.d(MainActivity.TAG, ">>>>>>>>>> =blackSourceAfterCompare ");

        int blackSourceAfterCompare = result[2];
        int badBlackPixels = result[1];

        int squaredDensityFactor = MainDrawingView.densityFactor * MainDrawingView.densityFactor;

        Log.d(MainActivity.TAG, "#############################formula##########################");


        double formula = ((double) (blackSource - blackSourceAfterCompare) / blackSource) * 1000;

        Log.d(MainActivity.TAG, "formula before decreasing = " + (int) formula);

        formula -= 0.5 * badBlackPixels / (squaredDensityFactor * MainDrawingView.densityFactor);

        long score = (long) formula;

        Log.d(MainActivity.TAG, "blackSource = " + blackSource);
        Log.d(MainActivity.TAG, "blackSourceAfterCompare = " + blackSourceAfterCompare);
        Log.d(MainActivity.TAG, "blackSourceAfterCompare / squaredDensityFactor = " + blackSourceAfterCompare / squaredDensityFactor);
        Log.d(MainActivity.TAG, "badBlackPixels = " + badBlackPixels);
        Log.d(MainActivity.TAG, "score = " + score);
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Log.d(MainActivity.TAG, "DisplayMetrics.DENSITY_DEFAULT  = " +  DisplayMetrics.DENSITY_DEFAULT);
        Log.d(MainActivity.TAG, "metrics.densityDpi  = " +  metrics.densityDpi);
        Log.d(MainActivity.TAG, "###########################formula############################");




        return score < 0 ?  0 : score;
    }

    //make this function generic by passing it arguments (and move to INFRA)
    public void showAlertDialogForRestart () {

        AlertDialog alertDialog = new AlertDialog.Builder(DrawingGame.this).create();
        alertDialog.setTitle("התחל מחדש                     ");
        alertDialog.setMessage("האם אתה בטוח כי ברצונך למחוק את הציור ולהתחיל מחדש?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "התחל מחדש",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        v.restartDrawing();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "בטל",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    public void flashHelperButtonClicked(View view) {
        v.setAllowDrawing(false);
        replaceHelper.setClickable(false);
        sourceImage.setVisibility(View.VISIBLE);
       // v.setBackground(ResourcesCompat.getDrawable(getResources(), randomImage, null));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            public void run()
            {
               // v.setBackgroundColor(Color.WHITE);
                sourceImage.setVisibility(View.GONE);
                v.setAllowDrawing(true);
                replaceHelper.setClickable(true);
            }
        }, flashHelperLength);
    }

    public void replaceDrawingButtonHelperClicked(View view) {
        v.restartDrawing();
        hideButtons();
        // uncomment to allow this helper
        //randomImage = images.get(1);
        startGame();
    }

    public void clueHelperButtonClicked(View view) {
    }

    public void changeBackgroundOpacity (int opacity) {
        activityBackground.getBackground().setAlpha(opacity);
    }
}
