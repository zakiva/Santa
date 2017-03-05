package com.example.zakiva.santa.Models;

import com.example.zakiva.santa.Helpers.GeneratorHelper;
import com.example.zakiva.santa.TriviaGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.zakiva.santa.Helpers.GeneratorHelper.getNumericAnswers;

/**
 * Created by zakiva on 9/28/16.
 */

public class Generator {

    public TriviaQuestion plusSeq() {
        ArrayList<Integer> pro = GeneratorHelper.returnSidra(21, "plusSeq");
        int n = pro.get(3);
        String s  =String.valueOf(pro.get(3));
        pro.remove(3);
        String sidra =pro.toString().replace("[", "").replace("]", "").trim();
        return new TriviaQuestion("30", "בסדרה "+sidra+" מה המספר הבא ?",s,s, getNumericAnswers(n,5).get(0), getNumericAnswers(n,5).get(1), getNumericAnswers(n,5).get(2),false);
    }
    public TriviaQuestion minusSeq(){
        ArrayList<Integer> pro = GeneratorHelper.returnSidra(21, "minusSeq");
        int n = pro.get(3);
        String s = String.valueOf(pro.get(3));
        pro.remove(3);
        String sidra =pro.toString().replace("[", "").replace("]", "").trim();
        return new TriviaQuestion("31", "בסדרה "+sidra+" מה המספר הבא ?", s,getNumericAnswers(n,5).get(0), getNumericAnswers(n,5).get(1), getNumericAnswers(n,5).get(2),s,false);
    }
    public TriviaQuestion plusPlusSeq(){
        ArrayList<Integer> pro = GeneratorHelper.returnSidra(21, "plusPlusSeq");
        int n = pro.get(3);
        String s = String.valueOf(pro.get(3));
        pro.remove(3);
        String sidra =pro.toString().replace("[", "").replace("]", "").trim();
        return new TriviaQuestion("32", "בסדרה "+sidra+" מה המספר הבא ?", s,getNumericAnswers(n,5).get(0),s, getNumericAnswers(n,5).get(1), getNumericAnswers(n,5).get(2),false);
    }
    public TriviaQuestion minusMinusSeq() {
        ArrayList<Integer> pro = GeneratorHelper.returnSidra(21, "minusMinusSeq");
        int n = pro.get(3);
        String s = String.valueOf(pro.get(3));
        pro.remove(3);
        String sidra =pro.toString().replace("[", "").replace("]", "").trim();

       return new TriviaQuestion("33", "בסדרה "+sidra+" מה המספר הבא ?", s, s, getNumericAnswers(n,5).get(0), getNumericAnswers(n,5).get(1),getNumericAnswers(n,5).get(2),false);
    }
    public TriviaQuestion geoSeq(){
        int a1=1+(int)(Math.random() * 7);
        System.out.println(a1);

        List<Integer> pro =new ArrayList<Integer>();
        pro.add(a1);
        int b=a1;
        while(pro.size()<4){
            b=a1*b;
            pro.add(b);
        }
        int n = pro.get(3);
        String s = String.valueOf(pro.get(3));
        pro.remove(3);
        String sidra = pro.toString().replace("[", "").replace("]", "").trim();

        return new TriviaQuestion("33", "בסדרה זו מה המספר הבא " + sidra + " ?", s, getNumericAnswers(n, 5).get(0), getNumericAnswers(n, 5).get(1), s, getNumericAnswers(n, 5).get(2),false);
    }
    public TriviaQuestion groupGeneratorsIsraelBands() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(5);
            switch (random) {
                case 0:tq = israelBandToAlbum();break;
                case 1:tq = israelBandToYear();break;
                case 2:tq = israelBandToMembers();break;
                case 3:tq = israelBandWhoFirst();break;
                case 4:tq = israelBandWhoLast();break;
            }return tq;
    }
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
    public TriviaQuestion groupGeneratorsAuthors() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(2);
        switch (random) {
            case 0:tq = authorToCountry();break;
            case 1:tq = bookToAuthor();break;
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
    public TriviaQuestion groupGeneratorsCountries() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(8);
        switch (random) {
            case 0:tq = countryLeastArea();break;
            case 1:tq = countryLeastGdp();break;
            case 2:tq = countryLeastPop();break;
            case 3:tq = countryMostArea();break;
            case 4:tq = countryMostGdp();break;
            case 5:tq = countryMostPop();break;
            case 6:tq = countryToCapital();break;
            case 7:tq = countryToCoin();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsIsraelEvents() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(5);
        switch (random) {
            case 0:tq = eventToYear();break;
            case 1:tq = defenseMinisterToEvent();break;
            case 2:tq = primeMinisterToEvent();break;
            case 3:tq = generalToEvent();break;
            case 4:tq = yearToEvent();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsSingers() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(4);
        switch (random) {
            case 0:tq = albumToSinger();break;
            case 1:tq = bornToSinger();break;
            case 2:tq = songToSinger();break;
            case 3:tq = countryToSinger();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsChampionships() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(3);
        switch (random) {
            case 0:tq = mostChampionships();break;
            case 1:tq = leastChampionships();break;
            case 2:tq = teamToChampionships();break;
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
    public TriviaQuestion groupGeneratorsMaleActors() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(2);
        switch (random) {
            case 0:tq = maleActorToCharacter();break;
            case 1:tq = characterToMaleActor();break;
        }return tq;
    }
    public TriviaQuestion groupGeneratorsFemaleActors() {
        TriviaQuestion tq = null;
        int random = new Random().nextInt(2);
        switch (random) {
            case 0:tq = femaleActorToCharacter();break;
            case 1:tq = characterToFemaleActor();break;
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

    public TriviaQuestion israelBandToAlbum() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("israelBands"), "איזה מהאלבומים הבאים שייך ללהקת #0#?", q, "albums");
    }
    public TriviaQuestion worldBandToAlbum() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldBands"), "איזה מהאלבומים הבאים שייך ללהקת #0#?",q,"albums");
    }
    public TriviaQuestion israelBandToYear() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("israelBands"), "באיזו שנה הוקמה להקת #0#?", q, "year");
    }
    public TriviaQuestion worldBandToYear() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldBands"), "באיזו שנה הוקמה להקת #0#?", q, "year");
    }
    public TriviaQuestion israelBandToMembers() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("israelBands"), "מי מהבאים חבר בלהקת #0#?",q, "members");
    }
    public TriviaQuestion worldBandToMembers() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldBands"), "מי מהבאים חבר בלהקת #0#?",q, "members");
    }
    public TriviaQuestion worldBandWhoFirst() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("worldBands"), "איזו מהלהקות הבאות הוקמה קודם?","name", "year",false);
    }
    public TriviaQuestion israelBandWhoFirst() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("israelBands"), "איזו מהלהקות הבאות הוקמה קודם?", "name", "year",false);
    }
   public TriviaQuestion israelBandWhoLast() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("israelBands"), "איזו מהלהקות הבאות הוקמה מאוחר יותר?", "name", "year",true);
    }
    public TriviaQuestion worldBandWhoLast() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("worldBands"), "איזו מהלהקות הבאות הוקמה מאוחר יותר?", "name", "year",true);
    }
    public TriviaQuestion inventionToYear() {
        String[] q = {"name"};
        TriviaQuestion tq = GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("inventions"), "באיזו שנה הומצא #0#?", q, "year");
        if(tq.getMale()){
            return tq;
        }
        tq.setQuestion(tq.getQuestion().replace("הומצא","הומצאה"));
        return tq;
    }
    public TriviaQuestion inventionToInventor() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("inventions"), "מי המציא את #0#?", q, "inventor");
    }
    public TriviaQuestion inventionToCountry() {
        String[] q = {"name"};
        TriviaQuestion tq = GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("inventions"), "באיזו מדינה הומצא #0#?", q, "country");
        if(tq.getMale()){
            return tq;
        }
        tq.setQuestion(tq.getQuestion().replace("הומצא","הומצאה"));
        return tq;
}
   public TriviaQuestion inventionWhoFirst() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("inventions"), "איזו מההמצאות הבאות הומצאה קודם?","name", "year",false);
    }
    public TriviaQuestion inventionWhoLast() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("inventions"), "איזו מההמצאות הבאות הומצאה מאוחר יותר?","name", "year",true);
    }
    public TriviaQuestion authorToCountry() {
        String[] q = {"name"};
        TriviaQuestion tq = GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("authors"), "היכן נולד הסופר #0#?",q, "country");
        if(tq.getMale()){
            return tq;
        }
        tq.setQuestion(tq.getQuestion().replace("נולד הסופר","נולדה הסופרת"));
        return tq;
    }
    public TriviaQuestion bookToAuthor() {
        String[] q = {"name"};
        TriviaQuestion tq = GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("authors"), "איזה מהספרים הבאים כתב #0#?",q, "books");
        if(tq.getMale()){
            return tq;
        }
        tq.setQuestion(tq.getQuestion().replace("כתב","כתבה"));
        return tq;
    }
    public TriviaQuestion sonToFather() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("bibleFathers"), "בתנ\"ך, מי היה אביו של #0#?",q, "father");
    }
    public TriviaQuestion quoteToPerson() {
        String[] q = {"quote"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("quotes"), "מי אמר \"#0#\"?",q, "name");
    }
    public TriviaQuestion wifeToHusband() {
        String[] q = {"husband"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("wifeHusband"), "אשתו של #0# היא:",q, "wife");
    }
    public TriviaQuestion husbandToWife() {
        String[] q = {"wife"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("wifeHusband"), "בעלה של #0# הוא:", q, "husband");
    }
    public TriviaQuestion hostToYear() {
        String[] q = { "year"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldCups"), "איזו מדינה אירחה את המונדיאל בשנת #0#?",q, "host");
    }
    public TriviaQuestion winnerToYear() {
        String[] q = { "year"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldCups"), "איזו מדינה זכתה במונדיאל בשנת #0#?",q, "winner");
    }
    public TriviaQuestion leaderToYears() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldLeaders"), "באילו שנים חי #0#?",q, "years");
    }
      public TriviaQuestion brandWhoFirst() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("brands"), "איזה מהמותגים הבאים נוסד מוקדם יותר?", "brand","year",false);
    }
    public TriviaQuestion brandWhoLast() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("brands"), "איזה מהמותגים הבאים נוסד מאוחר יותר?", "brand","year",true);
    }
    public TriviaQuestion brandToYear() {
        String[] q = {"brand"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("brands"), "באיזו שנה נוסד המותג #0#?",q, "year");
    }
    public TriviaQuestion brandToFounder() {
        String[] q = {"brand"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("brands"), "מי ייסד את #0#?",q, "founder");
    }
    public TriviaQuestion brandToCountry() {
        String[] q = { "brand"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("brands"), "באיזו מדינה נוסד המותג #0#?",q, "country");
    }
    public TriviaQuestion countryToCoin () {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("countries"), "באיזה מטבע משתמשים ב#0#?",q, "coin");
    }
    public TriviaQuestion countryToCapital () {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("countries"), "מהי עיר הבירה של #0#?",q, "capital");
    }
    public TriviaQuestion countryMostArea () {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("countries"), "איזו מהמדינות הבאות בעלת השטח הגדול ביותר?","name","area",true);
    }
    public TriviaQuestion countryLeastArea () {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("countries"), "איזו מהמדינות הבאות בעלת השטח הקטן ביותר?","name","area",false);
    }
    public TriviaQuestion countryMostPop () {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("countries"), "לאיזו מהמדינות הבאות אוכלוסייה גדולה יותר?","name","population",true);
    }
    public TriviaQuestion countryLeastPop () {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("countries"), "לאיזו מהמדינות הבאות אוכלוסייה קטנה יותר?","name","population",false);
    }
    public TriviaQuestion countryMostGdp () {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("countries"), "לאיזו מהמדינות הבאות תמ\"ג גבוה יותר?","name","gdp",true);
    }
    public TriviaQuestion countryLeastGdp () {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("countries"), "לאיזו מהמדינות הבאות תמ\"ג נמוך יותר?","name","gdp",false);
    }
    public TriviaQuestion eventToYear() {
        String[] q = {"year"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("israelEvents"), "איזה אירוע התקיים בשנת #0#?",q, "event");
    }
    public TriviaQuestion yearToEvent() {
        String[] q = { "event"};
        TriviaQuestion tq = GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("israelEvents"), "באיזו שנה התרחש #0#?",q, "year");
        if(tq.getMale()){
            return tq;
        }
        tq.setQuestion(tq.getQuestion().replace("התרחש","התרחשה"));
        return tq;
    }
    public TriviaQuestion generalToEvent() {
        String[] q = { "event"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("israelEvents"), "מי כיהן כרמטכ״ל בזמן #0#?",q, "general");
    }
    public TriviaQuestion primeMinisterToEvent() {
        String[] q = {"event"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("israelEvents"), "מי כיהן כראש הממשלה בזמן #0#?",q, "pminister");
    }
    public TriviaQuestion defenseMinisterToEvent() {
        String[] q = {"event"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("israelEvents"), "מי כיהן כשר הביטחון בזמן #0#?",q, "dminister");
    }
    public TriviaQuestion albumToSinger() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("singers"), "איזה מהאלבומים הבאים שייך ל#0#?",q, "albums");
    }
    public TriviaQuestion songToSinger() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("singers"), "איזה מהשירים הבאים שייך ל#0#?",q, "songs");
    }
    public TriviaQuestion bornToSinger() {
        String[] q = {"name"};
        TriviaQuestion tq = GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("singers"), "באיזו שנה נולד הזמר #0#?",q, "born");
        if(tq.getMale()){
            return tq;
        }
        tq.setQuestion(tq.getQuestion().replace("נולד הזמר","נולדה הזמרת"));
        return tq;
    }
    public TriviaQuestion countryToSinger() {
        String[] q = {"name"};
        TriviaQuestion tq = GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("singers"), "באיזו מדינה נולד הזמר #0#?",q, "country");
        if(tq.getMale()){
            return tq;
        }
        tq.setQuestion(tq.getQuestion().replace("נולד הזמר","נולדה הזמרת"));
        return tq;
    }
