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
import com.google.android.gms.drive.internal.StringListResponse;
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
    public static void getGlobalFieldsFromFirebase (final Context context) {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("globalFields");
        ValueEventListener timeCodeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String,Object> fields = (HashMap<String, Object>) dataSnapshot.getValue();

                String fullTimeCode = (String) fields.get("timeCode");

                String timeCodeFirstPart = fullTimeCode.split("_")[0];
                String timeCodeSecondPart = fullTimeCode.split("_")[1];

                // Setting the timecode
                MainActivity.setTimeCode(timeCodeFirstPart);

                //Log.d(TAG, "## We got the timecode: " + fields.get("timeCodeNew"));
                //Log.d(TAG, "<< >> MainActivity.getTime =  " + MainActivity.getTimeCode());

                DrawingGame.NUMBER_OF_DRAWINGS = (int) (long) ((Long) fields.get("drawingsNumber"));

                DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("prizes").child(timeCodeSecondPart);
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Map<String,Map<String, String>> bothPrizes = new HashMap<String,Map<String, String>>();
                        bothPrizes = (Map<String,Map<String, String>>) snapshot.getValue();
                        updatePrizeInfoFields(bothPrizes, context);
                        updatePrizeIcons();
                        Prize.updatePrizesNames();
                        Prize.colorPrizes();
                        Loader.increase();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("FB error (prizes): ", databaseError.getMessage());
                    }
                });
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

    public static void getTriviaDataFromFirebase(final String [] sheetsNames) {
        //must initialize data hash before puttint data inside
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("triviaDataSheets");
        ValueEventListener triviaDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Log.d(TAG, "snapppppppppppppp");

               // GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t = new GenericTypeIndicator<ArrayList<HashMap<String, Object>>>() {};

               // ArrayList<HashMap<String, Object>> lst = dataSnapshot.getValue(t);

                Map <String, Object> data = (Map) dataSnapshot.getValue();

                for (int i = 0; i < sheetsNames.length; i++) {
                    ArrayList<HashMap<String, Object>> lst = (ArrayList<HashMap<String, Object>>) data.get(sheetsNames[i]);
                    TriviaGame.addSheetToDataHash(sheetsNames[i], lst);
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
    public static void updateUserAttributes(String type,String ageRange,String gender,String name,String email) {
        myDatabase.child("users").child(userEmail).child("attributes").child("signedUpType").setValue(type);
        myDatabase.child("users").child(userEmail).child("attributes").child("ageRange").setValue(ageRange);
        myDatabase.child("users").child(userEmail).child("attributes").child("gender").setValue(gender);
        myDatabase.child("users").child(userEmail).child("attributes").child("name").setValue(name);
        myDatabase.child("users").child(userEmail).child("attributes").child("email").setValue(email);
    }
    public static String formatEmail(String e){
        String s = e;
        if(e.contains(".")){
           s = e.replace(".","*@@*");
        }
        return s;
    }

    public static void addPrize (final String prizeNumber, final String competitionNumber, final String timeCode, final String prizeName, final String category, final String imageName, final String worth, final String company, final String description, final Activity yourActivity) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(yourActivity.getString(R.string.firebase_storage));
        storageRef.child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Winner winner = new Winner(name, competition, details, imageName, uri.toString(), prize, minusKey);
                Map<String,String> prize = new HashMap<String, String>();
                prize.put("prizeNumber", prizeNumber);
                prize.put("competitionNumber", competitionNumber);
                prize.put("timeCode", timeCode);
                prize.put("prizeName", prizeName);
                prize.put("category", category);
                prize.put("worth", worth);
                prize.put("company", company);
                prize.put("description", description);
                prize.put("imageUrl", uri.toString());
                String aORb = "b";
                if ((Integer.parseInt(prizeNumber) % 2) == 1)
                    aORb = "a";
                myDatabase.child("prizes").child(competitionNumber).child(aORb).setValue(prize);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public static void updatePrizeInfoFields(Map<String,Map<String, String>> bothPrizes, Context context){

        Prize.prizeACategory = bothPrizes.get("a").get("category");
        Prize.prizeACompany = bothPrizes.get("a").get("company");
        Prize.prizeACompetitionNumber = bothPrizes.get("a").get("competitionNumber");
        Prize.prizeADescription = bothPrizes.get("a").get("description");
        Prize.prizeAImageUrl = bothPrizes.get("a").get("imageUrl");
        Prize.prizeAName = bothPrizes.get("a").get("prizeName");
        Prize.prizeANumber = bothPrizes.get("a").get("prizeNumber");
        Prize.prizeAWorth = bothPrizes.get("a").get("worth");

        Prize.prizeBCategory = bothPrizes.get("b").get("category");
        Prize.prizeBCompany = bothPrizes.get("b").get("company");
        Prize.prizeBCompetitionNumber = bothPrizes.get("b").get("competitionNumber");
        Prize.prizeBDescription = bothPrizes.get("b").get("description");
        Prize.prizeBImageUrl = bothPrizes.get("b").get("imageUrl");
        Prize.prizeBName = bothPrizes.get("b").get("prizeName");
        Prize.prizeBNumber = bothPrizes.get("b").get("prizeNumber");
        Prize.prizeBWorth = bothPrizes.get("b").get("worth");

        Picasso.with(context).load(Prize.prizeAImageUrl).fetch();
        Picasso.with(context).load(Prize.prizeBImageUrl).fetch();
    }

    public static void updatePrizeIcons(){

        switch (Prize.prizeACategory) {
            case "Culinary":
                Prize.drawableAEnabled = R.drawable.culinary_enable;
                Prize.drawableADisabled = R.drawable.culinary_disable;
                break;
            case "Lifestyle":
                Prize.drawableAEnabled = R.drawable.lifrstyle_enable;
                Prize.drawableADisabled = R.drawable.lifrstyle_disable;
                break;
            case "Fashion":
                Prize.drawableAEnabled = R.drawable.fashion_enable;
                Prize.drawableADisabled = R.drawable.fashion_disable;
                break;
            case "Gadgets":
                Prize.drawableAEnabled = R.drawable.gadget_enable;
                Prize.drawableADisabled = R.drawable.gadget_disable;
                break;
            case "Beauty Products":
                Prize.drawableAEnabled = R.drawable.beauty_enable;
                Prize.drawableADisabled = R.drawable.beauty_disable;
                break;
            case "Cash":
                Prize.drawableAEnabled = R.drawable.cash_enable;
                Prize.drawableADisabled = R.drawable.cash_disable;
                break;
            case "Baby Products":
                Prize.drawableAEnabled = R.drawable.babies_enable;
                Prize.drawableADisabled = R.drawable.babies_disable;
                break;
            case "Sport":
                Prize.drawableAEnabled = R.drawable.sport_enable;
                Prize.drawableADisabled = R.drawable.sport_disable;
                break;
            case "Tourism":
                Prize.drawableAEnabled = R.drawable.travel_enable;
                Prize.drawableADisabled = R.drawable.travel_disable;
                break;
            case "Pet Products":
                Prize.drawableAEnabled = R.drawable.animal_enable;
                Prize.drawableADisabled = R.drawable.animal_disable;
                break;
            case "Shopping":
                Prize.drawableAEnabled = R.drawable.shopping_enable;
                Prize.drawableADisabled = R.drawable.shopping_disable;
                break;
            default:
                break;
        }

        switch (Prize.prizeBCategory) {
            case "Culinary":
                Prize.drawableBEnabled = R.drawable.culinary_enable;
                Prize.drawableBDisabled = R.drawable.culinary_disable;
                break;
            case "Lifestyle":
                Prize.drawableBEnabled = R.drawable.lifrstyle_enable;
                Prize.drawableBDisabled = R.drawable.lifrstyle_disable;
                break;
            case "Fashion":
                Prize.drawableBEnabled = R.drawable.fashion_enable;
                Prize.drawableBDisabled = R.drawable.fashion_disable;
                break;
            case "Gadgets":
                Prize.drawableBEnabled = R.drawable.gadget_enable;
                Prize.drawableBDisabled = R.drawable.gadget_disable;
                break;
            case "Beauty Products":
                Prize.drawableBEnabled = R.drawable.beauty_enable;
                Prize.drawableBDisabled = R.drawable.beauty_disable;
                break;
            case "Cash":
                Prize.drawableBEnabled = R.drawable.cash_enable;
                Prize.drawableBDisabled = R.drawable.cash_disable;
                break;
            case "Baby Products":
                Prize.drawableBEnabled = R.drawable.babies_enable;
                Prize.drawableBDisabled = R.drawable.babies_disable;
                break;
            case "Sport":
                Prize.drawableBEnabled = R.drawable.sport_enable;
                Prize.drawableBDisabled = R.drawable.sport_disable;
                break;
            case "Tourism":
                Prize.drawableBEnabled = R.drawable.travel_enable;
                Prize.drawableBDisabled = R.drawable.travel_disable;
                break;
            case "Pet Products":
                Prize.drawableBEnabled = R.drawable.animal_enable;
                Prize.drawableBDisabled = R.drawable.animal_disable;
                break;
            case "Shopping":
                Prize.drawableBEnabled = R.drawable.shopping_enable;
                Prize.drawableBDisabled = R.drawable.shopping_disable;
                break;
            default:
                break;
        }
    }
}
