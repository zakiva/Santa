package com.example.zakiva.santa;

import com.example.zakiva.santa.Helpers.*;
import static com.example.zakiva.santa.Helpers.Infra.*;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = ">>>>>>>Debug: ";
    private static String timeCode;
    private static Activity activity;
    public static long candies;
    public static long INIT_CANDIES_NUMBER = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        //set the real user email instead
        ((Santa) this.getApplication()).setGlobalEmail("userDemoEmail");

       // Infra.addSheet("c2", Generator.prepareCountriesData());
        //this must end before continue to first user interface !!!
        getTimeCodeFromServer();
        getTriviaDataFromFirebase();
    }

    public static void setTimeCode(String timeCode) {
        MainActivity.timeCode = timeCode;
        initInfra(((Santa) activity.getApplication()).getGlobalEmail(), timeCode);
        initUserCandies(INIT_CANDIES_NUMBER);
    }

    public static void setCandies(long c) {
        candies = c;
        Log.d(TAG, "setCandies = " + c);
    }

    public static String getTimeCode () {
        return timeCode;
    }

    public void startClicked(View view) {
        addUser();
        //addCompetition("compeexample", "giftexample");
        getUser();
        //getCompetition("compeexample");
    }

    public void goToGoogle(View v) {
        startActivity(new Intent(MainActivity.this, Google.class));
    }

    public void triviaClicked(View view) {
        Log.d(TAG, " trivia clicked and time code field = " + timeCode);
        startActivity(new Intent(MainActivity.this, Trivia.class));
    }
    public void triviaGameClicked(View view) {
        startActivity(new Intent(MainActivity.this, TriviaGame.class));
    }
    public void goToFb(View v) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        startActivity(new Intent(MainActivity.this, Facebook.class));
    }
    public void drawClicked(View view) {
        startActivity(new Intent(MainActivity.this, Draw.class));
    }

    public void drawingGameClicked(View view) {
        startActivity(new Intent(MainActivity.this, DrawingGame.class));
    }

    public void prizeClicked(View view) {
        startActivity(new Intent(MainActivity.this, Prize.class));
    }
}