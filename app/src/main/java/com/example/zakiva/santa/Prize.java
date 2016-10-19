package com.example.zakiva.santa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.Infra;

public class Prize extends AppCompatActivity {

    private Button prizeA;
    private Button prizeB;
    private String prizeChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize);
        prizeA = (Button) findViewById(R.id.prizeA);
        prizeB = (Button) findViewById(R.id.prizeB);
    }

    public void prizeClicked(View view) {
        prizeChosen = view.getTag().toString();
        Infra.addPrizeToUser(prizeChosen);
    }

    public void windisPrizeClicked(View view) {
        startActivity(new Intent(Prize.this, Game.class));
    }
}
