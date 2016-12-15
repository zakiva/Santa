package com.example.zakiva.santa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.zakiva.santa.Helpers.Storage.setStringPreferences;

public class nameForPreAlpha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_for_pre_alpha);
    }

    public void continueButtonClicked(View view) {
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        if (name.equals("")) {

        }
        else {
            setStringPreferences("username02", name ,getApplicationContext());
            startActivity(new Intent(nameForPreAlpha.this, Loader.class));
        }
    }
}
