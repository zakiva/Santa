package com.example.zakiva.santa.Models;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.example.zakiva.santa.Helpers.Infra;
import com.example.zakiva.santa.Helpers.Storage;
import com.example.zakiva.santa.MainActivity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by micha on 12/15/2016.
 */

public class Token {
    static String userToken = null;

    // return a cached unique ID for each device
    public static String getID(Context context) {
        // if the ID isn't cached inside the class itself
        if (userToken == null) {
            //get it from database / settings table (implement your own method here)
           userToken = Storage.getStringPreferences("userToken",context);
        }
        // if the saved value was incorrect
        if (userToken.equals("NONE")) {
            userToken = generateID(context);
            Storage.setStringPreferences("userToken",userToken,context);
        }
        return userToken;
    }

    // generate a unique ID for each device
    // use available schemes if possible / generate a random signature instead
    private static String generateID(Context context) {

        // use the ANDROID_ID constant, generated at the first device boot
        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // in case known problems are occured
        String badDefaultToken = "9774d56d682e549c";
        if (badDefaultToken.equals(deviceId) || deviceId == null) {

            // get a unique deviceID like IMEI for GSM or ESN for CDMA phones
            // don't forget:
            //
            deviceId = UUID.randomUUID().toString();
            Log.d(MainActivity.TAG,"id = "+deviceId);
            // if nothing else works, generate a random number
            if (deviceId == null) {
                Random tmpRand = new Random();
                deviceId = String.valueOf(tmpRand.nextLong());
            }

        }

        // any value is hashed to have consistent format
        return getHash(deviceId);
    }

    // generates a SHA-1 hash for any string
    public static String getHash(String stringToHash) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] result = null;

        try {
            result = digest.digest(stringToHash.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        for (byte b : result)
        {
            sb.append(String.format("%02X", b));
        }

        String messageDigest = sb.toString();
        return messageDigest;
    }
}
