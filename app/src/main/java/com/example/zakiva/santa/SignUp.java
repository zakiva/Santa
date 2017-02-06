package com.example.zakiva.santa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.zakiva.santa.Helpers.Storage;
import com.example.zakiva.santa.Models.FacebookSignUp;
import com.example.zakiva.santa.Models.GoogleSignUp;


public class SignUp extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
    public void goToGoogleSignIn(View view){
        startActivity(new Intent(SignUp.this, GoogleSignUp.class));
    }
    public void goToFacebookSignIn(View view){
        startActivity(new Intent(SignUp.this, FacebookSignUp.class));
    }
    @Override
    public void onBackPressed() {
    }

    public void backToScoreButton(View view) {
        finish();
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(MainActivity.TAG,"is it???"+Storage.getStringPreferences("signedUpType",this.getApplicationContext()));
        if(!(Storage.getStringPreferences("signedUpType",this.getApplicationContext()).equals("NONE"))){
            finish();
        }
    }
}
