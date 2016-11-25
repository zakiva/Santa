package com.example.zakiva.santa;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.zakiva.santa.Helpers.HallOfFameAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HallOfFame extends AppCompatActivity {

    public static ArrayList<Object> dataHashWinners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);

        loadWinnersList();

        //HashMap<String, String> hm = (HashMap) dataHashWinners.get(1);
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

    public void loadWinnersList () {
        int size = dataHashWinners.size();
        ArrayList<String[]> items = new ArrayList<String[]>();

        for (int i = size - 1; i >= 0; i--) {
            String[] item = new String[6];

            item[0] = (String) ((HashMap) dataHashWinners.get(i)).get("competition");
            item[1] = (String) ((HashMap) dataHashWinners.get(i)).get("details");;
            item[2] = (String) ((HashMap) dataHashWinners.get(i)).get("imageName");;
            item[3] = (String) ((HashMap) dataHashWinners.get(i)).get("imageUrl");;
            item[4] = (String) ((HashMap) dataHashWinners.get(i)).get("name");;
            item[5] = (String) ((HashMap) dataHashWinners.get(i)).get("prize");;

            items.add(item);
        }
        ListAdapter hallOfFameAdapter = new HallOfFameAdapter(getBaseContext(), items);
        ListView winnersListView = (ListView) findViewById(R.id.winnersList);
        winnersListView.setAdapter(hallOfFameAdapter);
    }

    public void updateWinnerList(){
        HallOfFame.dataHashWinners = new ArrayList<>();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("winners");
        ValueEventListener winnersDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HallOfFame.dataHashWinners = (ArrayList<Object>) dataSnapshot.getValue();
                loadWinnersList();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(winnersDataListener);
    }

    public void backToPrizeClicked(View view) {
        startActivity(new Intent(HallOfFame.this, Prize.class));
    }
}
