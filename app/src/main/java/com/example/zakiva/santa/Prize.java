package com.example.zakiva.santa;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zakiva.santa.Helpers.Infra;

public class Prize extends AppCompatActivity {

    private static Button prizeA;
    private static Button prizeB;
    public static String prizeChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize);
        initFields();
        colorPrizes();
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
}
