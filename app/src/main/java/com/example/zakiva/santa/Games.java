package com.example.zakiva.santa;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zakiva.santa.Helpers.CustomPagerAdapter;
import com.example.zakiva.santa.Helpers.Infra;

public class Games extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
        viewPager.setClipToPadding(false);
        viewPager.setPadding(80,0,80,0);
        viewPager.setPageMargin(50);
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
