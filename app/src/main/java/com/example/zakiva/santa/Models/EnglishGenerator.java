package com.example.zakiva.santa.Models;

import com.example.zakiva.santa.Helpers.GeneratorHelper;
import com.example.zakiva.santa.TriviaGame;

import java.util.Random;

/**
 * Created by micha on 3/11/2017.
 */

public class EnglishGenerator {

    public TriviaQuestion groupGeneratorsWorldBands() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(5);
        switch (random) {
            case 0:tq = worldBandToAlbum();break;
            case 1:tq = worldBandToYear();break;
            case 2:tq = worldBandToMembers();break;
            case 3:tq = worldBandWhoFirst();break;
            case 4:tq = worldBandWhoLast();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsInventions() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(5);
        switch (random) {
            case 0:tq = inventionToCountry();break;
            case 1:tq = inventionToInventor();break;
            case 2:tq = inventionWhoFirst();break;
            case 3:tq = inventionWhoLast();break;
            case 4:tq = inventionToYear();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsWifeToHusband() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(2);
        switch (random) {
            case 0:tq = wifeToHusband();break;
            case 1:tq = husbandToWife();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsWorldCups() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(2);
        switch (random) {
            case 0:tq = hostToYear();break;
            case 1:tq = winnerToYear();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsBrands() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(5);
        switch (random) {
            case 0:tq = brandToYear();break;
            case 1:tq = brandWhoFirst();break;
            case 2:tq = brandWhoLast();break;
            case 3:tq = brandToCountry();break;
            case 4:tq = brandToFounder();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsLatitudes() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(2);
        switch (random) {
            case 0:tq = mostNorth();break;
            case 1:tq = mostSouth();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsActors() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(2);
        switch (random) {
            case 0:tq = actorToCharacter();break;
            case 1:tq = characterToActor();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsApps() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(3);
        switch (random) {
            case 0:tq = appToYear();break;
            case 1:tq = appFoundedFirst();break;
            case 2:tq = appFoundedLast();break;
        }return tq;
    }
    public TriviaQuestion quoteToPerson() {
        String[] q = {"quote"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("quotesEnglish"), "Who said \"#0#\"?", q, "name");
    }

    public TriviaQuestion carToCountry() {
        String[] q = {"car"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("carsEnglish"), "Where are the #0# cars made?", q, "country");
    }

    public TriviaQuestion leaderToYears() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldLeadersEnglish"), "During what years did #0# live?", q, "years");
    }

    public TriviaQuestion yearsToLeader() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("leadersYearsEnglish"), "During what years was #0#?", q, "years");
    }

    public TriviaQuestion actorToCharacter() {
        String[] q = {"actor", "name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("actorsEnglish"), "Which character was played by #0# in #1#?", q, "character");
    }

    public TriviaQuestion characterToActor() {
        String[] q = {"character", "name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("actorsEnglish"), "#0# in #1# was played by:", q, "actor");
    }

    public TriviaQuestion wifeToHusband() {
        String[] q = {"husband"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("wifeHusbandEnglish"), "#0# is married to:", q, "wife");
    }

    public TriviaQuestion husbandToWife() {
        String[] q = {"wife"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("wifeHusbandEnglish"), "#0# is married to:", q, "husband");
    }

    public TriviaQuestion inventionToYear() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("inventionsEnglish"), "In what year was #0# invented? ", q, "year");
    }
    public TriviaQuestion inventionToInventor() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("inventionsEnglish"), "Who invented #0#?", q, "inventor");
    }
    public TriviaQuestion inventionToCountry() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("inventionsEnglish"), "In which country was #0# invented?", q, "country");
    }
    public TriviaQuestion inventionWhoFirst() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("inventionsEnglish"), "Which of the following inventions was invented earlier?", "name", "year", false);
    }
    public TriviaQuestion inventionWhoLast() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("inventionsEnglish"), "Which of the following inventions was invented later?", "name", "year", true);
    }
    public TriviaQuestion brandWhoFirst() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("brandsEnglish"), "Which of the following brands was founded earlier?", "brand","year",false);
    }
    public TriviaQuestion brandWhoLast() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("brandsEnglish"), "Which of the following brands was founded later?", "brand","year",true);
    }
    public TriviaQuestion brandToYear() {
        String[] q = {"brand"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("brandsEnglish"), "In what year was #0# founded?",q, "year");
    }
    public TriviaQuestion brandToFounder() {
        String[] q = {"brand"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("brandsEnglish"), "Who founded #0#?",q, "founder");
    }
    public TriviaQuestion brandToCountry() {
        String[] q = { "brand"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("brandsEnglish"), "In which country was #0# founded?",q, "country");
    }
    public TriviaQuestion hostToYear() {
        String[] q = { "year"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldCupsEnglish"), "Which country hosted the FIFA World Cup in #0#?",q, "host");
    }
    public TriviaQuestion winnerToYear() {
        String[] q = { "year"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldCupsEnglish"), "Which country won the FIFA World Cup in #0#?",q, "winner");
    }
    public TriviaQuestion appToYear() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("appsEnglish"), "In what year was #0# launched?", q, "year");
    }
    public TriviaQuestion appFoundedFirst() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("appsEnglish"), "Which of the following apps was launched first?", "name","year",false);
    }
    public TriviaQuestion appFoundedLast() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("appsEnglish"), "Which of the following apps was launched later?", "name","year",true);
    }
    public TriviaQuestion mostNorth() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("latitudesEnglish"), "Which city is located further North?","city","latitude",true);
    }
    public TriviaQuestion mostSouth() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("latitudesEnglish"), "Which city is located further South?","city", "latitude",false);
    }
    public TriviaQuestion worldBandToAlbum() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldBandsEnglish"), "Which of the following albums belongs to #0#?",q,"albums");
    }
    public TriviaQuestion worldBandToYear() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldBandsEnglish"), "In what year were #0# formed? ", q, "year");
    }
    public TriviaQuestion worldBandToMembers() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldBandsEnglish"), "Who of the following was a member of #0#?",q, "members");
    }
    public TriviaQuestion worldBandWhoFirst() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("worldBandsEnglish"), "Which of the following bands was formed earlier?","name", "year",false);
    }
    public TriviaQuestion worldBandWhoLast() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("worldBandsEnglish"), "Which of the following bands was formed later?", "name", "year",true);
    }
}