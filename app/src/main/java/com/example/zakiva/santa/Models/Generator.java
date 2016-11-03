package com.example.zakiva.santa.Models;

import com.example.zakiva.santa.Helpers.GeneratorHelper;
import com.example.zakiva.santa.TriviaGame;

import java.util.ArrayList;
import java.util.List;

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
        String sidra =pro.toString().replace("[", "").replace("]", "").trim();    ;
        return new TriviaQuestion("30", "בסדרה "+sidra+" מה המספר הבא ?",s,s, getNumericAnswers(n,5).get(0), getNumericAnswers(n,5).get(1), getNumericAnswers(n,5).get(2));
    }
    public TriviaQuestion minusSeq(){
        ArrayList<Integer> pro = GeneratorHelper.returnSidra(21, "minusSeq");
        int n = pro.get(3);
        String s = String.valueOf(pro.get(3));
        pro.remove(3);
        String sidra =pro.toString().replace("[", "").replace("]", "").trim();    ;
        return new TriviaQuestion("31", "בסדרה "+sidra+" מה המספר הבא ?", s,getNumericAnswers(n,5).get(0), getNumericAnswers(n,5).get(1), getNumericAnswers(n,5).get(2),s);
    }
    public TriviaQuestion plusPlusSeq(){
        ArrayList<Integer> pro = GeneratorHelper.returnSidra(21, "plusPlusSeq");
        int n = pro.get(3);
        String s = String.valueOf(pro.get(3));
        pro.remove(3);
        String sidra =pro.toString().replace("[", "").replace("]", "").trim();    ;
        return new TriviaQuestion("32", "בסדרה "+sidra+" מה המספר הבא ?", s,getNumericAnswers(n,5).get(0),s, getNumericAnswers(n,5).get(1), getNumericAnswers(n,5).get(2));
    }
    public TriviaQuestion minusMinusSeq() {
        ArrayList<Integer> pro = GeneratorHelper.returnSidra(21, "minusMinusSeq");
        int n = pro.get(3);
        String s = String.valueOf(pro.get(3));
        pro.remove(3);
        String sidra =pro.toString().replace("[", "").replace("]", "").trim();    ;
       return new TriviaQuestion("33", "בסדרה "+sidra+" מה המספר הבא ?", s, s, getNumericAnswers(n,5).get(0), getNumericAnswers(n,5).get(1),getNumericAnswers(n,5).get(2));
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
        String s  =String.valueOf(pro.get(3));
        pro.remove(3);
        String sidra =pro.toString().replace("[", "").replace("]", "").trim();    ;
        return new TriviaQuestion("33", "בסדרה זו מה המספר הבא "+sidra+" ?", s,getNumericAnswers(n,5).get(0), getNumericAnswers(n,5).get(1),s, getNumericAnswers(n,5).get(2));
    }
    public TriviaQuestion bandToAlbum () {
        String [] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("israelBands"), "איזה מהאלבומים הבאים שייך ללהקת #0#?", q, "albums");
    }

    public TriviaQuestion bandToYear () {
        String [] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("worldBands"), "באיזו שנה הוקמה להקת #0#?", q, "year");
    }

    public TriviaQuestion countryToCapital () {
        String [] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("countries"), "מהי עיר הבירה של #0#?", q, "capital");
    }


    public TriviaQuestion inventionToInventor() {
        String [] q = {"name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("inventions"), "מי המציא את #0#?", q, "inventor");
    }

    public TriviaQuestion maleActorToCharacter() {
        String [] q = {"actor", "type", "name"};
        return GeneratorHelper.generateQuestionWithData(TriviaGame.dataHash.get("maleActors"), "איזו דמות גילם #0# ב#1# #2#?", q, "character");
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
