package com.example.zakiva.santa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Score extends AppCompatActivity {

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        extras = getIntent().getExtras();
        if (extras != null) {
            ((TextView) findViewById(R.id.score)).setText("Your score is: " + extras.getInt("score"));
        }
    }
}
