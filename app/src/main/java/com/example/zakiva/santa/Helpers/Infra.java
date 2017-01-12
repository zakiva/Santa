package com.example.zakiva.santa.Helpers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zakiva.santa.Models.Competition;
import com.example.zakiva.santa.Models.Game;
import com.example.zakiva.santa.Models.MainDrawingView;
import com.example.zakiva.santa.Models.Token;
import com.example.zakiva.santa.Models.TriviaQuestion;
import com.example.zakiva.santa.Models.User;
import com.example.zakiva.santa.Models.Winner;
import com.example.zakiva.santa.Trivia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.example.zakiva.santa.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
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
    public static String [] triviaSheets = {"inventions","countries","israelBands","worldBands","singers", "timeZones","worldCups","championships","latitudes","authors","israelEvents","bibleFathers","brands","femaleActors","leadersYears","maleActors","mountains","quotes","wifeHusband","worldLeaders","apps","cars"};
    public static int HallOfFameListLimit = 40;
    public static int HallOfFamePreDownloadLimit = 20;

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

    public static void addTriviaQuestion (String key, String q, String ca, String a, String b, String c, String d,boolean m) {
        TriviaQuestion triviaQuestion = new TriviaQuestion(key, q, ca, a, b, c, d,m);
        myDatabase.child("triviaQuestions").child(key).setValue(triviaQuestion);
    }

    public static void addSheet (String name, ArrayList<HashMap<String, Object>> data) {
        //Sheet sheet = new Sheet(name, data);
        myDatabase.child("triviaDataSheets").child(name).setValue(data);
    }

    public static void addPrizeToUser (String prize) {
        Log.d(TAG, "addPrizeToUser  We got the prize: " + prize);
        myDatabase.child("users").child(userEmail).child("competitions").child(timeCode).child("prize").setValue(prize);
        Log.d(TAG, "addPrizeToUser  saved prize prize: " + prize);

    }

    public static void addGameToUser (String type, long score) {
        Game game = new Game (type, score);
        myDatabase.child("users").child(userEmail).child("competitions").child(timeCode).child("games").push().setValue(game);
    }

    public static void addCandiesToUser (long candies) {
        myDatabase.child("users").child(userEmail).child("competitions").child(timeCode).child("candies").setValue(candies);
    }

    //init candies, prize, etc. only fields per competion - using time code
    //set/retrieve data from firebase and set the global vars.
    public static void initUserFieldsPerCompetition (final long candies) {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + userEmail + "/competitions/" + timeCode);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // need this for the first time only (?)
                if (dataSnapshot.getValue() == null) {
                    addCandiesToUser(candies);
                    return;
                }

                //candies

                Object c = ((Map) dataSnapshot.getValue()).get("candies");

                if (c == null) {
                    addCandiesToUser(candies);
                    MainActivity.setCandies(candies);
                }
                else {
                    long candies = (long) c;
                    MainActivity.setCandies(candies);
                }

                //prize

                Object p = ((Map) dataSnapshot.getValue()).get("prize");

                if (p == null) {
                    Prize.setPrizeChosen("NONE");
                }
                else {
                    String prize = (String) p;
                    Prize.setPrizeChosen(prize);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(userListener);
    }

    public static void addExpToUser (long exp) {
        myDatabase.child("users").child(userEmail).child("attributes").child("exp").setValue(exp);
    }

    //init exp - per user NOT per competition
    //set/retrieve data from firebase and set the global vars.
    public static void getUserAttributesFromFirebase () {

        Log.d(TAG, " getUserAttributesFromFirebase ");


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + userEmail + "/attributes");
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, " getUserAttributesFromFirebase  on data chagned ");


                // need this for the first time only (?)
                if (dataSnapshot.getValue() == null) {
                    Log.d(TAG, " getUserAttributesFromFirebase  value  == null ");
                    addExpToUser(0);
                    return;
                }

                //exp

                Object e = ((Map) dataSnapshot.getValue()).get("exp");

                if (e == null) {
                    Log.d(TAG, " getUserAttributesFromFirebase  e = null");

                    addExpToUser(0);
                    MainActivity.setExp(0);
                }
                else {
                    Log.d(TAG, " getUserAttributesFromFirebase  else ");

                    long exp = (long) e;
                    MainActivity.setExp(exp);
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

                Loader.increase();


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

    public static void addWinner (final String key, final String name, final String competition, final String details, final String imageName, final String prize, final Activity yourActivity) {
        final int minusKey = Integer.parseInt(key) * (-1);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(yourActivity.getString(R.string.firebase_storage));
        storageRef.child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Winner winner = new Winner(name, competition, details, imageName, uri.toString(), prize, minusKey);
                myDatabase.child("winners").push().setValue(winner);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public static void getWinnersFromFirebase(final Context context) {
        HallOfFame.dataHashWinners = new HashMap<>();
        DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("winners");
        ValueEventListener winnersDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HallOfFame.dataHashWinners = (HashMap<String, HashMap>) dataSnapshot.getValue();

                int size = HallOfFame.dataHashWinners.size();
                ArrayList<String[]> items = new ArrayList<String[]>();

                for (HashMap hm : HallOfFame.dataHashWinners.values()) {
                    String[] item = new String[2];
                    item[0] = (String) hm.get("imageUrl");
                    item[1] = (String) hm.get("minusKey").toString();
                    items.add(item);
                }
                Collections.sort(items, new java.util.Comparator<String[]>() {
                    public int compare(final String[] entry1, final String[] entry2) {
                        return (Integer.parseInt(entry1[1]) - Integer.parseInt(entry2[1]));
                    }
                });

                for (int i = 0; i < HallOfFamePreDownloadLimit; i++){
                    //Log.d("bbbbbbbb: ", items.get(i)[1]);
                    Picasso.with(context).load(items.get(i)[0]).transform(new CircleTransform()).fetch();
                }
                Loader.increase();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error!
            }
        };
        myRef2.orderByChild("minusKey").limitToFirst(HallOfFameListLimit).addValueEventListener(winnersDataListener);
    }

    //maybe wait for loader - because need to wait to this process to complete - or make the ui wait for this call
    public static void copyOldUserToNewUser (String oldUser, final String newUser) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + oldUser);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myDatabase.child("users").child(newUser).setValue(dataSnapshot.getValue());
                Log.d(TAG, "#########copied user############");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(userListener);
    }
    public static String formatEmail(String e){
        String s = e;
        if(e.contains(".")){
           s = e.replace(".","*@@*");
        }
        return s;
    }
}
