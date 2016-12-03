package com.example.zakiva.santa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

       // Bitmap bitmap = Drawing.convertImageToBitmap(randomImage, this);
       // Bitmap coloredBitmap = Drawing.convertBlackToColor(bitmap);

        Log.d(MainActivity.TAG, ">>>>>>>>>> =after bitmaps befrep drawbale  ");

      //  Drawable d = new BitmapDrawable(getResources(), coloredBitmap);
       // sourceImage.setImageDrawable(d);
       // v.setBackground(d);

        Log.d(MainActivity.TAG, ">>>>>>>>>> =after draewbake before intent  ");


        final Intent intent = new Intent(DrawingGame.this, Score.class);
        intent.putExtra("score", calcScoreNew());
        intent.putExtra("game", "drawing");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            public void run()
            {
                sourceImage.setImageDrawable(null);
                MainDrawingView.context = null;
                startActivity(intent);
            }
        }, 0000);
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



    public long calcScoreNew () {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), randomImage, options);
        Bitmap sourceBitmap = Bitmap.createScaledBitmap(bitmap, MainDrawingView.SIZE_PIXELS, MainDrawingView.SIZE_PIXELS, true);


        Bitmap userBitmap = v.canvasBitmap;

        //printer(sourceBitmap);
       // printer(userBitmap);



        compareBitmaps(sourceBitmap, userBitmap);


        return 555;
    }

    public void compareBitmaps (Bitmap squared, Bitmap rectangle) {

        Log.d(MainActivity.TAG, "start comparing....");


        int missed = 0, bad = 0;

        int result;

        int startHeight = getStartHeight();

        for (int i = 0; i < squared.getHeight(); i += MainDrawingView.JUMP) {
            for (int j = 0; j < squared.getWidth(); j+= MainDrawingView.JUMP) {

                result = checkPixels(i, j, squared, rectangle, startHeight);

                if (result > 0) {
                    if (result == 1)
                        missed++;
                    else
                        bad++;
                }

            }
        }

        Log.d(MainActivity.TAG, "MISSED = "  + missed);
        Log.d(MainActivity.TAG, "BAD = "  + bad);
    }

    public int checkPixels (int i, int j, Bitmap squared, Bitmap rectangle, int startHeight) {


        int DELTA = 10, pixelSq, alphaSq, pixelRec, alphaRec;
        //ignore margins
        if (i <= DELTA || j <= DELTA || i >= squared.getHeight() - DELTA - 1 || j >= squared.getWidth() - DELTA - 1)
            return 0;


        pixelSq = squared.getPixel(j, i);
        alphaSq = Color.alpha(pixelSq);

        int start = Drawing.roundDown(-1 * DELTA, MainDrawingView.JUMP);

        for (int k = start; k < DELTA; k += MainDrawingView.JUMP) {
            for (int l = start; l < DELTA; l += MainDrawingView.JUMP) {

                pixelRec = rectangle.getPixel(j + k, i + startHeight + l);
                alphaRec = Color.alpha(pixelRec);

                if (alphaRec == 255) {

                    if (alphaSq == 255)
                        return 0;
                    else
                        return 2; //bad
                }
            }
        }

        if (alphaSq == 255)
            return 1; // missed

        return 0;
    }




    public int getStartHeight () {
        int startHeight = (MainDrawingView.screenHeightPixels - MainDrawingView.SIZE_PIXELS) / 2;
        startHeight = Drawing.roundDown(startHeight, MainDrawingView.JUMP);
        startHeight -= MainDrawingView.JUMP * 4 * MainDrawingView.densityFactor; //don't ask me why
        //make sure it is not negative
        startHeight = startHeight < 0 ? 0 : startHeight;
        Log.d(MainActivity.TAG, "$$$$$$$$$$startHeight = " + startHeight);
        return startHeight;
    }



    public void printer (Bitmap b) {

        int s = MainDrawingView.SIZE_PIXELS;

        Bitmap bitmap = Bitmap.createScaledBitmap(b, s, s, true);

        int[][] matrix = new int[s][s];
        int[][] pixels = new int[s][s];
        int[][] alphas = new int[s][s];

        int h = bitmap.getHeight();
        int w = bitmap.getWidth();

        Log.d(MainActivity.TAG, "original bitmap h = " + b.getHeight());
        Log.d(MainActivity.TAG, "new bitmap h = " + h);


        int pixel, alpha;

        int k = 0, l = 0;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {

                pixel = bitmap.getPixel(j, i);
                alpha = Color.alpha(pixel);

                pixels[k][l] = pixel;
                alphas[k][l] = alpha;

                if (pixel < -1 && alpha == 255) {
                    k = Drawing.roundDown(i, MainDrawingView.densityFactor * Drawing.JUMP);
                    l = Drawing.roundDown(j, MainDrawingView.densityFactor * Drawing.JUMP);
                    matrix[k][l] = 1;

                } else {
                }
            }
        }

        Log.d(MainActivity.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Log.d(MainActivity.TAG, "printMatrix(pixels) =  ");

       // Drawing.printMatrixValues(pixels, s, s);

        Log.d(MainActivity.TAG, "printMatrix(alphas) = ");

        Drawing.printMatrixValues(alphas, s, s);

        Log.d(MainActivity.TAG, "printMatrix (matrix) = ");

        Drawing.printMatrix(matrix, s, s);

        Log.d(MainActivity.TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
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
