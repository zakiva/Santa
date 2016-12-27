package com.example.zakiva.santa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.zakiva.santa.Helpers.HallOfFameAdapter;
import com.example.zakiva.santa.Helpers.Infra;
import com.example.zakiva.santa.Models.Winner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class HallOfFame extends AppCompatActivity {

    public static HashMap<String, HashMap> dataHashWinners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);

        loadWinnersList();

        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadWinnersList();
                    }
                }
        );
    }

    public void loadWinnersList () {
        int size = dataHashWinners.size();
        ArrayList<String[]> items = new ArrayList<String[]>();

        for (HashMap hm : dataHashWinners.values()) {
            String[] item = new String[7];
            item[0] = (String) hm.get("competition");
            item[1] = (String) hm.get("details");
            item[2] = (String) hm.get("imageName");
            item[3] = (String) hm.get("imageUrl");
            item[4] = (String) hm.get("name");
            item[5] = (String) hm.get("prize");
            item[6] = (String) hm.get("minusKey").toString();
            //Log.d("aaaaaaa: ", item[6]);
            items.add(item);
        }
        Collections.sort(items, new java.util.Comparator<String[]>() {
            public int compare(final String[] entry1, final String[] entry2) {
                return (Integer.parseInt(entry1[6]) - Integer.parseInt(entry2[6]));
            }
        });
        ListAdapter hallOfFameAdapter = new HallOfFameAdapter(getBaseContext(), items);
        ListView winnersListView = (ListView) findViewById(R.id.winnersList);
        winnersListView.setAdapter(hallOfFameAdapter);

        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void backToPrizeClicked(View view) {
        startActivity(new Intent(HallOfFame.this, Prize.class));
    }

    //This loop helps generating Objects on firebase. Do not use it with more than 100 iterations
    public static void addWinnersForTesting(Activity myActivity){
        for (int i = 0; i < 40; i++ ){
            String key = Integer.toString(i);
            String imageName = key + ".jpg";
            Infra.addWinner(key, "Person " + key, key, "blah blah", imageName, "Prize " + key, myActivity);
        }
        //Infra.addWinner("0", "Lionel Messi", "12", "blah blah", "0.jpg", "Canon Camera");
        //Infra.addWinner("1", "Claudio Marchisio", "16", "blah blah", "1.jpg", "Sony Playstation 4");
        //Infra.addWinner("2", "Gigi Buffon", "17", "blah blah", "2.jpg", "Apple Iphone 7");
        //Infra.addWinner("3", "Leonardo Bonucci", "23", "blah blah", "3.jpg", "Samsung Galaxy S7");
        //Infra.addWinner("4", "Mario Mandjukic", "30", "blah blah", "4.jpg", "Trip to Barcelona");
        //Infra.addWinner("5", "Sami Khadeira", "32", "blah blah", "5.jpg", "Lenovo Yoga 2 Pro");
        //Infra.addWinner("6", "Neymar", "35", "blah blah", "6.jpg", "Mazda 3");
        //Infra.addWinner("7", "Xavi", "38", "blah blah", "7.jpg", "Samsung Smart TV");
        //Infra.addWinner("8", "Pique", "40", "blah blah", "8.jpg", "Raven Sunglasses");
        //Infra.addWinner("9", "Javier Mascherano", "56", "blah blah", "9.jpg", "Spaceship");
    }
}
