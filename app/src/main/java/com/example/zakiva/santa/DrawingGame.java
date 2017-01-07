package com.example.zakiva.santa;

import android.animation.ObjectAnimator;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zakiva.santa.Helpers.Drawing;
import com.example.zakiva.santa.Helpers.Infra;
import com.example.zakiva.santa.Models.Images;
import com.example.zakiva.santa.Models.MainDrawingView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static com.example.zakiva.santa.Helpers.Drawing.printImagesNameOnDisk;
import static com.example.zakiva.santa.Helpers.Storage.*;
import static com.example.zakiva.santa.Models.Images.*;
import static com.example.zakiva.santa.Models.MainDrawingView.density;
import static com.example.zakiva.santa.Models.MainDrawingView.densityFactor;

public class DrawingGame extends AppCompatActivity {

    public static ArrayList<Integer> sourceIndexes;
    public static int NUMBER_OF_DRAWINGS = 5; // must be greater than number of images in the queue
    public static int defaultIndex; // for safety if download has not been completed
    private Drawable sourceDrawble;
    private Bitmap sourceBitmap;
    private Bitmap sourceBitmapClue1;
    private Bitmap sourceBitmapClue2;
    private int millisecondsToShow;
    //private int randomImage;
    //private Button pen;
   // private Button eraser;
    private ImageView restart;
    private ImageView undo;
    private TextView replacePriceTextView;
    private TextView flashPriceTextView;
    private TextView cluePriceTextView;
    private TextView doneButton;
    private ImageView replaceHelper;
    private ImageView flashHelper;
    private ImageView clueHelper;
    private ImageView sourceImageView;
    private MainDrawingView v;
    private TextView candiesTextView;
    private ProgressBar stopperBar;
    private int flashHelperLength;
   // private ArrayList<Integer> images;
    private RelativeLayout activityBackground;
    private static HashMap<String,Boolean> enableHelpers,disableHelpers;
    private static int replaceHelperPrice = 50;
    private static int flashHelperPrice = 150;
    private static int clueHelperPrice = 200;
    private RelativeLayout flashBox;
    private RelativeLayout replaceBox;
    private RelativeLayout clueBox;
    public static HashSet<String> imagesOnDisk = new HashSet<>();
    private int currentIndex;
    private TextView stopper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MainActivity.TAG, "1>>>>>>>>>> default index = " + defaultIndex);
        Log.d(MainActivity.TAG, "1>>>>>>>>>> =current index =  " + currentIndex);
        Log.d(MainActivity.TAG, "1>>>>>>>>>> images on disk = " + imagesOnDisk.toString());

        setContentView(R.layout.activity_drawing_game);
        Drawing.initDrawingHelper();
        initFields();
        displayHelpersPrices();
        displayCandies();
        Log.d(MainActivity.TAG, "2>>>>>>>>>> default index = " + defaultIndex);
        Log.d(MainActivity.TAG, "2>>>>>>>>>> =current index =  " + currentIndex);
        Log.d(MainActivity.TAG, "2>>>>>>>>>> images on disk = " + imagesOnDisk.toString());
        startGame(); // including updating images and queue
    }

    public void updateImagesAndQueue () {
        Log.d(MainActivity.TAG, ">>>>>>>>>> sources indexes =   " + sourceIndexes.toString());
        Log.d(MainActivity.TAG, "updateImagesAndQueue>>>>>>>>>> default index = " + defaultIndex);
        Log.d(MainActivity.TAG, "updateImagesAndQueue>>>>>>>>>> =current index =  " + currentIndex);
        Log.d(MainActivity.TAG, "updateImagesAndQueue>>>>>>>>>> images on disk = " + imagesOnDisk.toString());

        //update Bitmap
        sourceBitmap = getBitmapFromDisk("drawing" + sourceIndexes.get(0) + ".png", getApplicationContext());
        //save source index for clues
        currentIndex = sourceIndexes.get(0);
        //sourceBitmapClue1 = getBitmapFromDisk("drawing" + sourceIndexes.get(0) + "Clue1.png", getApplicationContext());
        //sourceBitmapClue2 = getBitmapFromDisk("drawing" + sourceIndexes.get(0) + "Clue2.png", getApplicationContext());
        if (sourceBitmap == null || !(imagesOnDisk.contains("drawing" + sourceIndexes.get(0) + "Clue1.png")) || !(imagesOnDisk.contains("drawing" + sourceIndexes.get(0) + "Clue2.png"))) { // for safety if download has not been completed
            sourceBitmap = getBitmapFromDisk("drawing" + defaultIndex + ".png", getApplicationContext());
            currentIndex = defaultIndex;
            Log.d(MainActivity.TAG, ">>>>>>>>>> =go to default unforuntaly  ");

            // sourceBitmapClue1 = getBitmapFromDisk("drawing" + defaultIndex + "Clue1.png", getApplicationContext());
           // sourceBitmapClue2 = getBitmapFromDisk("drawing" + defaultIndex + "Clue2.png", getApplicationContext());
        }
        //update Drawable
        sourceDrawble = new BitmapDrawable(getResources(), sourceBitmap);
        sourceImageView.setImageDrawable(sourceDrawble);
        //remove first image from disk
        if (sourceIndexes.get(0) != defaultIndex) {
            deleteImageFromDisk("drawing" + sourceIndexes.get(0) + ".png", getApplicationContext());
           // deleteImageFromDisk("drawing" + sourceIndexes.get(0) + "Clue1.png", getApplicationContext());
           // deleteImageFromDisk("drawing" + sourceIndexes.get(0) + "Clue2.png", getApplicationContext());
        }
        //updateQueue
        sourceIndexes.remove(0);
        int n = new Random().nextInt(NUMBER_OF_DRAWINGS);
        int safe = 0;
        while (sourceIndexes.contains(n) && safe < 1000) {
            n = new Random().nextInt(NUMBER_OF_DRAWINGS);
            safe++;
            if (safe > 900) // should never happen
                Log.d(MainActivity.TAG, ">>>>>>>>>safe!!!!!! not good !!  ");
        }
        sourceIndexes.add(n);
        //download last image
        String newImage = "drawing" + sourceIndexes.get(sourceIndexes.size() - 1);
        Images.downloadImageToDisk(newImage + ".png", getApplicationContext());
        Images.downloadImageToDisk(newImage + "Clue1.png", getApplicationContext());
        Images.downloadImageToDisk(newImage + "Clue2.png", getApplicationContext());
        //shift the old images in Preferences
        setStringPreferences("oldImage0", getStringPreferences("oldImage1", getApplicationContext()), getApplicationContext());
        setStringPreferences("oldImage1", getStringPreferences("oldImage2", getApplicationContext()), getApplicationContext());
        setStringPreferences("oldImage2", newImage, getApplicationContext());
    }

    //flow
    public void initFields() {
        millisecondsToShow = 3000;
        flashHelperLength = 2000;
       // pen = (Button) findViewById(R.id.pen);
       // eraser = (Button) findViewById(R.id.eraser);
        restart = (ImageView) findViewById(R.id.restart);
        undo = (ImageView) findViewById(R.id.undo);
        doneButton = (TextView) findViewById(R.id.doneButton);
        flashHelper = (ImageView) findViewById(R.id.flashHelperButton);
        replaceHelper = (ImageView) findViewById(R.id.replaceHelperButton);
        clueHelper = (ImageView) findViewById(R.id.clueHelperButton);
        v = (MainDrawingView) findViewById(R.id.single_touch_view);
        //images = new ArrayList<Integer>(Arrays.asList(R.drawable.bone700sq, R.drawable.heart700sq, R.drawable.house700sq, R.drawable.nike700sq, R.drawable.tree700sq));
       // images = new ArrayList<Integer>(Arrays.asList(R.drawable.rec_400));
        //Collections.shuffle(images);
        //randomImage = images.get(0);
        activityBackground = (RelativeLayout) findViewById(R.id.activityBackground);
        activityBackground.getBackground().setAlpha(0);
        sourceImageView = (ImageView) findViewById(R.id.sourceImage);
        stopperBar = (ProgressBar) findViewById(R.id.stopperBar);
        candiesTextView = (TextView) findViewById(R.id.candiesNumber);
        disableHelpers = new HashMap<>();
        enableHelpers = new HashMap<>();
        initDisableEnable(disableHelpers, false, false, false);
        initDisableEnable(enableHelpers, true, true, true);
        flashBox = (RelativeLayout) findViewById(R.id.flashBox);
        replaceBox = (RelativeLayout) findViewById(R.id.replaceBox);
        clueBox = (RelativeLayout) findViewById(R.id.clueBox);
        stopper = (TextView) findViewById(R.id.stopper);
    }

    public void displayHelpersPrices () {
        flashPriceTextView = (TextView) findViewById(R.id.flashPrice);
        replacePriceTextView = (TextView) findViewById(R.id.replacePrice);
        cluePriceTextView = (TextView) findViewById(R.id.cluePrice);

        flashPriceTextView.setText(flashHelperPrice + "");
        replacePriceTextView.setText(replaceHelperPrice + "");
        cluePriceTextView.setText(clueHelperPrice + "");
    }

    public void initDisableEnable(HashMap helpers, boolean flash, boolean replace, boolean clue) {
        helpers.put("flash", flash);
        helpers.put("replace", replace);
        helpers.put("clue", clue);
    }

    public void startGame() {
        //show the source image
        //v.setBackground(ResourcesCompat.getDrawable(getResources(), randomImage, null));

        disableEnableViews(disableHelpers);
        updateImagesAndQueue();
        //sourceImageView.setVisibility(View.VISIBLE);

        stopper.setVisibility(View.VISIBLE);
        runStopperBar();

        new CountDownTimer(millisecondsToShow, 100) {

            public void onTick(long millisUntilFinished) {
//                Log.d(MainActivity.TAG, ">>>>>>>>>> =millisUntilFinished " + millisUntilFinished);
                // cancel DRAW IT before starting gmae
                // if (millisUntilFinished / 1000 == 0) {
                //     v.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.draw_it, null));
                // }
                stopper.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                startDraw();
            }
        }.start();
    }

    public void startDraw() {
        //v.setBackgroundColor(Color.WHITE);
        //for checking only:
        //v.setBackground(ResourcesCompat.getDrawable(getResources(), randomImage, null));

        // sourceImageView.setVisibility(View.GONE);

        sourceBitmapClue1 = getBitmapFromDisk("drawing" + currentIndex + "Clue1.png", getApplicationContext());
        sourceBitmapClue2 = getBitmapFromDisk("drawing" + currentIndex + "Clue2.png", getApplicationContext());

        if (currentIndex != defaultIndex && !(sourceIndexes.contains(currentIndex))) {
            deleteImageFromDisk("drawing" + currentIndex + "Clue1.png", getApplicationContext());
            deleteImageFromDisk("drawing" + currentIndex + "Clue2.png", getApplicationContext());
        }


        sourceDrawble = new BitmapDrawable(getResources(), sourceBitmapClue1);
        sourceImageView.setImageDrawable(sourceDrawble);
        disableEnableViews(enableHelpers);
        stopper.setVisibility(View.GONE);
        stopperBar.setVisibility(View.GONE);

        Log.d(MainActivity.TAG, "3>>>>>>>>>> default index = " + defaultIndex);
        Log.d(MainActivity.TAG, "3>>>>>>>>>> =current index =  " + currentIndex);
        Log.d(MainActivity.TAG, "3>>>>>>>>>> images on disk = " + imagesOnDisk.toString());



        v.setAllowDrawing(true);
        //pen.setVisibility(View.VISIBLE);
        //eraser.setVisibility(View.VISIBLE);
        restart.setVisibility(View.VISIBLE);
        undo.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.VISIBLE);
        replaceHelper.setVisibility(View.VISIBLE);
        clueHelper.setVisibility(View.VISIBLE);
        flashHelper.setVisibility(View.VISIBLE);
    }

    //listeners
    public void doneButtonClicked(View view) {

        Log.d(MainActivity.TAG, "DONE>>>>>>>>>> default index = " + defaultIndex);
        Log.d(MainActivity.TAG, "DONE>>>>>>>>>> =current index =  " + currentIndex);
        Log.d(MainActivity.TAG, "DONE>>>>>>>>>> images on disk = " + imagesOnDisk.toString());

        doneButton.setClickable(false);
        disableEnableViews(disableHelpers);


        v.setAllowDrawing(false);

        //sourceImageView.setVisibility(View.VISIBLE);
        sourceDrawble = new BitmapDrawable(getResources(), sourceBitmap);
        sourceImageView.setImageDrawable(sourceDrawble);

        //hideButtons();

        Log.d(MainActivity.TAG, ">>>>>>>>>> =before botmaps  ");

        // Bitmap bitmap = Drawing.convertImageToBitmap(randomImage, this);
        // Bitmap coloredBitmap = Drawing.convertBlackToColor(bitmap);

        Log.d(MainActivity.TAG, ">>>>>>>>>> =after bitmaps befrep drawbale  ");

        //  Drawable d = new BitmapDrawable(getResources(), coloredBitmap);
        // sourceImage.setImageDrawable(d);
        // v.setBackground(d);

        Log.d(MainActivity.TAG, ">>>>>>>>>> =after draewbake before intent  ");


        final Intent intent = new Intent(DrawingGame.this, Score.class);

        Date startTime = getCurrentTime();
        intent.putExtra("score", calcScoreNew());
        Date endTime = getCurrentTime();

        long millisecondsDelta = (endTime.getTime() - startTime.getTime());
        long timeForHandler = 2000 - millisecondsDelta >= 0 ? 2000 - millisecondsDelta : 0;

        intent.putExtra("game", "drawing");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sourceImageView.setImageDrawable(null);
                MainDrawingView.context = null;
                startActivity(intent);
            }
        }, timeForHandler);
    }

    public Date getCurrentTime () {
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp stamp = new Timestamp(currentTimeMillis);
        Date currentTime = new Date(stamp.getTime());
        return currentTime;
    }

    public void hideButtons() {
        //pen.setVisibility(View.GONE);
        //eraser.setVisibility(View.GONE);
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
            //pen.setBackgroundResource(R.drawable.pen_icon);
            //eraser.setBackgroundResource(R.drawable.eraser_icon_color);
        } else {
            //pen.setBackgroundResource(R.drawable.pen_icon_color);
            //eraser.setBackgroundResource(R.drawable.eraser_icon);
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

    /*

    public long calcScore() {
        //Bitmap draw_bitmap = v.canvasBitmap;
        //Bitmap source_bitmap = Drawing.convertImageToBitmap(randomImage, this);
        Log.d(MainActivity.TAG, ">>>>>>>>>> =convertImageToMatrix and print source matrix = ");
        int[][] source_matrix = Drawing.convertImageToMatrix(randomImage, this);
        Drawing.printMatrix(source_matrix, MainDrawingView.SIZE_PIXELS, MainDrawingView.SIZE_PIXELS);
        Log.d(MainActivity.TAG, ">>>>>>>>>> =convertBitmapToMatrix and print USER MATRIX =  ");

        int[][] user_matrix = Drawing.convertRectangleBitmapToMatrix(v.canvasBitmap);
        Drawing.printMatrix(user_matrix, MainDrawingView.drawingAreaHeight, MainDrawingView.screenWidthPixels);


        Log.d(MainActivity.TAG, ">>>>>>>>>> =convert rectangle matrix to square matrix and print SQUARE MATRIX =  ");

        int[][] square_user_matrix = Drawing.convertRectangleMatrixToSquareMatrix(user_matrix);

        Drawing.printMatrix(square_user_matrix, MainDrawingView.SIZE_PIXELS, MainDrawingView.SIZE_PIXELS);


        Log.d(MainActivity.TAG, ">>>>>>>>>> =countBlackPixels ");

        int blackSource = Drawing.countBlackPixels(source_matrix);
        Log.d(MainActivity.TAG, ">>>>>>>>>> =compareMatrices ");

        int[] result = Drawing.compareMatrices(source_matrix, square_user_matrix);
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
        Log.d(MainActivity.TAG, "DisplayMetrics.DENSITY_DEFAULT  = " + DisplayMetrics.DENSITY_DEFAULT);
        Log.d(MainActivity.TAG, "metrics.densityDpi  = " + metrics.densityDpi);
        Log.d(MainActivity.TAG, "###########################formula############################");


        return score < 0 ? 0 : score;
    }
    */

    public long calcScoreNew() {

        boolean DEBUG = false;

        if (MainDrawingView.blackPixelsCounter > 500) {
            return 0; //there are too many black pixels in the user matrix - to avoid long calculation time
        }

      //  BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inScaled = false;
        //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), randomImage, options);
       // Bitmap bitmap = getBitmapFromDisk("drawing0.jpg", getApplicationContext());
        Bitmap sourceScaledBitmap = Bitmap.createScaledBitmap(sourceBitmap, MainDrawingView.SIZE_PIXELS, MainDrawingView.SIZE_PIXELS, true);


        Bitmap userBitmap = v.canvasBitmap;

        Log.d(MainActivity.TAG, " ##################  printer(sourceBitmap);  ################### = ");


        if (DEBUG) {
            printer(sourceScaledBitmap);
            Drawing.printBitmap(sourceScaledBitmap, sourceScaledBitmap.getHeight(), sourceScaledBitmap.getWidth());
        }
        //printer(userBitmap);
        Log.d(MainActivity.TAG, ">>>>>>>>>> printer(userBitmap) =  ");
        byte[] bytesArray = Drawing.getBytesPixels(userBitmap);
        Log.d(MainActivity.TAG, "0:" + bytesArray[0]);
        Log.d(MainActivity.TAG, "1:" + bytesArray[1]);
        Log.d(MainActivity.TAG, "2:" + bytesArray[2]);

        //Log.d(MainActivity.TAG, "Arrays.toString(byteArray) = " + Arrays.toString(bytesArray));

        Log.d(MainActivity.TAG, " ##################  userBytesMatrix  ################### = ");

        byte[][] userBytesMatrix = Drawing.convertBitmapToBytesMatrix(userBitmap);

        if (DEBUG)
            Drawing.printBytesMatrix(userBytesMatrix, userBitmap.getHeight(), userBitmap.getWidth());

        //   for (int i=0; i < bytesArray.length; i++) {
        //       if (bytesArray[i] != 0)
        //          Log.d(MainActivity.TAG, "NOT A ZERO !!!!!!!!!");
        // }


        int s = MainDrawingView.SIZE_PIXELS;

        // Bitmap sourceBitmapFixedSize = Bitmap.createScaledBitmap(sourceBitmap, s, s, true);


        int score = formula(compareSourceBitmapToUserMatrix(sourceScaledBitmap, userBytesMatrix));

        return score;
    }

    int formula (HashMap<String, Integer> data) {

        int density =  MainDrawingView.densityFactor;
        int squaredDensity = density * density;

        Log.d(MainActivity.TAG, " density  = " + density);
        Log.d(MainActivity.TAG, " squaredDensity =  " + squaredDensity);


        int score = 1000;
        double rate = 1.0 * (data.get("black") - data.get("missed")) / data.get("black"); //what about density here?
        score *= rate;
        score -= data.get("bad") * 1.0 / squaredDensity; // need to consider density
        score -= score > 990 ? new Random().nextInt(30) : 0; // need some luck to be the best ;)
        return score < 0 ? 0 : score;
    }

    int globalBlackSource;

    public HashMap<String, Integer> compareSourceBitmapToUserMatrix(Bitmap squared, byte[][] rectangle) {

        Log.d(MainActivity.TAG, "start comparing....");

        Log.d(MainActivity.TAG, " ###########################printer from cpmareBitmaps: ############################");


        globalBlackSource = 0;

        int missed = 0, bad = 0, outOfSquared = 0;

        int DELTA = 40;

        int startHeight = getStartHeight();

        int START_BY_DELTA = Drawing.roundDownNegative(-1 * DELTA, MainDrawingView.JUMP);

        //check metrics

        for (int i = 0; i < squared.getHeight(); i += MainDrawingView.JUMP) {
            for (int j = 0; j < squared.getWidth(); j += MainDrawingView.JUMP) {


                if (isMissed(i, j, squared, rectangle, startHeight, DELTA * densityFactor, START_BY_DELTA))
                    missed++;
                else if (isBad(i, j, squared, rectangle, startHeight, DELTA * densityFactor, START_BY_DELTA))
                    bad++;



                /*


                int pixelSq = squared.getPixel(j, i);
                int alphaSq = Color.alpha(pixelSq);
                if (alphaSq != 255)
                    Log.d(MainActivity.TAG, "alpha = " + alphaSq);

                    */





                /*

                result = checkPixels(i, j, squared, rectangle, startHeight);





                if (result > 0) {
                    if (result == 1) {
                      //  Log.d(MainActivity.TAG, "updated missed..");
                        missed++;
                    }
                    else {
                        bad++; //result == 2
                    }
                }
                */

            }
        }

        //search for bad pixels above the square
        for (int i = 0; i < startHeight - DELTA / 2 ; i += MainDrawingView.JUMP) {
            for (int j = 0; j < squared.getWidth(); j += MainDrawingView.JUMP) {

                if (rectangle[i][j] != 0)
                    outOfSquared++;
            }
        }
        //search for bad pixels below the square
        for (int i = squared.getHeight() + DELTA / 2 + startHeight; i < rectangle.length; i += MainDrawingView.JUMP) {
            for (int j = 0; j < squared.getWidth(); j += MainDrawingView.JUMP) {

                if (rectangle[i][j] != 0)
                    outOfSquared++;
            }
        }


        Log.d(MainActivity.TAG, "rectangle.length = " + rectangle.length);
        Log.d(MainActivity.TAG, "rectangle[0].length = " + rectangle[0].length);
        Log.d(MainActivity.TAG, "startHeigfht = " + startHeight);



        Log.d(MainActivity.TAG, "$$MISSED = " + missed);
        Log.d(MainActivity.TAG, "BAD = " + bad);
        Log.d(MainActivity.TAG, "outOfSquared = " + outOfSquared);
        Log.d(MainActivity.TAG, "GLOBALBLACKSOURCE = " + globalBlackSource);
        Log.d(MainActivity.TAG, "DELTA = " + DELTA);
        Log.d(MainActivity.TAG, "start = " + START_BY_DELTA);
        Log.d(MainActivity.TAG, "squared.getHeight() = " + squared.getHeight());
        Log.d(MainActivity.TAG, "squared.getWidth() = " + squared.getWidth());
        Log.d(MainActivity.TAG, "MainDrawingView.blackPixelsCounter = " + MainDrawingView.blackPixelsCounter);



        HashMap<String, Integer> result = new HashMap<>();
        result.put("bad", bad + outOfSquared);
        result.put("missed", missed);
        result.put("black", globalBlackSource);
        return result;
    }


    //check for specific pixel in the source bitmap:
    // does exist a corresponding pixel in the user matrix (including DELTA)?
    public boolean isMissed(int i, int j, Bitmap squared, byte[][] rectangle, int startHeight, int DELTA, int start) {

        if (i % (MainDrawingView.JUMP * densityFactor) != 0)
            return false; // we ignore lines that are not aligned to jump and density

        //ignore margins
       // if (i <= DELTA || j <= DELTA || i >= squared.getHeight() - DELTA - 1 || j >= squared.getWidth() - DELTA - 1)
       //     return false;

        int pixelSq = squared.getPixel(j, i);



        if (pixelSq == -1) // if the source pixel is white we can't miss it :)
            return false;

        int x, y;

        byte byteRec;



        //the source pixel is black - search for black pixel in the user matrix:

        globalBlackSource++;


        for (int k = start; k < DELTA; k += MainDrawingView.JUMP) {
            for (int l = start; l < DELTA; l += MainDrawingView.JUMP) {

                y = i + startHeight + l;

                x = j + k;

                if (y < 0 || y >= rectangle.length || x < 0 || x >= rectangle[0].length)
                    continue;

                byteRec = rectangle[y][x];

                if (byteRec != 0) // found black user pixel -> not missed
                    return false;
            }
        }

        return true;
    }


    //check for specific pixel in the matrix bitmap:
    // does exist a corresponding pixel in the source bitmap (including DELTA)?
    public boolean isBad(int i, int j, Bitmap squared, byte[][] rectangle, int startHeight, int DELTA, int start) {

        //ignore margins
        //if (i <= DELTA || j <= DELTA || i >= squared.getHeight() - DELTA - 1 || j >= squared.getWidth() - DELTA - 1)
         //   return false;

        int pixelSq;

        //just for safety - should never exceed:
        if (i + startHeight >= rectangle.length)
            return false;

        byte byteRec = rectangle[i + startHeight][j];

        if (byteRec == 0) // if the user pixel is white it can't be bad :)
            return false;

        //the user pixel is black - search for black pixel in the source bitmap:

        int x, y;

        for (int k = start; k < DELTA; k += MainDrawingView.JUMP * densityFactor) { // * density factor ?
            for (int l = start; l < DELTA; l += MainDrawingView.JUMP * densityFactor) { // * density factor ?

                x = j + k;
                y = i + l;

                if (y < 0 || y >= squared.getHeight() || x < 0 || x >= squared.getWidth())
                    continue;

                pixelSq = squared.getPixel(x, y);

                if (pixelSq < -1) // found black source pixel -> not bad
                    return false;
            }
        }
        //Log.d(MainActivity.TAG, "returning isBad() == true on pixel: (" + (i + startHeight) + "," + j +")");

        return true;
    }



    /*



    public int checkPixels (int i, int j, Bitmap squared, byte [][] rectangle, int startHeight) {

        int DELTA = 10, pixelSq, alphaSq;
        byte byteRec;
        //ignore margins
        if (i <= DELTA || j <= DELTA || i >= squared.getHeight() - DELTA - 1 || j >= squared.getWidth() - DELTA - 1)
            return 0;


        pixelSq = squared.getPixel(j, i);
        //alphaSq = Color.alpha(pixelSq);

        int start = Drawing.roundDown(-1 * DELTA, MainDrawingView.JUMP);

        if (pixelSq < -1) {

           // Log.d(MainActivity.TAG, "YES");



            for (int k = start; k < DELTA; k += MainDrawingView.JUMP) {
                for (int l = start; l < DELTA; l += MainDrawingView.JUMP) {

                    byteRec = rectangle[i + startHeight + l][j + k];

                    if (byteRec != 0) {

                        return 0; //good

                    }
                }
            }

           // Log.d(MainActivity.TAG, ">>>>>>>>>>>>>>>>>>>>MISSED<<<<<<<<<<<<<<<<<<<");


            return 1; //missed

        }

        else {
            //Log.d(MainActivity.TAG, "NO");

            for (int k = start; k < DELTA; k += MainDrawingView.JUMP) {
                for (int l = start; l < DELTA; l += MainDrawingView.JUMP) {

                    byteRec = rectangle[i + startHeight + l][j + k];

                    if (byteRec != 0) {

                        return 2; //bad

                    }
                }
            }
            return 0; //good
        }
    }

    */


    public int getStartHeight() {
        int startHeight = (MainDrawingView.screenHeightPixels - MainDrawingView.SIZE_PIXELS) / 2;
        startHeight = Drawing.roundDown(startHeight, MainDrawingView.JUMP);
        startHeight -= MainDrawingView.JUMP * 4 * MainDrawingView.densityFactor; //don't ask me why
        //make sure it is not negative
        startHeight = startHeight < 0 ? 0 : startHeight;
        Log.d(MainActivity.TAG, "$$$$$$$$$$startHeight = " + startHeight);
        return startHeight;
    }


    public void printer(Bitmap b) {

        int s = MainDrawingView.SIZE_PIXELS;

        //Bitmap bitmap = Bitmap.createScaledBitmap(b, s, s, true);

        Bitmap bitmap = b;

        int[][] matrix = new int[s][s];
        int[][] pixels = new int[s][s];
        int[][] alphas = new int[s][s];
        // byte[][] bytes = new byte[s][s];

        int h = bitmap.getHeight();
        int w = bitmap.getWidth();

        Log.d(MainActivity.TAG, "original bitmap h = " + b.getHeight());
        Log.d(MainActivity.TAG, "new bitmap h = " + h);


        int pixel, alpha;
        //byte myByte;

        int k = 0, l = 0;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {

                pixel = bitmap.getPixel(j, i);
                alpha = Color.alpha(pixel);

                //if (alpha != 255)
                  //  Log.d(MainActivity.TAG, "alpha = " + alpha);

                k = Drawing.roundDown(i, MainDrawingView.densityFactor * Drawing.JUMP);
                l = Drawing.roundDown(j, MainDrawingView.densityFactor * Drawing.JUMP);


                //myByte = bitmap.getPixel(j, i);

                pixels[k][l] = pixel;
                alphas[k][l] = alpha;
                //bytes[k][l] = myByte;

                if (pixel < -1 && alpha == 255) {
                    matrix[k][l] = 1;

                } else {
                }
            }
        }

        Log.d(MainActivity.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Log.d(MainActivity.TAG, "printMatrix(pixels) =  ");

        Drawing.printMatrixValues(pixels, s, s, "pixel");

        Log.d(MainActivity.TAG, "printMatrix(alphas) = ");

        Drawing.printMatrixValues(alphas, s, s, "alpha");

        Log.d(MainActivity.TAG, "printMatrix (matrix) = ");

        Drawing.printMatrix(matrix, s, s);

        Log.d(MainActivity.TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }


    //make this function generic by passing it arguments (and move to INFRA)
    public void showAlertDialogForRestart() {

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
        if (!updateCandies(flashHelperPrice)) {
            notifyNoCandies();
            return;
        }
        v.setAllowDrawing(false);
        disableEnableViews(disableHelpers);
        enableHelpers.put("flash",false);
        flashHelper.setBackgroundResource(R.drawable.flash_disable);
        final Drawable oldSource = sourceDrawble;
        sourceDrawble = new BitmapDrawable(getResources(), sourceBitmap);
        sourceImageView.setImageDrawable(sourceDrawble);
        //sourceImageView.setVisibility(View.VISIBLE);
        // v.setBackground(ResourcesCompat.getDrawable(getResources(), randomImage, null));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // v.setBackgroundColor(Color.WHITE);
                //sourceImageView.setVisibility(View.GONE);
                sourceDrawble = oldSource;
                sourceImageView.setImageDrawable(sourceDrawble);
                v.setAllowDrawing(true);
                disableEnableViews(enableHelpers);
            }
        }, flashHelperLength);
    }

    public void replaceDrawingButtonHelperClicked(View view) {
        if (!updateCandies(replaceHelperPrice)) {
            notifyNoCandies();
            return;
        }
        v.setAllowDrawing(false);
        enableHelpers.put("replace",false);
        replaceHelper.setBackgroundResource(R.drawable.next_disable);

        Handler handler = new Handler(); // to allow UI thread to update view
        handler.postDelayed(new Runnable() {
            public void run() {
                v.restartDrawing();
                startGame();
            }
        }, 1);
    }

    public void clueHelperButtonClicked(View view) {
        if (!updateCandies(clueHelperPrice)) {
            notifyNoCandies();
            return;
        }

        enableHelpers.put("clue",false);
        disableEnableViews(enableHelpers);
        clueHelper.setBackgroundResource(R.drawable.hint_disable);

        sourceDrawble = new BitmapDrawable(getResources(), sourceBitmapClue2);
        sourceImageView.setImageDrawable(sourceDrawble);
    }

    public void changeBackgroundOpacity(int opacity) {
        activityBackground.getBackground().setAlpha(opacity);
    }

    public void runStopperBar() {
        stopperBar.getProgressDrawable().setColorFilter(Color.parseColor("#9254ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        stopperBar.setVisibility(View.VISIBLE);
        ObjectAnimator animation = ObjectAnimator.ofInt(stopperBar, "progress", 0, 100);
        animation.setDuration(millisecondsToShow);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    public boolean updateCandies (int price) {
        if (MainActivity.candies < price)
            return false;
        long new_candies = MainActivity.candies - price;
        Infra.addCandiesToUser(new_candies);
        MainActivity.setCandies(new_candies);
        displayCandies();
        return true;
    }

    public void notifyNoCandies () {
        Toast toast = Toast.makeText(this, "Need more candies mateee!!", Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        toast.show();
    }

    public void displayCandies() {
        candiesTextView.setText(MainActivity.candies + "");
    }

    public void disableEnableViews(HashMap<String,Boolean> helpers){
        flashHelper.setClickable(helpers.get("flash"));
        flashBox.setClickable(helpers.get("flash"));
        replaceHelper.setClickable(helpers.get("replace"));
        replaceBox.setClickable(helpers.get("replace"));
        clueHelper.setClickable(helpers.get("clue"));
        clueBox.setClickable(helpers.get("clue"));
    }
}
