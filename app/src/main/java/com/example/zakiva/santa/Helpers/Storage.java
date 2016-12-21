package com.example.zakiva.santa.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zakiva.santa.Santa;

/**
 * Created by Ariel on 12/15/2016.
 */
public class Storage {
    //Use this function like this (from any activity):
    //getStringPreferences("key", getApplicationContext());
    //if key does not exist return "NONE"
    public static String getStringPreferences(String key, Context context){
        SharedPreferences settings = context.getSharedPreferences("MY_DATA", 0);
        String value = settings.getString(key, "NONE");
        return value;
    }

    //Use this function like this (from any activity):
    //setStringPreferences("key", "value", getApplicationContext());
    public static void setStringPreferences(String key, String value, Context context){
        SharedPreferences settings = context.getSharedPreferences("MY_DATA", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }


    //Use this function like this (from any activity):
    //getStringPreferences("key", getApplicationContext());
    //if key does not exist return Integer.MAX_VALUE
    public static int getIntPreferences(String key, Context context){
        SharedPreferences settings = context.getSharedPreferences("MY_DATA", 0);
        int value = settings.getInt(key, Integer.MAX_VALUE);
        return value;
    }

    //Use this function like this (from any activity):
    //setStringPreferences("key", value, getApplicationContext());
    public static void setIntPreferences(String key, int value, Context context){
        SharedPreferences settings = context.getSharedPreferences("MY_DATA", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    //Use this function like this (from any activity):
    //getStringPreferences("key", getApplicationContext());
    //if key does not exist return false. This can be very confusing, use with caution!
    public static boolean getBooleanPreferences(String key, Context context){
        SharedPreferences settings = context.getSharedPreferences("MY_DATA", 0);
        boolean value = settings.getBoolean(key, false);
        return value;
    }

    //Use this function like this (from any activity):
    //setStringPreferences("key", value, getApplicationContext());
    public static void setBooleanPreferences(String key, boolean value, Context context){
        SharedPreferences settings = context.getSharedPreferences("MY_DATA", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
