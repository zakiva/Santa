package com.example.zakiva.santa;

import android.app.Application;
import android.content.res.Configuration;

import java.util.Locale;


/**
 * Created by zakiva on 8/26/16.
 */

public class Santa extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private String globalEmail;

    public String getGlobalEmail() {
        return globalEmail;
    }

    public void setGlobalEmail(String str) {
        globalEmail = str;
    }
}