package com.example.zakiva.santa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.zakiva.santa.Models.Images;

public class PrizeInformation extends AppCompatActivity {

    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_information);

        tabHost = (TabHost) findViewById(R.id.tabsBox);
        createTabs();
        //Set image from server
        Images.updateImage("add_text.jpeg", R.id.imageViewExample,PrizeInformation.this, "1");
    }

    public void createTabs(){
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("fashion");
        tabSpec.setContent(R.id.fashionTab);
        tabSpec.setIndicator("Fashion");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("food");
        tabSpec.setContent(R.id.foodTab);
        tabSpec.setIndicator("Food");
        tabHost.addTab(tabSpec);
    }
}
