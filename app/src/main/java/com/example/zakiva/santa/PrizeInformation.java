package com.example.zakiva.santa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zakiva.santa.Models.Images;

public class PrizeInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_information);

        //Set image from server
        Images.updateImage("add_text.jpeg", R.id.imageViewExample,PrizeInformation.this, "1");
    }
}
