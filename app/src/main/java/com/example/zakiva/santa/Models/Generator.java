package com.example.zakiva.santa.Models;

import com.example.zakiva.santa.Helpers.GeneratorHelper;
import com.example.zakiva.santa.Trivia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by zakiva on 9/28/16.
 */

public class Generator {

    public TriviaQuestion bandToAlbum () {

        //this is just an example of data - to be removed
        ArrayList<HashMap<String, Object>> dataExample = prepareBandsData();

        HashMap<String, Object> questionHashFromData = GeneratorHelper.buildQuestionHashFromData(dataExample, "name", "albums");
        String name = (String) questionHashFromData.get("question");
        String right = (String) questionHashFromData.get("rightAnswer");
        ArrayList <String> answers = (ArrayList<String>) questionHashFromData.get("answers");

        String q = "איזה מהאלבומים הבאים שייך ללהקת " + name + "?";

        TriviaQuestion question = new TriviaQuestion("someKey", q, right, answers.get(0), answers.get(1), answers.get(2), answers.get(3));
        return question;
    }

    public TriviaQuestion countryToCapital () {

        //this is just an example of data - to be removed
        ArrayList<HashMap<String, Object>> dataExample = prepareCountriesData();

        HashMap<String, Object> questionHashFromData = GeneratorHelper.buildQuestionHashFromData(dataExample, "name", "capital");
        String name = (String) questionHashFromData.get("question");
        String right = (String) questionHashFromData.get("rightAnswer");
        ArrayList <String> answers = (ArrayList<String>) questionHashFromData.get("answers");

        String q = "מהי עיר הבירה של " + name + "?";

        TriviaQuestion question = new TriviaQuestion("someKey", q, right, answers.get(0), answers.get(1), answers.get(2), answers.get(3));
        return question;
    }

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





}
