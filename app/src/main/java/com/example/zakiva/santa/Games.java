package com.example.zakiva.santa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zakiva.santa.Helpers.CustomPagerAdapter;
import com.example.zakiva.santa.Helpers.Drawing;
import com.example.zakiva.santa.Helpers.Infra;

public class Games extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        setViewPager();
    }

    public void setViewPager () {

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin((int) Drawing.convertDpToPixel(20, this));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                colorPageIndicator(position);
            }
        });
    }

    public void colorPageIndicator (int position) {
        (findViewById(R.id.indicatorGame1)).setBackgroundResource(R.drawable.indecator_disable);
        (findViewById(R.id.indicatorGame2)).setBackgroundResource(R.drawable.indecator_disable);
        (findViewById(R.id.indicatorGame3)).setBackgroundResource(R.drawable.indecator_disable);
        (findViewById(R.id.indicatorGame4)).setBackgroundResource(R.drawable.indecator_disable);
        (findViewById(R.id.indicatorGame5)).setBackgroundResource(R.drawable.indecator_disable);

        switch (position) {
            case 0:
                (findViewById(R.id.indicatorGame1)).setBackgroundResource(R.drawable.indecator_enable);
                break;
            case 1:
                (findViewById(R.id.indicatorGame2)).setBackgroundResource(R.drawable.indecator_enable);
                break;
            case 2:
                (findViewById(R.id.indicatorGame3)).setBackgroundResource(R.drawable.indecator_enable);
                break;
            case 3:
                (findViewById(R.id.indicatorGame4)).setBackgroundResource(R.drawable.indecator_enable);
                break;
            case 4:
                (findViewById(R.id.indicatorGame5)).setBackgroundResource(R.drawable.indecator_enable);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Games.this, Prize.class));
    }

    public void gameClicked(View view) {
        Intent intent = new Intent(Games.this, Score.class);
        String tag = view.getTag().toString();
        if (tag.equals("")) { // for coming soon games
            Toast toast = Toast.makeText(this, "We can't wait to play it too!!", Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.WHITE);
            toast.show();
            return;
        }
        intent.putExtra("game", tag);
        intent.putExtra("score", (long) -1);
        startActivity(intent);
    }
}
