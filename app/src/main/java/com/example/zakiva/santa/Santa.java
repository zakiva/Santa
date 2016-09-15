package com.example.zakiva.santa;

import android.app.Application;
import android.os.Bundle;

import com.example.zakiva.santa.Helpers.VungleAds;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.VunglePub;


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

    @Override
    public void onCreate() {
        super.onCreate();
    }

}