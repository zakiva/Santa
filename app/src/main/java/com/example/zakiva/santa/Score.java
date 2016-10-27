package com.example.zakiva.santa;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.Infra;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initFields();
        if (score != -1)
            Infra.addGameToUser(gameType, score);
        displayScore();
        displayCandies();
        calcAndDisplayExp();
    }

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
        extras = getIntent().getExtras();
        scoreTextView = ((TextView) findViewById(R.id.score));
        candiesTextView = ((TextView) findViewById(R.id.candies));
        expTextView = ((TextView) findViewById(R.id.exp));
        expBar = ((ProgressBar) findViewById(R.id.expBar));
        score = extras.getLong("score");
        gameType = extras.getString("game");
    }

    public void displayCandies () {
        candiesTextView.setText(MainActivity.candies + " candies");
    }

    public void displayScore () {
        if (extras != null) {
            if (score != -1) {
                ((TextView) findViewById(R.id.score)).setText("Your score is: " + score);
                 scoreTextView.setVisibility(View.VISIBLE);
            }
            else {
                scoreTextView.setVisibility(View.GONE);
            }
        }
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




    public void calcAndDisplayExp () {
        long exp = MainActivity.exp;
        mProgressStatus = exp;
        if (extras != null && score != -1) {
            exp += score;
            if (exp > EXP_SIZE) {
                exp %= EXP_SIZE;
                Infra.addExpToUser(exp);
                expSizeReached(exp);
            }
            else {
                Infra.addExpToUser(exp);
                showProgressAnimation(exp);
            }
        }
        else {
            expBar.setProgress((int)exp);
            expTextView.setText("" + exp);
        }
    }

    void showProgressAnimation (final long newExp) {

        final Handler mHandler = new Handler();

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                double delta = 0.01;
                while (mProgressStatus < newExp) {

                    //set "frames rate"
                    mProgressStatus += delta;
                    delta += 0.000003;

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
                            expTextView.setText("" + (int) mProgressStatus);
                        }
                    });
                }
            }
        }).start();
    }

    public void expSizeReached (long newExp) {
        //add more candies.. tell the user WOW..
        //show complete animation before new progress animation

        mProgressStatus = 0;
        showProgressAnimation(newExp);
    }
}



/*





public class MyActivity extends Activity {
    private static final int PROGRESS = 0x1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.progressbar_activity);

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);

        Handler mHandler = new Handler();

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus = doWork();

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        }).start();
    }
}

*/
