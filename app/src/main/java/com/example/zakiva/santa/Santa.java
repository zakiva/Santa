package com.example.zakiva.santa;

import android.app.Application;


/**
 * Created by zakiva on 8/26/16.
 */

public class Santa extends Application {

    private String globalEmail;

    public String getGlobalEmail() {
        return globalEmail;
    }

    public void setGlobalEmail(String str) {
        globalEmail = str;
    }
}