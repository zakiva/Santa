package com.example.zakiva.santa.Models;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.zakiva.santa.Helpers.Infra;
import com.example.zakiva.santa.Helpers.Storage;
import com.example.zakiva.santa.Loader;
import com.example.zakiva.santa.MainActivity;
import com.example.zakiva.santa.R;
import com.example.zakiva.santa.Santa;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class GoogleSignUp extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 10;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions googleSignInOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_up);
        initGoogle();
        signIn();
    }
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void initGoogle() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile().requestEmail().requestScopes(Plus.SCOPE_PLUS_LOGIN, Plus.SCOPE_PLUS_PROFILE, new Scope("https://www.googleapis.com/auth/plus.profile.emails.read"))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Plus.API)
                .build();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "google connection failed", Toast.LENGTH_LONG).show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(MainActivity.TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (resultCode == RESULT_OK)
        Log.d(MainActivity.TAG, "second step = ");
        GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        GoogleSignInAccount account = googleSignInResult.getSignInAccount();
        try {
            String name, email;
            if (account != null) {
                name = account.getDisplayName();
                email = account.getEmail();
                getExtraInfoFromGoogle(account,name);
                Log.d(MainActivity.TAG, "googleEmail = " + email);
                Log.d(MainActivity.TAG, "googleName = " + name);
                String formattedEmail = Infra.formatEmail(email);
                Storage.setStringPreferences(Loader.userEmailFieldName , formattedEmail, getApplicationContext());
                Storage.setStringPreferences("signedUpType", "google", getApplicationContext());
                String token = Storage.getStringPreferences("userToken", getApplicationContext());
                Infra.copyOldUserToNewUser(token, formattedEmail);
                ((Santa) this.getApplication()).setSignedUpType("google");
                ((Santa) this.getApplication()).setGlobalEmail(formattedEmail);
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void getExtraInfoFromGoogle(GoogleSignInAccount account, final String name) {
        Plus.PeopleApi.load(mGoogleApiClient, account.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
            @Override
            public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
                Person person = loadPeopleResult.getPersonBuffer().get(0);
                String gender = getMaleFemale(person.getGender());
                String ageRange = String.valueOf(person.getAgeRange());
                Infra.updateUserAttributes("google",ageRange,gender,name);
                Log.d(MainActivity.TAG, "Gender: " + person.getGender());
                Log.d(MainActivity.TAG, "Birthday: " + person.getBirthday());
                Log.d(MainActivity.TAG, "AgeRange: " + person.getAgeRange());
            }
        });
    }
    public String getMaleFemale(int gender){
        if(gender==0) {
            return "Male";
        }else if(gender==1){
            return "Female";
        }else{
            return "NONE";
        }
    }
}
