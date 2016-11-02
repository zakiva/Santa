package com.example.zakiva.santa;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zakiva.santa.Helpers.Infra;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Prize extends AppCompatActivity {

    private static RelativeLayout prizeA;
    private static RelativeLayout prizeB;
    private static ImageView prizeAIcon;
    private static ImageView prizeBIcon;
    private static TextView prizeALabel;
    private static TextView prizeBLabel;
    public static String prizeChosen;
    private static TextView stopper;
    private static CountDownTimer timer;
    public static long milisecondsToEndOfCompetition;
    private static Activity activity;

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
        activity = this;
        prizeA = (RelativeLayout) findViewById(R.id.prizeA);
        prizeB = (RelativeLayout) findViewById(R.id.prizeB);
        prizeAIcon = (ImageView) findViewById(R.id.prizeAIcon);
        prizeBIcon = (ImageView) findViewById(R.id.prizeBIcon);
        prizeALabel = (TextView) findViewById(R.id.prizeALabel);
        prizeBLabel = (TextView) findViewById(R.id.prizeBLabel);
        stopper = (TextView) findViewById(R.id.stopper);
    }

    public static void colorPrizes () {
        if (prizeChosen.equals("NONE")) {
            prizeALabel.setTextColor(Color.parseColor("#aaaaaa"));
            prizeBLabel.setTextColor(Color.parseColor("#aaaaaa"));
            prizeAIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.culinary_disable, null));
            prizeBIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gadget_disable, null));
            ((GradientDrawable) prizeA.getBackground()).setStroke(1, Color.parseColor("#aaaaaa"));
            ((GradientDrawable) prizeB.getBackground()).setStroke(1, Color.parseColor("#aaaaaa"));
            return;
        }
        if (prizeChosen.equals("a")) {
            prizeALabel.setTextColor(Color.parseColor("#9254ff"));
            prizeBLabel.setTextColor(Color.parseColor("#aaaaaa"));
            prizeAIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.culinary_enable, null));
            prizeBIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gadget_disable, null));
            ((GradientDrawable) prizeA.getBackground()).setStroke(1, Color.parseColor("#9254ff"));
            ((GradientDrawable) prizeB.getBackground()).setStroke(1, Color.parseColor("#aaaaaa"));
        }
        else {
            prizeALabel.setTextColor(Color.parseColor("#aaaaaa"));
            prizeBLabel.setTextColor(Color.parseColor("#9254ff"));
            prizeAIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.culinary_disable, null));
            prizeBIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gadget_enable, null));
            ((GradientDrawable) prizeA.getBackground()).setStroke(1, Color.parseColor("#aaaaaa"));
            ((GradientDrawable) prizeB.getBackground()).setStroke(1, Color.parseColor("#9254ff"));
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

        if (prizeChosen.equals("NONE")) {
            Toast toast = Toast.makeText(this, "First choose a prize mateee!!!", Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.WHITE);
            toast.show();
        }
        else {
            startActivity(new Intent(Prize.this, Games.class));
        }
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

    public void seePrizeInformationClicked(View view) {
        startActivity(new Intent(Prize.this, PrizeInformation.class));
    }
}
