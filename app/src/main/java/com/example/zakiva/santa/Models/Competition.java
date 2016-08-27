package com.example.zakiva.santa.Models;

/**
 * Created by zakiva on 8/27/16.
 */

public class Competition {

    public String key;
    public String giftId;

    public Competition() {}

    public Competition(String key, String giftId) {
        this.key = key;
        this.giftId = giftId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
