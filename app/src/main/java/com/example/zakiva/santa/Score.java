package com.example.zakiva.santa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.Infra;

public class Score extends AppCompatActivity {

    Bundle extras;
    TextView scoreTextView;
    long score;
    String gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initFields();
        if (score != -1)
            Infra.addGameToUser(gameType, score);
        displayScore();
    }

    public void initFields () {
        extras = getIntent().getExtras();
        scoreTextView = ((TextView) findViewById(R.id.score));
        score = extras.getLong("score");
        gameType = extras.getString("game");
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

        switch (gameType) {
            case "trivia":
                startActivity(new Intent(Score.this, Trivia.class));
                break;
            case "drawing":
                startActivity(new Intent(Score.this, DrawingGame.class));
                break;
            default:
                return;
        }
    }
}
