package com.example.zakiva.santa.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zakiva.santa.Santa;

/**
 * Created by Ariel on 12/15/2016.
 */
public class Storage {
    //Use this function like this (from any activity):
    //getStringFromSharedPreferences("key", getApplicationContext());
    public static String getStringFromSharedPreferences(String key, Context context){
        SharedPreferences settings = context.getSharedPreferences("MY_DATA", 0);
        String value = settings.getString(key, "NONE");
        return value;
    }

    //Use this function like this (from any activity):
    //writeStringToSharedPreferences("key", "value", getApplicationContext());
    public static void writeStringToSharedPreferences(String key, String value, Context context){
        SharedPreferences settings = context.getSharedPreferences("MY_DATA", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