/*    public TriviaQuestion singerToSong() {
        String[] q = {"songs"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("singers"), "השיר #0# בוצע במקור על ידי?",q, "name");
    }*/
    public TriviaQuestion teamToChampionships() {
        String[] q = {"team"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("championships"), "בכמה אליפויות זכתה #0# (בכדורגל)?",q, "total");
    }
   public TriviaQuestion mostChampionships() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("championships"), "מי מבין הקבוצות הבאות זכתה בהכי הרבה אליפויות (בכדורגל)?","team","total",true);
    }
    public TriviaQuestion leastChampionships() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("championships"), "מי מבין הקבוצות הבאות זכתה בהכי מעט אליפויות (בכדורגל)?", "team", "total",false);
    }
    public TriviaQuestion mostNorth() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("latitudes"), "איזו עיר ממוקמת צפונית יותר?","city","latitude",true);
    }
    public TriviaQuestion mostSouth() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("latitudes"), "איזו עיר ממוקמת דרומית יותר?","city", "latitude",false);
    }
    public TriviaQuestion yearsToLeader() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("leadersYears"), "באילו שנים כיהן #0#?",q, "years");
    }
 /*   public TriviaQuestion timeZone() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("timeZones"), "אם השעה ב... היא 14:00 מה השעה ב... (עיר)", "name", "years");
    }*/
    public TriviaQuestion maleActorToCharacter() {
        String [] q = {"actor", "type", "name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("maleActors"), "איזו דמות גילם #0# ב#1# #2#?",q, "character");
    }
    public TriviaQuestion femaleActorToCharacter() {
        String [] q = {"actor", "type", "name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("femaleActors"), "איזו דמות גילמה #0# ב#1# #2#?",q, "character");
    }
    public TriviaQuestion characterToMaleActor() {
        String[] q = {"character", "type", "name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("maleActors"), "איזה שחקן גילם את דמותו של #0# ב#1# #2#?", q, "actor");
    }
    public TriviaQuestion characterToFemaleActor() {
        String[] q = {"character", "type", "name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("femaleActors"), "איזו שחקנית גילמה את דמותה של #0# ב#1# #2#?", q, "actor");
    }
    public TriviaQuestion carToCountry() {
        String[] q = {"car"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("cars"), "באיזו מדינה מיוצר רכב ה#0#?", q, "country");
    }
    public TriviaQuestion appToYear() {
        String[] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("apps"), "באיזו שנה הושקה אפליקצית #0#?", q, "year");
    }
    public TriviaQuestion appFoundedFirst() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("apps"), "איזו אפליקציה הושקה קודם?", "name","year",false);
    }
    public TriviaQuestion appFoundedLast() {
        return GeneratorHelper.maxGenerateQuestionWithData(TriviaGame.dataHash.get("apps"), "איזו אפליקציה הושקה מאוחר יותר?", "name","year",true);
    }


    /*

    COMMENTING OUT THESE EXAMPLE METHODS - WE NOW USE REAL DATA


    //this method is just an example of preparing the data - to be removed
    public static ArrayList<HashMap<String, Object>> prepareBandsData () {

        HashMap<String, Object> d1 = new HashMap<>();
        HashMap<String, Object> d2 = new HashMap<>();
        HashMap<String, Object> d3 = new HashMap<>();
        HashMap<String, Object> d4 = new HashMap<>();
        HashMap<String, Object> d5 = new HashMap<>();
        HashMap<String, Object> d6 = new HashMap<>();

        d1.put("name", "הביטלס");
        d2.put("name", "טיפקס");
        d3.put("name", "אתניקס");
        d4.put("name", "הדלתות");
        d5.put("name", "השועלים");
        d6.put("name", "האבנים המתגלגלות");

        ArrayList <String> albums1 = new ArrayList<> (Arrays.asList("בום 1", "אלבום כלשהו"));
        ArrayList <String> albums2 = new ArrayList<> (Arrays.asList("בום 1", "אלבום כלשהו"));
        ArrayList <String> albums3 = new ArrayList<> (Arrays.asList("בום 3", "אלבום כלשהו"));
        ArrayList <String> albums4 = new ArrayList<> (Arrays.asList("בום 4", "אלבום כלשהו"));
        ArrayList <String> albums5 = new ArrayList<> (Arrays.asList("בום 2", "אלבום כלשהו"));
        ArrayList <String> albums6 = new ArrayList<> (Arrays.asList("בום 1", "אלבום כלשהו"));

        d1.put("albums", albums1);
        d2.put("albums", albums2);
        d3.put("albums", albums3);
        d4.put("albums", albums4);
        d5.put("albums", albums5);
        d6.put("albums", albums6);

        d1.put("year", "1910");
        d2.put("year", "1930");
        d3.put("year", "1940");
        d4.put("year", "1950");
        d5.put("year", "1970");
        d6.put("year", "1910");

        return new ArrayList<HashMap<String, Object>> (Arrays.asList(d1, d2, d3, d4, d5, d6));
    }

    //this method is just an example of preparing the data - to be removed
    public static ArrayList<HashMap<String, Object>> prepareCountriesData () {

        HashMap<String, Object> d1 = new HashMap<>();
        HashMap<String, Object> d2 = new HashMap<>();
        HashMap<String, Object> d3 = new HashMap<>();
        HashMap<String, Object> d4 = new HashMap<>();
        HashMap<String, Object> d5 = new HashMap<>();
        HashMap<String, Object> d6 = new HashMap<>();

        d1.put("name", "ישראל");
        d2.put("name", "ספרד");
        d3.put("name", "צרפת");
        d4.put("name", "בלגיה");
        d5.put("name", "יוון");
        d6.put("name", "אוסטרליה");

        d1.put("capital", "ירושלים");
        d2.put("capital", "מדריד");
        d3.put("capital", "פריז");
        d4.put("capital", "בריסל");
        d5.put("capital", "אתונה");
        d6.put("capital", "קנברה");

        return new ArrayList<HashMap<String, Object>> (Arrays.asList(d1, d2, d3, d4, d5, d6));
    }
    */

}
