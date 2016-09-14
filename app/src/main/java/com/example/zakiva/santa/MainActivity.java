package com.example.zakiva.santa;

import com.example.zakiva.santa.Models.*;
import com.example.zakiva.santa.Helpers.*;
import static com.example.zakiva.santa.Helpers.Infra.*;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vungle.publisher.VunglePub;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = ">>>>>>>Debug: ";

    final VunglePub vunglePub = VunglePub.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set the real user email instead
        ((Santa) this.getApplication()).setGlobalEmail("userDemoEmail");
        initInfra(((Santa) this.getApplication()).getGlobalEmail());

        //Vungle init
        VungleAds vungleAds = new VungleAds();
        vungleAds.vungleInit(MainActivity.this);

        //Parser.readExcelFile(this, "/storage/emulated/0/Download/trivia2.xls");
        //OneSignal.sendTag("key", "value");
    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vunglePub.clearEventListeners();
    };

    public void startClicked(View view) {
        addUser();
        addCompetition("compeexample", "giftexample");
        getUser();
        getCompetition("compeexample");
    }
    public void goToGoogle(View v) {
        startActivity(new Intent(MainActivity.this, Google.class));
    }

    public void triviaClicked(View view) {
        startActivity(new Intent(MainActivity.this, Trivia.class));
    }
    public void goToFb(View v) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        startActivity(new Intent(MainActivity.this, Facebook.class));
    }

    public void playAd(View v) {
        vunglePub.playAd();
    }
}