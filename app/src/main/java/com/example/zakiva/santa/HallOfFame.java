package com.example.zakiva.santa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.zakiva.santa.Helpers.HallOfFameAdapter;

import java.util.ArrayList;

public class HallOfFame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);

        loadWinnersList();
    }

    public void loadWinnersList () {
        ArrayList<String[]> items = new ArrayList<String[]>();
        for (int i = 0; i < 20; i++) {
            String[] item = new String[1];
            item[0] = "winner#" + i;
            items.add(item);
        }
        ListAdapter hallOfFameAdapter = new HallOfFameAdapter(getBaseContext(), items);
        ListView winnersListView = (ListView) findViewById(R.id.winnersList);
        winnersListView.setAdapter(hallOfFameAdapter);
    }

    public void backToPrizeClicked(View view) {
        startActivity(new Intent(HallOfFame.this, Prize.class));
    }
}
