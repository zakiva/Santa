package com.example.zakiva.santa.Helpers;

import android.util.Log;

import com.example.zakiva.santa.Models.Competition;
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

    public static void addTriviaQuestion (String key, String q, String ca, String a, String b, String c, String d) {
        TriviaQuestion triviaQuestion = new TriviaQuestion(key, q, ca, a, b, c, d);
        myDatabase.child("triviaQuestions").child(key).setValue(triviaQuestion);
    }

    public static void addSheet (String name, ArrayList<HashMap<String, Object>> data) {
        //Sheet sheet = new Sheet(name, data);
        myDatabase.child("triviaDataSheets").child(name).setValue(data);
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

    public static void getTriviaDataFromFirebase(final String sheet) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("triviaDataSheets/" + sheet);
        ValueEventListener triviaDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Log.d(TAG, "snapppppppppppppp");

               // GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t = new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};

               // ArrayList<HashMap<String, Object>> lst = dataSnapshot.getValue(t);

                ArrayList<HashMap<String, Object>> lst = (ArrayList<HashMap<String, Object>>) dataSnapshot.getValue();


                //Sheet s = dataSnapshot.getValue(GenericTypeIndicator);
               // Log.d(TAG, "s is ourssssssssss");

               // ArrayList<HashMap<String,Object>> lst = s.getData();
             //   Log.d(TAG, "We got the sheet: " + s.getName());
                Trivia.addSheetToDataHash(sheet, lst);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "displayQuestion:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(triviaDataListener);
    }
}
