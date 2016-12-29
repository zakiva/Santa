package com.example.zakiva.santa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zakiva.santa.Models.Images;
//import com.example.zakiva.santa.Helpers.VungleAds;
//import com.vungle.publisher.VunglePub;

import java.util.ArrayList;
import java.util.Random;

import static com.example.zakiva.santa.Helpers.Drawing.printImagesNameOnDisk;
import static com.example.zakiva.santa.Helpers.Infra.getTimeCodeFromServer;
import static com.example.zakiva.santa.Helpers.Infra.getTriviaDataFromFirebase;
import static com.example.zakiva.santa.Helpers.Infra.getWinnersFromFirebase;
import static com.example.zakiva.santa.Helpers.Infra.getUserAttributesFromFirebase;
import static com.example.zakiva.santa.Helpers.Storage.*;
import static com.example.zakiva.santa.Models.Images.*;

public class Loader extends AppCompatActivity {

    public static final String TAG = ">>>>>>>Debug: ";
    private static int counter = 0;
    public static Loader loader;
    public static int IMAGES_QUEUE_SIZE = 3;
    //final VunglePub vunglePub = VunglePub.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        loader = this;

        // Initiate Vungle
        //VungleAds vungleAds = new VungleAds();
        //vungleAds.vungleInit(Loader.this);

        ImageView imageView = (ImageView) findViewById(R.id.loader_gif);
        Glide.with(this).load(R.drawable.loading_dots).crossFade().into(imageView);

        Log.d(TAG, " oncreatre ");

        Log.d(TAG, " this = null  " + this == null ? "yes" : "no");

        //set the real user email instead
        ((Santa) this.getApplication()).setGlobalEmail("userDemoEmail1");
        //this must end before continue to first user interface !!!

        Log.d(TAG, " start loadrss  ");

        getTimeCodeFromServer();
        getTriviaDataFromFirebase();
        getWinnersFromFirebase(getApplicationContext());
       // Log.d(TAG, "on create loader before clean");
      //  printImagesNameOnDisk(getApplicationContext());
        cleanOldImages();
       // Log.d(TAG, "on create loader after clean before init");
      //  printImagesNameOnDisk(getApplicationContext());
        initDrawing();
      //  Log.d(TAG, "on create loader after init");
      //  printImagesNameOnDisk(getApplicationContext());
        Log.d(TAG, " finish oncreatre ");
    }

    void cleanOldImages () {
        String image;
        for (int i = 0; i <= IMAGES_QUEUE_SIZE; i++) { // "<=" because we have one more for default
            image = getStringPreferences("oldImage" + i, getApplicationContext());
            if (!(image.equals("NONE")))
                deleteImageFromDisk(image, getApplicationContext());
        }
    }

    void initDrawing() {
        DrawingGame.sourceIndexes = new ArrayList<>();
        String image;
        for (int i = 0; i < IMAGES_QUEUE_SIZE; i++) {
            DrawingGame.sourceIndexes.add(new Random().nextInt(DrawingGame.NUMBER_OF_DRAWINGS));
        }
        for (int i = 0; i < DrawingGame.sourceIndexes.size(); i++) {
            image = "drawing" + DrawingGame.sourceIndexes.get(i) + ".jpg";
            Images.downloadImageToDisk(image, getApplicationContext());
            setStringPreferences("oldImage" + i, image, getApplicationContext());
        }
        DrawingGame.defaultIndex = DrawingGame.sourceIndexes.get(0); //for safety
        setStringPreferences("oldImage3", "drawing" + DrawingGame.defaultIndex + ".jpg", getApplicationContext());
    }

/*    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vunglePub.clearEventListeners();
    };*/

    public static void increase () {
        counter++;
        Log.d(TAG, " increased, now counter = " + counter);
        loader.startApp();
    }

    public void startApp  () {
        // 3 out of the 8 are images for drawing
        if (counter == 8)
            startActivity(new Intent(Loader.this, MainActivity.class));
    }
}
