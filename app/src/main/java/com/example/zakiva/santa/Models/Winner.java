package com.example.zakiva.santa.Models;

/**
 * Created by Ariel on 11/23/2016.
 */
public class Winner {
    public String name;
    public String competition;
    public String details;
    public String imageName;
    public String imageUrl;
    public String prize;

    public Winner() {}

    public Winner(String name, String competition, String details, String imageName, String imageUrl, String prize) {
        this.name = name;
        this.competition = competition;
        this.details = details;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.prize = prize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
