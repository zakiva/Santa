package com.example.zakiva.santa.Models;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by zakiva on 10/20/16.
 */

public class Game {

    public String type;
    public long score;
    public long timeStamp;
    public String dateAndTime;

    public Game() {}

    public Game (String type, long score) {
        this.type = type;
        this.score = score;
        long currentTime = System.currentTimeMillis();
        this.timeStamp = currentTime/1000;
        Timestamp stamp = new Timestamp(currentTime);
        Date date = new Date(stamp.getTime());
        this.dateAndTime = date.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getScore () {
        return score;
    }

    public void setScore (long score) {
        this.score = score;
    }

    public long getTimeStamp () {
        return timeStamp;
    }

    public void setTimeStamp (long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}







