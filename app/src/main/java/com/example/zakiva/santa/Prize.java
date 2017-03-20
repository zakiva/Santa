package com.example.zakiva.santa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zakiva.santa.Helpers.Infra;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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

    public static String prizeACategory = "";
    public static String prizeACompany = "";
    public static String prizeACompetitionNumber = "";
    public static String prizeADescription = "";
    public static String prizeAImageUrl = "";
    public static String prizeAName = "";
    public static String prizeANumber = "";
    public static String prizeAWorth = "";

    public static String prizeBCategory = "";
    public static String prizeBCompany = "";
    public static String prizeBCompetitionNumber = "";
    public static String prizeBDescription = "";
    public static String prizeBImageUrl = "";
    public static String prizeBName = "";
    public static String prizeBNumber = "";
    public static String prizeBWorth = "";

    public static int drawableAEnabled;
    public static int drawableADisabled;
    public static int drawableBEnabled;
    public static int drawableBDisabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize);
        initFields();
        updatePrizesNames();
        colorPrizes();
        setCountDown();

        // Set a toolbar to replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    public static void updatePrizesNames (){
        if (prizeALabel == null){
            return;
        }
        prizeALabel.setText(prizeAName);
        prizeBLabel.setText(prizeBName);
    }
    public static void colorPrizes () {
        if (prizeALabel == null){
            return;
        }
        if (prizeChosen.equals("NONE")) {
            prizeALabel.setTextColor(Color.parseColor("#aaaaaa"));
            prizeBLabel.setTextColor(Color.parseColor("#aaaaaa"));
            prizeAIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), drawableADisabled, null));
            prizeBIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), drawableBDisabled, null));
            ((GradientDrawable) prizeA.getBackground()).setStroke(1, Color.parseColor("#aaaaaa"));
            ((GradientDrawable) prizeB.getBackground()).setStroke(1, Color.parseColor("#aaaaaa"));
            return;
        }
        if (prizeChosen.equals("a")) {
            prizeALabel.setTextColor(Color.parseColor("#9254ff"));
            prizeBLabel.setTextColor(Color.parseColor("#aaaaaa"));
            prizeAIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), drawableAEnabled, null));
            prizeBIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), drawableBDisabled, null));
            ((GradientDrawable) prizeA.getBackground()).setStroke(1, Color.parseColor("#9254ff"));
            ((GradientDrawable) prizeB.getBackground()).setStroke(1, Color.parseColor("#aaaaaa"));
        }
        else {
            prizeALabel.setTextColor(Color.parseColor("#aaaaaa"));
            prizeBLabel.setTextColor(Color.parseColor("#9254ff"));
            prizeAIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), drawableADisabled, null));
            prizeBIcon.setBackground(ResourcesCompat.getDrawable(activity.getResources(), drawableBEnabled, null));
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
            Toast toast = Toast.makeText(this, "Please choose a prize first", Toast.LENGTH_SHORT);
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

    public void settingsClicked(View view) {
    }

    public void hallOfFameClicked(View view) {
        startActivity(new Intent(Prize.this, HallOfFame.class));
    }

    public static void updatePrizeInfoFields(Map<String,Map<String, String>> bothPrizes, Context context){

        Prize.prizeACategory = bothPrizes.get("a").get("category");
        Prize.prizeACompany = bothPrizes.get("a").get("company");
        Prize.prizeACompetitionNumber = bothPrizes.get("a").get("competitionNumber");
        Prize.prizeADescription = bothPrizes.get("a").get("description");
        Prize.prizeAImageUrl = bothPrizes.get("a").get("imageUrl");
        Prize.prizeAName = bothPrizes.get("a").get("prizeName");
        Prize.prizeANumber = bothPrizes.get("a").get("prizeNumber");
        Prize.prizeAWorth = bothPrizes.get("a").get("worth");

        Prize.prizeBCategory = bothPrizes.get("b").get("category");
        Prize.prizeBCompany = bothPrizes.get("b").get("company");
        Prize.prizeBCompetitionNumber = bothPrizes.get("b").get("competitionNumber");
        Prize.prizeBDescription = bothPrizes.get("b").get("description");
        Prize.prizeBImageUrl = bothPrizes.get("b").get("imageUrl");
        Prize.prizeBName = bothPrizes.get("b").get("prizeName");
        Prize.prizeBNumber = bothPrizes.get("b").get("prizeNumber");
        Prize.prizeBWorth = bothPrizes.get("b").get("worth");

        Picasso.with(context).load(Prize.prizeAImageUrl).fetch();
        Picasso.with(context).load(Prize.prizeBImageUrl).fetch();
    }

    public static void updatePrizeIcons(){

        switch (Prize.prizeACategory) {
            case "Culinary":
                Prize.drawableAEnabled = R.drawable.culinary_enable;
                Prize.drawableADisabled = R.drawable.culinary_disable;
                break;
            case "Lifestyle":
                Prize.drawableAEnabled = R.drawable.lifrstyle_enable;
                Prize.drawableADisabled = R.drawable.lifrstyle_disable;
                break;
            case "Fashion":
                Prize.drawableAEnabled = R.drawable.fashion_enable;
                Prize.drawableADisabled = R.drawable.fashion_disable;
                break;
            case "Gadgets":
                Prize.drawableAEnabled = R.drawable.gadget_enable;
                Prize.drawableADisabled = R.drawable.gadget_disable;
                break;
            case "Beauty Products":
                Prize.drawableAEnabled = R.drawable.beauty_enable;
                Prize.drawableADisabled = R.drawable.beauty_disable;
                break;
            case "Cash":
                Prize.drawableAEnabled = R.drawable.cash_enable;
                Prize.drawableADisabled = R.drawable.cash_disable;
                break;
            case "Baby Products":
                Prize.drawableAEnabled = R.drawable.babies_enable;
                Prize.drawableADisabled = R.drawable.babies_disable;
                break;
            case "Sport":
                Prize.drawableAEnabled = R.drawable.sport_enable;
                Prize.drawableADisabled = R.drawable.sport_disable;
                break;
            case "Tourism":
                Prize.drawableAEnabled = R.drawable.travel_enable;
                Prize.drawableADisabled = R.drawable.travel_disable;
                break;
            case "Pet Products":
                Prize.drawableAEnabled = R.drawable.animal_enable;
                Prize.drawableADisabled = R.drawable.animal_disable;
                break;
            case "Shopping":
                Prize.drawableAEnabled = R.drawable.shopping_enable;
                Prize.drawableADisabled = R.drawable.shopping_disable;
                break;
            default:
                break;
        }

        switch (Prize.prizeBCategory) {
            case "Culinary":
                Prize.drawableBEnabled = R.drawable.culinary_enable;
                Prize.drawableBDisabled = R.drawable.culinary_disable;
                break;
            case "Lifestyle":
                Prize.drawableBEnabled = R.drawable.lifrstyle_enable;
                Prize.drawableBDisabled = R.drawable.lifrstyle_disable;
                break;
            case "Fashion":
                Prize.drawableBEnabled = R.drawable.fashion_enable;
                Prize.drawableBDisabled = R.drawable.fashion_disable;
                break;
            case "Gadgets":
                Prize.drawableBEnabled = R.drawable.gadget_enable;
                Prize.drawableBDisabled = R.drawable.gadget_disable;
                break;
            case "Beauty Products":
                Prize.drawableBEnabled = R.drawable.beauty_enable;
                Prize.drawableBDisabled = R.drawable.beauty_disable;
                break;
            case "Cash":
                Prize.drawableBEnabled = R.drawable.cash_enable;
                Prize.drawableBDisabled = R.drawable.cash_disable;
                break;
            case "Baby Products":
                Prize.drawableBEnabled = R.drawable.babies_enable;
                Prize.drawableBDisabled = R.drawable.babies_disable;
                break;
            case "Sport":
                Prize.drawableBEnabled = R.drawable.sport_enable;
                Prize.drawableBDisabled = R.drawable.sport_disable;
                break;
            case "Tourism":
                Prize.drawableBEnabled = R.drawable.travel_enable;
                Prize.drawableBDisabled = R.drawable.travel_disable;
                break;
            case "Pet Products":
                Prize.drawableBEnabled = R.drawable.animal_enable;
                Prize.drawableBDisabled = R.drawable.animal_disable;
                break;
            case "Shopping":
                Prize.drawableBEnabled = R.drawable.shopping_enable;
                Prize.drawableBDisabled = R.drawable.shopping_disable;
                break;
            default:
                break;
        }
    }
}