package com.example.zakiva.santa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class Google extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 10;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private SignInButton btnGoSign;
    private TextView name, email;
    private String i_name="", i_email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        initGoogle();

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        btnGoSign = (SignInButton)findViewById(R.id.goSignIn);
        btnGoSign.setSize(SignInButton.SIZE_ICON_ONLY);
        btnGoSign.setScopes(gso.getScopeArray());

        btnGoSign.setOnClickListener(this);
        test();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void initGoogle() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();
            try {
                Intent sendData = new Intent(Google.this, Google.class);
                String name, email;
                if (account != null) {
                    name = account.getDisplayName();
                    email = account.getEmail();
                    sendData.putExtra("p_name", name);
                    sendData.putExtra("p_email", email);
                    startActivity(sendData);
                }
            } catch (Exception e) {
                Toast.makeText(Google.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(Google.this, "login failed", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"google connection failed",Toast.LENGTH_LONG).show();
    }
    public void test() {
        Intent i = getIntent();
        i_name = i.getStringExtra("p_name");
        i_email = i.getStringExtra("p_email");
        name.setText(i_name);
        email.setText(i_email);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goSignIn:
                signIn();
                break;
        }
    }
}
