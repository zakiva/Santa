package com.example.zakiva.santa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.Infra;
import com.example.zakiva.santa.Models.Images;
import com.squareup.picasso.Picasso;

public class PrizeInformation extends AppCompatActivity {

    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_information);

        tabHost = (TabHost) findViewById(R.id.tabsBox);
        createTabs();
        //Set image from server
        //Images.updateImage("add_text.jpeg", R.id.imageViewExample,PrizeInformation.this, "1");

        //Handle prize name
        TextView textViewPrizeNameA = (TextView) findViewById(R.id.giftNameTabA);
        TextView textViewPrizeNameB = (TextView) findViewById(R.id.giftNameTabB);

        textViewPrizeNameA.setText(Prize.prizeAName);
        textViewPrizeNameB.setText(Prize.prizeBName);

        //Handle company name
        TextView textViewCompanyA = (TextView) findViewById(R.id.companyNameTabA);
        TextView textViewCompanyB = (TextView) findViewById(R.id.companyNameTabB);

        textViewCompanyA.setText(Prize.prizeACompany);
        textViewCompanyB.setText(Prize.prizeBCompany);

        //Handle prize worth
        TextView textViewWorthA = (TextView) findViewById(R.id.moneyTabA);
        TextView textViewWorthB = (TextView) findViewById(R.id.moneyTabB);

        textViewWorthA.setText(Prize.prizeAWorth);
        textViewWorthB.setText(Prize.prizeBWorth);

        //Handle prize description
        TextView textViewDescriptionA = (TextView) findViewById(R.id.informationTabA);
        TextView textViewDescriptionB = (TextView) findViewById(R.id.informationTabB);

        textViewDescriptionA.setText(Prize.prizeADescription);
        textViewDescriptionB.setText(Prize.prizeBDescription);

        final ImageView imageViewA = (ImageView) findViewById(R.id.imageViewTabA);
        final ImageView imageViewB = (ImageView) findViewById(R.id.imageViewTabB);

        Picasso.with(this).load(Prize.prizeAImageUrl).into(imageViewA);
        Picasso.with(this).load(Prize.prizeBImageUrl).into(imageViewB);
    }

    public void createTabs(){
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab_a");
        tabSpec.setContent(R.id.tabA);
        tabSpec.setIndicator(Prize.prizeACategory);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab_b");
        tabSpec.setContent(R.id.tabB);
        tabSpec.setIndicator(Prize.prizeBCategory);
        tabHost.addTab(tabSpec);
    }

    public void backToPrizeClicked(View view) {
        startActivity(new Intent(PrizeInformation.this, Prize.class));
    }
}