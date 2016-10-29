package com.example.zakiva.santa.Helpers;

import android.util.Log;

import com.example.zakiva.santa.MainActivity;
import com.example.zakiva.santa.Models.Generator;
import com.example.zakiva.santa.Models.TriviaQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zakiva on 9/28/16.
 */

public class GeneratorHelper {

    public static final String TAG = ">>>>>>>Debug: ";

    public static TriviaQuestion generateQuestionWithData(ArrayList<HashMap<String, Object>> data, String q, String[] questionKeys, String answerKey) {

        Log.d(TAG, "generateQuestionWithData: " + q);

        HashMap<String, Object> questionHashFromData = GeneratorHelper.buildQuestionHashFromData(data, questionKeys, answerKey);
        String right = (String) questionHashFromData.get("rightAnswer");
        ArrayList<String> answers = (ArrayList<String>) questionHashFromData.get("answers");

        String q$;

        for (int i = 0; i < questionKeys.length; i++) {
            q$ = (String) questionHashFromData.get("question" + i);
            q = q.replace("#" + i + "#", q$);
        }
        boolean male = false;
        if (questionHashFromData.get("male") != null) {
            if (Integer.parseInt(questionHashFromData.get("male").toString()) == 1)
                male = true;
        }
        TriviaQuestion question = new TriviaQuestion("someKey", q, right, answers.get(0), answers.get(1), answers.get(2), answers.get(3), male);
        return question;
    }

    public static HashMap<String, Object> buildQuestionHashFromData(ArrayList<HashMap<String, Object>> data, String[] questionKeys, String answerKey) {
        HashMap<String, Object> result = new HashMap<>();
        Collections.shuffle(data);
        HashMap<String, Object> rightAnswerHash = data.get(0);

        for (int i = 0; i < questionKeys.length; i++) {
            result.put("question" + i, (String) rightAnswerHash.get(questionKeys[i]));
        }

        String rightAnswer = chooseStringFromField(rightAnswerHash, answerKey);
        result.put("rightAnswer", rightAnswer);
        result.put("male", rightAnswerHash.get("male"));
        ArrayList<String> answers = new ArrayList<>();
        answers.add(rightAnswer);

        int i = 1, k = 0;

        while (answers.size() < 4) {
            HashMap<String, Object> answerHash = data.get(i);
            String answer = chooseStringFromField(answerHash, answerKey);

            if (!(answers.contains(answer)))
                answers.add(answer);
            i++;
            k++;
            if (i == data.size())
                i = 1;
            if (k > 300) // if data is correct, this should never happen
                answers.add("XXX");
        }

        Collections.shuffle(answers);
        result.put("answers", answers);
        return result;
    }

    public static String chooseStringFromField(HashMap<String, Object> hash, String key) {

        if (hash.get(key).getClass().equals(String.class))
            return (String) hash.get(key);

        ArrayList<String> lst = (ArrayList<String>) hash.get(key);
        Collections.shuffle(lst);
        return lst.get(0);
    }


    public static void printSheet(String name, ArrayList<HashMap<String, Object>> sheet) {
        Log.d(TAG, "#####################SHEET NAME = " + name + "#####################");
        for (HashMap<String, Object> hash : sheet) {
            printHash(hash);
        }
    }

    public static void printHash(HashMap<String, Object> hash) {
        String line = "";
        for (String key : hash.keySet()) {
            line = line + ", " + key + ":" + hash.get(key);
        }
        Log.d(TAG, line);
    }

    public static ArrayList<String> getNumericAnswers(int n, int diff) {
        ArrayList<String> list = new ArrayList<>();
        int test = (n - diff) + (int) (Math.random() * (n + diff));
        for (int i = 0; i < 10; i++) {
            if (test == n) {
                test = (n - diff) + (int) (Math.random() * (n + diff));
            } else {
                list.add(String.valueOf(test + diff));
                list.add(String.valueOf(test - diff));
                list.add(String.valueOf(test));
            }
        }
        Collections.shuffle(list);
        return list;
    }

