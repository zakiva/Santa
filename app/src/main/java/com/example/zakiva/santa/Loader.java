package com.example.zakiva.santa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zakiva.santa.Helpers.Drawing;
import com.example.zakiva.santa.Helpers.Infra;
import com.example.zakiva.santa.Helpers.Storage;
import com.example.zakiva.santa.Models.Images;
import com.example.zakiva.santa.Models.Token;
//import com.example.zakiva.santa.Helpers.VungleAds;
//import com.vungle.publisher.VunglePub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static com.example.zakiva.santa.Helpers.Drawing.printImagesNameOnDisk;
import static com.example.zakiva.santa.Helpers.Infra.*;
import static com.example.zakiva.santa.Helpers.Storage.*;
import static com.example.zakiva.santa.Models.Images.*;

public class Loader extends AppCompatActivity {

    public static final String TAG = ">>>>>>>Debug: ";
    private static int counter = 0;
    public static Loader loader;
    public static int IMAGES_QUEUE_SIZE = 3;
    public static String userEmailFieldName  = "userEmail";
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

        String email = Storage.getStringPreferences(userEmailFieldName ,this.getApplicationContext());
        ((Santa) this.getApplication()).setSignedUpType(Storage.getStringPreferences("signedUpType",getApplicationContext()));
        Log.d(">>>>>>>>>>>>","what's your email address:  "+email);
        if (email.equals("NONE")){
        //set the real user email instead
            ((Santa) this.getApplication()).setGlobalEmail(Token.getID(getApplicationContext()));
            Log.d(MainActivity.TAG,"is sign up ?????"+ ((Santa) this.getApplication()).getSignedUpType());
            //this must end before continue to first user interface !!!
        }
        else{
            ((Santa) this.getApplication()).setGlobalEmail(email);
            Log.d(MainActivity.TAG,"is sign up ?????"+ ((Santa) this.getApplication()).getSignedUpType());
        }
        Log.d(TAG, " start loadrss  ");

        getGlobalFieldsFromFirebase();
        initTrivia();
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
            if (!(image.equals("NONE"))) {
                deleteImageFromDisk(image + ".png", getApplicationContext());
                deleteImageFromDisk(image + "Clue1.png", getApplicationContext());
                deleteImageFromDisk(image + "Clue2.png", getApplicationContext());
            }
        }
    }

    void initDrawing() {
        DrawingGame.sourceIndexes = new ArrayList<>();
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < DrawingGame.NUMBER_OF_DRAWINGS; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        String image;
        for (int i = 0; i < IMAGES_QUEUE_SIZE; i++) {
            DrawingGame.sourceIndexes.add(numbers.get(i));
        }
        for (int i = 0; i < DrawingGame.sourceIndexes.size(); i++) {
            image = "drawing" + DrawingGame.sourceIndexes.get(i);
            Images.downloadImageToDisk(image + ".png", Images.STORAGE_DRAWINGS_FOLDER, getApplicationContext());
            Images.downloadImageToDisk(image + "Clue1.png", Images.STORAGE_DRAWINGS_FOLDER, getApplicationContext());
            Images.downloadImageToDisk(image + "Clue2.png", Images.STORAGE_DRAWINGS_FOLDER, getApplicationContext());
            setStringPreferences("oldImage" + i, image, getApplicationContext());
        }
        DrawingGame.defaultIndex = DrawingGame.sourceIndexes.get(0); //for safety
        setStringPreferences("oldImage3", "drawing" + DrawingGame.defaultIndex, getApplicationContext());
    }

    void initTrivia() {
        TriviaGame.dataHash = new HashMap<>();
        TriviaGame.sheetsMapping = new HashMap<>();
        TriviaGame.sheetsIndexs = new ArrayList<>();
        for (int i = 0; i < TriviaGame.allSheetsNames.length; i ++) {
            TriviaGame.sheetsMapping.put(i, TriviaGame.allSheetsNames[i]);
            TriviaGame.sheetsIndexs.add(i);
        }
        Collections.shuffle(TriviaGame.sheetsIndexs);
        TriviaGame.currentSheetsNames = new String[TriviaGame.NUMBER_OF_QUESTIONS + 1];
        TriviaGame.nextSheetsNames = new String[TriviaGame.NUMBER_OF_QUESTIONS + 1];
        for (int i = 0; i < TriviaGame.NUMBER_OF_QUESTIONS + 1; i++) {
            TriviaGame.currentSheetsNames[i] = TriviaGame.sheetsMapping.get(TriviaGame.sheetsIndexs.get(i));
        }
        int offset = TriviaGame.NUMBER_OF_QUESTIONS + 1;
        for (int i = 0; i < TriviaGame.NUMBER_OF_QUESTIONS + 1; i++) {
            TriviaGame.nextSheetsNames[i] = TriviaGame.sheetsMapping.get(TriviaGame.sheetsIndexs.get(i + offset));
        }
        getTriviaDataFromFirebase(TriviaGame.currentSheetsNames);
        getTriviaDataFromFirebase(TriviaGame.nextSheetsNames);

        TriviaGame.currentSheetsOffset = TriviaGame.NUMBER_OF_QUESTIONS * 2 + 2;

        Log.d(TAG, " sheets name =  ");
        for (int i = 0; i < TriviaGame.sheetsIndexs.size(); i ++) {
            Log.d(TAG, TriviaGame.sheetsMapping.get(TriviaGame.sheetsIndexs.get(i)));
        }
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
        // 9 out of the 14 are images for drawing
        if (counter == 15)
            startActivity(new Intent(Loader.this, MainActivity.class));
    }
}
