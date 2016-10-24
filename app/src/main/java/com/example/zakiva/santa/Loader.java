package com.example.zakiva.santa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static com.example.zakiva.santa.Helpers.Infra.getTimeCodeFromServer;
import static com.example.zakiva.santa.Helpers.Infra.getTriviaDataFromFirebase;

public class Loader extends AppCompatActivity {

    public static final String TAG = ">>>>>>>Debug: ";
    private static int counter = 0;
    public static Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        loader = this;

        Log.d(TAG, " oncreatre ");

        Log.d(TAG, " this = null  " + this == null ? "yes" : "no");



        //set the real user email instead
        ((Santa) this.getApplication()).setGlobalEmail("userDemoEmail");
        //this must end before continue to first user interface !!!

        Log.d(TAG, " start loadrss  ");

        getTimeCodeFromServer();
        getTriviaDataFromFirebase();

        Log.d(TAG, " finish oncreatre ");

    }

    public static void increase () {
        counter++;
        loader.startApp();
    }

    public void startApp  () {
        if (counter == 3)
            startActivity(new Intent(Loader.this, MainActivity.class));
    }
}
