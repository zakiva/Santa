package com.example.zakiva.santa;

import com.example.zakiva.santa.Models.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = ">>>>>>>Debug: ";
    private String userEmail;
    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabase = FirebaseDatabase.getInstance().getReference();
        //set the real user email instead
        ((Santa) this.getApplication()).setGlobalEmail("userDemoEmail");
        userEmail = ((Santa) this.getApplication()).getGlobalEmail();
    }

    public void newUserClicked(View view) {
        Log.d(TAG, "newUserClicked");
        User user = new User(userEmail);
        myDatabase.child("Users").child(userEmail).setValue(user);
    }

    //example how to get an object from the database
    public void getUser () {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users/" + userEmail);
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
}
