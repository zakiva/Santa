package com.example.zakiva.santa;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.Infra;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Prize extends AppCompatActivity {

    private static Button prizeA;
    private static Button prizeB;
    public static String prizeChosen;
    private static TextView stopper;
    private static CountDownTimer timer;
    public static long milisecondsToEndOfCompetition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize);
        initFields();
        colorPrizes();
        setCountDown();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        colorPrizes();
    }

    public static void setPrizeChosen (String p) {
        prizeChosen = p;
        if (prizeA != null)
            colorPrizes();
        Loader.increase();
    }

    public void initFields () {
        prizeA = (Button) findViewById(R.id.prizeA);
        prizeB = (Button) findViewById(R.id.prizeB);
        stopper = (TextView) findViewById(R.id.stopper);
    }

    public static void colorPrizes () {
        if (prizeChosen.equals("NONE")) {
            prizeB.setBackgroundColor(Color.GRAY);
            prizeA.setBackgroundColor(Color.GRAY);
            return;
        }
        if (prizeChosen.equals("a")) {
            prizeA.setBackgroundColor(Color.BLUE);
            prizeB.setBackgroundColor(Color.GRAY);
        }
        else {
            prizeB.setBackgroundColor(Color.BLUE);
            prizeA.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void prizeClicked(View view) {
        prizeChosen = view.getTag().toString();
        Infra.addPrizeToUser(prizeChosen);
        colorPrizes();
    }

    public void windisPrizeClicked(View view) {
        startActivity(new Intent(Prize.this, Games.class));
    }

    public static void setCountDown () {
        //we want to allow updating the count down from the timeCode setter
        //avoid crashing when calling setCountDown before first creation of this Activity
        if (stopper == null)
            return;
        //cancel the previous time if exists (only on second running and above)
        if (timer != null)
            timer.cancel();

        String timeCode = MainActivity.getTimeCode();

        Date endTime = null;
        try {
            endTime = new SimpleDateFormat("ddMMyyyyHHmmss").parse(timeCode);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long currentTimeMillis = System.currentTimeMillis();
        Timestamp stamp = new Timestamp(currentTimeMillis);
        Date currentTime = new Date(stamp.getTime());

        milisecondsToEndOfCompetition = (endTime.getTime()-currentTime.getTime());

        Log.d(MainActivity.TAG, ">>>>>>>MILLISECONDS " + milisecondsToEndOfCompetition);

        timer = new CountDownTimer(milisecondsToEndOfCompetition, 100) {

            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;

                String formattedTime = String.format("%02d:%02d:%02d", seconds / 3600,
                        (seconds % 3600) / 60, (seconds % 60));


                if (millisUntilFinished / 1000 == 0) {

                }

                stopper.setText(formattedTime);
            }

            public void onFinish() {
                //what happens when time is 00:00:00?
            }
        }.start();
    }
}
