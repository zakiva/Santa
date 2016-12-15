package com.example.zakiva.santa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
//import com.example.zakiva.santa.Helpers.VungleAds;
//import com.vungle.publisher.VunglePub;

import static com.example.zakiva.santa.Helpers.Infra.getTimeCodeFromServer;
import static com.example.zakiva.santa.Helpers.Infra.getTriviaDataFromFirebase;
import static com.example.zakiva.santa.Helpers.Infra.getWinnersFromFirebase;
import static com.example.zakiva.santa.Helpers.Infra.getUserAttributesFromFirebase;
import static com.example.zakiva.santa.Helpers.Storage.getStringPreferences;

public class Loader extends AppCompatActivity {

    public static final String TAG = ">>>>>>>Debug: ";
    private static int counter = 0;
    public static Loader loader;
    //final VunglePub vunglePub = VunglePub.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        loader = this;

        //name for pre-alpha
        String name = getStringPreferences("username02", getApplicationContext());
        if (name.equals("NONE")) {
            startActivity(new Intent(Loader.this, nameForPreAlpha.class));
            return;
        }

        //set the real user email instead
        ((Santa) this.getApplication()).setGlobalEmail(name);
        //this must end before continue to first user interface !!!


        // Initiate Vungle
        //VungleAds vungleAds = new VungleAds();
        //vungleAds.vungleInit(Loader.this);

        ImageView imageView = (ImageView) findViewById(R.id.loader_gif);
        Glide.with(this).load(R.drawable.loading_dots).crossFade().into(imageView);

        Log.d(TAG, " oncreatre ");

        Log.d(TAG, " this = null  " + this == null ? "yes" : "no");

        Log.d(TAG, " start loadrss  ");

        getTimeCodeFromServer();
        getTriviaDataFromFirebase();
        getWinnersFromFirebase(getApplicationContext());

        Log.d(TAG, " finish oncreatre ");
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
        if (counter == 5)
            startActivity(new Intent(Loader.this, Prize.class));
    }
}
