package com.example.zakiva.santa.Models;

/**
 * Created by Ariel on 11/23/2016.
 */
public class Winner {
    public String name;
    public String competition;
    public String details;
    public String imageName;
    String imageUrl;

    public Winner() {}

    public Winner(String name, String competition, String details, String imageName, String imageUrl) {
        this.name = name;
        this.competition = competition;
        this.details = details;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
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

    public String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
