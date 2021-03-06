package com.example.zakiva.santa;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zakiva.santa.Helpers.Infra;
import com.example.zakiva.santa.Helpers.Storage;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Facebook extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView email;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        email = (TextView)findViewById(R.id.email);
        name = (TextView)findViewById(R.id.name);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton)findViewById(R.id.loginButton);

        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

                if(AccessToken.getCurrentAccessToken() != null){
                    RequestData();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void RequestData(){
        final Application myApp = this.getApplication();
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){

                        email.setText(json.getString("email"));
                        name.setText(json.getString("name"));
                        String formattedEmail = Infra.formatEmail(json.getString("email"));
                        Storage.setStringPreferences("userEmail",formattedEmail,getApplicationContext());
                        Storage.setStringPreferences("signedUpType","facebook",getApplicationContext());
                        String token = Storage.getStringPreferences("userToken",getApplicationContext());
                        Infra.copyOldUserToNewUser(token,formattedEmail);
                        ((Santa) myApp).setSignedUpType("facebook");
                        ((Santa) myApp).setGlobalEmail(formattedEmail);
                        //Infra.addSignedUpType("facebook");
                        Log.d(MainActivity.TAG,"email"+ json.getString("email"));
                        Log.d(MainActivity.TAG,"name = "+json.getString("name"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}