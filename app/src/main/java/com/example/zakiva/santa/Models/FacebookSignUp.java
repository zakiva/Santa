package com.example.zakiva.santa.Models;

import android.app.Application;
import android.content.Intent;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookSignUp extends AppCompatActivity {

    private CallbackManager callbackManager;
    private String name = "NONE";
    private String gender = "NONE";
    private String ageRange = "NONE";
    private String email = "NONE";
    private String facebookUserId = "NONE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook_sign_up);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        RequestData();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends","email"));
    }


    public void RequestData() {
        final Application myApp = this.getApplication();
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject json, GraphResponse response) {

                json = response.getJSONObject();
                try {
                    if (json != null) {
                        facebookUserId = json.getString("id");
                        if(json.has("gender")) {
                            gender = json.getString("gender");
                        }
                        if(json.has("name")) {
                            name = json.getString("name");
                            Log.d(MainActivity.TAG, "name = " + json.getString("name"));
                        }
                        if(json.has("age_range")) {
                            ageRange = json.getString("age_range");
                        }
                        if(json.has("email")) {
                            email = json.getString("email");
                            Log.d(MainActivity.TAG, "email" + json.getString("email"));
                        }
                        Log.d(MainActivity.TAG, "granted: " + AccessToken.getCurrentAccessToken().getPermissions());
                        Log.d(MainActivity.TAG, "granted: " + AccessToken.getCurrentAccessToken().getDeclinedPermissions());
                        Infra.updateUserAttributes("facebook",ageRange,gender,name,email);
                        Storage.setStringPreferences(Loader.userEmailFieldName , facebookUserId, getApplicationContext());
                        Storage.setStringPreferences("signedUpType", "facebook", getApplicationContext());
                        String token = Storage.getStringPreferences("userToken", getApplicationContext());
                        Infra.copyOldUserToNewUser(token, facebookUserId);
                        ((Santa) myApp).setSignedUpType("facebook");
                        ((Santa) myApp).setGlobalEmail(facebookUserId);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, age_range");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(MainActivity.TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
