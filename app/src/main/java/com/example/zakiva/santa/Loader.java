package com.example.zakiva.santa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static com.example.zakiva.santa.Helpers.Infra.getTimeCodeFromServer;
import static com.example.zakiva.santa.Helpers.Infra.getTriviaDataFromFirebase;
import static com.example.zakiva.santa.Helpers.Infra.getUserAttributesFromFirebase;

public class Loader extends AppCompatActivity {

    public static final String TAG = ">>>>>>>Debug: ";
    private static int counter = 0;
    public static Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        loader = this;

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

        Log.d(TAG, " finish oncreatre ");

    }

    public static void increase () {
        counter++;
        Log.d(TAG, " increased, now counter = " + counter);
        loader.startApp();
    }

    public void startApp  () {
        if (counter == 4)
            startActivity(new Intent(Loader.this, MainActivity.class));
    }
}
