package com.example.zakiva.santa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        displayExp();
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

    public void displayExp () {
        long exp = MainActivity.exp;
        if (extras != null && score != -1) {
            exp += score;
            if (exp > EXP_SIZE) {
                expSizeReached();
                exp %= EXP_SIZE;
            }
            Infra.addExpToUser(exp);
        }
        expTextView.setText("exp: " + exp);
        expBar.setProgress((int)exp);
    }

    public void expSizeReached () {
        //add more candies.. tell the user WOW..
    }
}