    public static ArrayList<Integer> returnSidra(int range, String type) {
        int a1 = (int) (Math.random() * range), diff = 1 + (int) (Math.random() * range), t = a1;
        ArrayList<Integer> seq = new ArrayList<Integer>();
        seq.add(a1);
        while (seq.size() < 4) {
            switch (type) {
                case "plusSeq":
                    t = t + diff;
                    break;
                case "plusPlusSeq":
                    t = t + diff;
                    diff--;
                    break;
                case "minusSeq":
                    t = t - diff;
                    break;
                case "minusMinusSeq":
                    t = t + diff;
                    diff--;
            }
            seq.add(t);
        }
        return seq;
    }

    public static ArrayList<TriviaQuestion> generateQuestionsArray(int number_of_questions) {
        Log.d(MainActivity.TAG, "generateQuestionsArray");

        int ARRAY_SIZE = number_of_questions * 2;
        //IMPORTANT: when adding new generators must update this number:
        int NUMBER_OF_GENERATORS = 41;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_GENERATORS; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        ArrayList<TriviaQuestion> array = new ArrayList<>();
        Generator generator = new Generator();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            Log.d(MainActivity.TAG, "generateQuestionsArray:  i,  random number = " + i + "," + numbers.get(i % NUMBER_OF_GENERATORS));
            //add new generators down here AND update NUMBER_OF_GENERATORS above.
            switch (numbers.get(i % NUMBER_OF_GENERATORS)) {

                case 0:
                    array.add(generator.israelBandToAlbum());
                    break;
                case 1:
                    array.add(generator.worldBandToAlbum());
                    break;
                case 2:
                    array.add(generator.israelBandToYear());
                    break;
                case 3:
                    array.add(generator.worldBandToYear());
                    break;
                case 4:
                    array.add(generator.israelBandToMembers());
                    break;
                case 5:
                    array.add(generator.worldBandToMembers());
                    break;
                case 6:
                    array.add(generator.inventionToInventor());
                    break;
                case 7:
                    array.add(generator.inventionToCountry());
                    break;
                case 8:
                    array.add(generator.inventionToYear());
                    break;
                case 9:
                    array.add(generator.yearToInvention());
                    break;

                case 10:
                    array.add(generator.bookToAuthor());
                    break;
                case 11:
                    array.add(generator.authorToCountry());
                    break;

                case 12:
                    array.add(generator.sonToFather());
                    break;

                case 13:
                    array.add(generator.quoteToPerson());
                    break;

                case 14:
                    array.add(generator.wifeToHusband());
                    break;
                case 15:
                    array.add(generator.husbandToWife());
                    break;

                case 16:
                    array.add(generator.teamToChampionships());
                    break;

                case 17:
                    array.add(generator.hostToYear());
                    break;
                case 18:
                    array.add(generator.winnerToYear());
                    break;

                case 19:
                    array.add(generator.leaderToYears());
                    break;
                case 20:
                    array.add(generator.yearsToLeader());
                    break;

                case 21:
                    array.add(generator.brandToYear());
                    break;
                case 22:
                    array.add(generator.founderToBrand());
                    break;
                case 23:
                    array.add(generator.countryToBrand());
                    break;

                case 24:
                    array.add(generator.capitalToCountry());
                    break;
                case 25:
                    array.add(generator.coinToCountry());
                    break;
                case 26:
                    array.add(generator.countryToCapital());
                    break;
                case 27:
                    array.add(generator.countryToContinent());
                    break;

                case 28:
                    array.add(generator.songToSinger());
                    break;

                case 29:
                    array.add(generator.defenseMinisterToEvent());
                    break;
                case 30:
                    array.add(generator.generalToEvent());
                    break;
                case 31:
                    array.add(generator.primeMinisterToEvent());
                    break;
                case 32:
                    array.add(generator.eventToYear());
                    break;
                case 33:
                    array.add(generator.yearToEvent());
                    break;

                case 34:
                    array.add(generator.albumToSinger());
                    break;
                case 35:
                    array.add(generator.bornToSinger());
                    break;
                case 36:
                    array.add(generator.countryToSinger());
                    break;

                case 37:
                    array.add(generator.femaleActorToCharacter());
                    break;
                case 38:
                    array.add(generator.characterToFemaleActor());
                    break;
                case 39:
                    array.add(generator.maleActorToCharacter());
                    break;
                case 40:
                    array.add(generator.characterToMaleActor());
                    break;

            }
        }
        Log.d(MainActivity.TAG, "generateQuestionsArray:  array.size = " + array.size());

        return array;
    }
}