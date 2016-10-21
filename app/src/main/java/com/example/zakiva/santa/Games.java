package com.example.zakiva.santa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Games extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Games.this, Prize.class));
    }

    public void gameClicked(View view) {
        Intent intent = new Intent(Games.this, Score.class);
        intent.putExtra("game", view.getTag().toString());
        intent.putExtra("score", (long) -1);
        startActivity(intent);
    }
}
