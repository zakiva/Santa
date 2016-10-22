package com.example.zakiva.santa.Helpers;

import android.util.Log;

import com.example.zakiva.santa.Models.Competition;
import com.example.zakiva.santa.Models.Game;
import com.example.zakiva.santa.Models.TriviaQuestion;
import com.example.zakiva.santa.Models.User;
import com.example.zakiva.santa.Trivia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.example.zakiva.santa.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zakiva on 8/27/16.
 */

public class Infra {

    public static final String TAG = ">>>>>>>Debug: ";
    public static DatabaseReference myDatabase;
    public static String userEmail;
    public static String timeCode;
    public static String [] triviaSheets = {"inventions", "countries", "bands", "singers", "timeZones","worldCups","championships","latitudes","authors","israelEvents"};


    public static void initInfra (String email, String time) {
        myDatabase = FirebaseDatabase.getInstance().getReference();
        userEmail = email;
        timeCode = time;
    }

    public static void addUser () {
        User user = new User(userEmail);
        myDatabase.child("users").child(userEmail).child("userObject").setValue(user);
    }

    /*
    public static void addCompetition (String key, String giftId) {
        Competition competition = new Competition(key, giftId);
        myDatabase.child("users").child(userEmail).child("competitions").child(key).setValue(competition);
    }
    */

    public static void addTriviaQuestion (String key, String q, String ca, String a, String b, String c, String d) {
        TriviaQuestion triviaQuestion = new TriviaQuestion(key, q, ca, a, b, c, d);
        myDatabase.child("triviaQuestions").child(key).setValue(triviaQuestion);
    }

    public static void addSheet (String name, ArrayList<HashMap<String, Object>> data) {
        //Sheet sheet = new Sheet(name, data);
        myDatabase.child("triviaDataSheets").child(name).setValue(data);
    }

    public static void addPrizeToUser (String prize) {
        myDatabase.child("users").child(userEmail).child("competitions").child(timeCode).child("prize").setValue(prize);
    }

    public static void addGameToUser (String type, long score) {
        Game game = new Game (type, score);
        myDatabase.child("users").child(userEmail).child("competitions").child(timeCode).child("games").push().setValue(game);
    }

    public static void setUserCandies (long candies) {
        myDatabase.child("users").child(userEmail).child("competitions").child(timeCode).child("candies").setValue(candies);
    }

    public static void initUserCandies (final long candies) {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + userEmail + "/competitions/" + timeCode + "/candies");
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    setUserCandies(candies);
                    MainActivity.setCandies(candies);
                }
                else {
                    long candies = (long) dataSnapshot.getValue();
                    MainActivity.setCandies(candies);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(userListener);
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

    //get a timestamp code from the database
    public static void getTimeCodeFromServer () {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("timeCode");
        ValueEventListener timeCodeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.setTimeCode((String) dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getTimeCode:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(timeCodeListener);
    }

    //get a question object from the database and display it on the screen
    public static void displayTriviaQuestion (String key) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("triviaQuestions/" + key);
        ValueEventListener triviaQuestionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TriviaQuestion q = dataSnapshot.getValue(TriviaQuestion.class);
                Log.d(TAG, "We got the question: " + q.getQuestion());
                Trivia.loadQuestionToScreen(q);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "displayQuestion:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(triviaQuestionListener);
    }

    public static void getTriviaDataFromFirebase() {
        //must initialize data hash before puttint data inside
        TriviaGame.dataHash = new HashMap<>();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("triviaDataSheets");
        ValueEventListener triviaDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Log.d(TAG, "snapppppppppppppp");

               // GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t = new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};

               // ArrayList<HashMap<String, Object>> lst = dataSnapshot.getValue(t);

                Map <String, Object> data = (Map) dataSnapshot.getValue();

                for (String sheet : triviaSheets) {
                    ArrayList<HashMap<String, Object>> lst = (ArrayList<HashMap<String, Object>>) data.get(sheet);
                    TriviaGame.addSheetToDataHash(sheet, lst);
                }


                //Sheet s = dataSnapshot.getValue(GenericTypeIndicator);
               // Log.d(TAG, "s is ourssssssssss");

               // ArrayList<HashMap<String,Object>> lst = s.getData();
             //   Log.d(TAG, "We got the sheet: " + s.getName());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "displayQuestion:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(triviaDataListener);
    }
}
