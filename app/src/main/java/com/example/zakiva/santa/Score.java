package com.example.zakiva.santa;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.Drawing;
import com.example.zakiva.santa.Helpers.Infra;
//import com.vungle.publisher.VunglePub;

public class Score extends AppCompatActivity {

    static int TRY_CANDIES_PRICE = 100;
    Bundle extras;
    TextView scoreTextView;
    TextView candiesTextView;
    TextView expTextView;
    ProgressBar expBar;
    long score;
    String gameType;
    double mProgressStatus = 0;
    long EXP_SIZE = 2000;
    Activity activity;
    ImageView mainIcon;
    TextView scoreTitle;
    RelativeLayout messageBox;
    Button playButton;
    TextView shareScore;

    //final VunglePub vunglePub = VunglePub.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initFields();
        displayScreenAccordingToScore();
        if (score != -1)
            Infra.addGameToUser(gameType, score);
        displayCandies();
        calcAndDisplayExp();
    }

    public void displayScreenAccordingToScore () {
        if (score != -1) {
            //set main icon to score section
            mainIcon.setBackground(ResourcesCompat.getDrawable(this.getResources(), R.drawable.score_section, null));

            //show score labels
            scoreTextView.setText("Your score is: " + score);
            scoreTextView.setVisibility(View.VISIBLE);
            scoreTitle.setVisibility(View.VISIBLE);

            //set message box below score
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins((int) Drawing.convertDpToPixel(15, this), (int) Drawing.convertDpToPixel(20, this), (int) Drawing.convertDpToPixel(15, this), (int) Drawing.convertDpToPixel(0, this));
            p.addRule(RelativeLayout.BELOW, R.id.score);
            messageBox.setLayoutParams(p);

            //set text of CTA to retry
            playButton.setText("Retry! (100 candis)");

            //show share score
            shareScore.setVisibility(View.VISIBLE);
        }
        else {

            //don't show score labels
            scoreTextView.setVisibility(View.GONE);
            scoreTitle.setVisibility(View.GONE);

            //set main icon according to game type
            switch (gameType) {
                case "trivia":
                    mainIcon.setBackground(ResourcesCompat.getDrawable(this.getResources(), R.drawable.trivia_section, null));
                    break;
                case "drawing":
                    mainIcon.setBackground(ResourcesCompat.getDrawable(this.getResources(), R.drawable.drawdis_section, null));
                    break;
                default:
                    return;
            }
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
    }*/

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Score.this, Games.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        displayCandies();
        //displayExp();
    }

    public void initFields () {
        activity = this;
        extras = getIntent().getExtras();
        scoreTextView = ((TextView) findViewById(R.id.score));
        candiesTextView = ((TextView) findViewById(R.id.candies));
        expTextView = ((TextView) findViewById(R.id.exp));
        expBar = ((ProgressBar) findViewById(R.id.expBar));
        mainIcon = ((ImageView) findViewById(R.id.mainIconScoreSection));;
        scoreTitle = ((TextView) findViewById(R.id.scoreTitle));;
        score = extras.getLong("score");
        gameType = extras.getString("game");
        messageBox = ((RelativeLayout) findViewById(R.id.messageRectangle));
        playButton = ((Button) findViewById(R.id.playButton));
        shareScore = ((TextView) findViewById(R.id.shareScoreOnBar));
    }

    public void displayCandies () {
        candiesTextView.setText(MainActivity.candies + " candies");
    }


    public void playButtonClicked(View v) {
        if (extras == null)
            return;

        if (!updateCandies()) // don't start game (and inform the user need more candies?)
            return;

        switch (gameType) {
            case "trivia":
                startActivity(new Intent(Score.this, TriviaGame.class));
                break;
            case "drawing":
                startActivity(new Intent(Score.this, DrawingGame.class));
                break;
            default:
                return;
        }
    }

    public boolean updateCandies () {
        if (MainActivity.candies < TRY_CANDIES_PRICE)
            return false;
        long new_candies = MainActivity.candies - TRY_CANDIES_PRICE;
        Infra.addCandiesToUser(new_candies);
        return true;
    }

    public void homeScreenButtonClicked(View view) {
        startActivity(new Intent(Score.this, Prize.class));
    }


    public void videoAdButtonClicked(View view) {
        /*if (vunglePub.isAdPlayable()) {
            vunglePub.playAd();
        }
        else {
            // Ad is not ready
            Log.d("Oops: ", "Can't load video!");
        }*/
    }


    public void calcAndDisplayExp () {
        long exp = MainActivity.exp;
        mProgressStatus = exp;
        expBar.setProgress((int)exp);
        expTextView.setText("" + exp);
        if (extras != null && score != -1) {
            exp += score;
            if (exp > EXP_SIZE) {
                exp %= EXP_SIZE;
                Infra.addExpToUser(exp);
                expSizeReached(exp);
            }
            else {
                Infra.addExpToUser(exp);
                showProgressAnimation(exp, false, 0);
            }
        }
        else {
            //its the first time we are here (not retry). init views anyway above in this method
        }
    }

    void showProgressAnimation (final long newExp, final boolean expReached, final long expAfterSize) {

        Log.d(MainActivity.TAG, " showProgressAnimation  -- newEXP , exp reached, expAfterSize " + newExp +"," + expReached + "," + expAfterSize);

        final Handler mHandler = new Handler();

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                double delta = 0.05;
                while (mProgressStatus < newExp) {

                    //set "frames rate"
                    mProgressStatus += delta;
                    //we can play with this too:
                    //delta += 0.000003;

                    //another option instead of delta
                    /*
                    if (newExp - mProgressStatus < 100)
                        mProgressStatus += 0.08;
                    else
                        mProgressStatus += 0.04;
                        */
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            expBar.setProgress((int) mProgressStatus);
                        }
                    });
                }


                Log.d(MainActivity.TAG, "if (expReached): ");

                // have to run this on the main thread, not in the background
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        if (expReached) {
                            showAlertDialogForExpSizeReached(expAfterSize);
                        }
                        else {
                            expTextView.setText("" + (int) mProgressStatus);
                        }
                    }
                });

            }
        }).start();
    }

    public void expSizeReached (long newExp) {
        //add more candies.. tell the user WOW..
        //show complete animation before new progress animation
        Log.d(MainActivity.TAG, "expSizeReached: ");
        showProgressAnimation(EXP_SIZE, true, newExp);
    }

    //make this function generic by passing it arguments (and move to INFRA)
    public void showAlertDialogForExpSizeReached (final long newExp) {

        Log.d(MainActivity.TAG, "showAlertDialogForExpSizeReached ");

        AlertDialog alertDialog = new AlertDialog.Builder(Score.this).create();
        alertDialog.setTitle("congratrs!!");
        alertDialog.setMessage("you've reached the EXP SIZE");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mProgressStatus = 0;
                        showProgressAnimation(newExp, false, 0);
                    }
                });
        alertDialog.show();
    }

    public void shareScoreButtonClicked(View view) {
    }
}
