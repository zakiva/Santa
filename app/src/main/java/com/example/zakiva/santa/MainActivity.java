package com.example.zakiva.santa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = ">>>>>>>Debug: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newButtonClicked(View view) {
        Log.d(TAG, "newButtonClicked");
        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();
        myDatabase.child("example").setValue("WOW!");
    }
}
