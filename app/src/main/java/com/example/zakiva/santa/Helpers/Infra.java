package com.example.zakiva.santa.Helpers;

import android.util.Log;

import com.example.zakiva.santa.Models.Competition;
import com.example.zakiva.santa.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by zakiva on 8/27/16.
 */

public class Infra {

    public static final String TAG = ">>>>>>>Debug: ";
    public static DatabaseReference myDatabase;
    public static String userEmail;

    public static void initInfra (String email) {
        myDatabase = FirebaseDatabase.getInstance().getReference();
        userEmail = email;
    }

    public static void addUser () {
        User user = new User(userEmail);
        myDatabase.child("users").child(userEmail).child("userObject").setValue(user);
    }

    public static void addCompetition (String key, String giftId) {
        Competition competition = new Competition(key, giftId);
        myDatabase.child("users").child(userEmail).child("competitions").child(key).setValue(competition);
    }

    //get a user object from the database
    public static void getUser () {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + userEmail +"/userObject");
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                Log.d(TAG, "We got the user: " + u.getEmail());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(userListener);
    }

    //get a competition object from the database
    public static void getCompetition (String key) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + userEmail +"/competitions/" + key);
        ValueEventListener competitionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Competition c = dataSnapshot.getValue(Competition.class);
                Log.d(TAG, "We got the competition: " + c.getGiftId());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getCompetition:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(competitionListener);
    }
}
