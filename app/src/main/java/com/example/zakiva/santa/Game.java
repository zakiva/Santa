package com.example.zakiva.santa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.zakiva.santa.Helpers.Drawing;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }




    public void gameClicked(View view) {

        switch (view.getTag().toString()) {
            case "trivia":
                startActivity(new Intent(Game.this, Trivia.class));
                break;
            case "drawing":
                startActivity(new Intent(Game.this, DrawingGame.class));
                break;
            default:
                return;
        }
    }
}
