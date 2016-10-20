package com.example.zakiva.santa.Models;

/**
 * Created by zakiva on 10/20/16.
 */

public class Game {

    public String type;
    public long score;

    public Game() {}

    public Game (String type, long score) {
        this.type = type;
        this.score = score;
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
}







