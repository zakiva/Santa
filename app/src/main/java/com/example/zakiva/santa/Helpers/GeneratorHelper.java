package com.example.zakiva.santa.Helpers;

import android.util.Log;

import com.example.zakiva.santa.MainActivity;
import com.example.zakiva.santa.Models.EnglishGenerator;
import com.example.zakiva.santa.Models.Generator;
import com.example.zakiva.santa.Models.TriviaQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * Created by zakiva on 9/28/16.
 */

public class GeneratorHelper {

    public static final String TAG = ">>>>>>>Debug: ";

    private static int runIndex = 1;

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

    public static TriviaQuestion maxGenerateQuestionWithData(ArrayList<HashMap<String, Object>> data, String q, String questionKey, String answerKey, boolean maxMin) {
        Log.d(MainActivity.TAG, "data = " + data);
        HashMap<String, Object> questionHashFromData = GeneratorHelper.maxBuildQuestion(data, questionKey, answerKey, maxMin);
        String right = (String) questionHashFromData.get("rightAnswer");
        ArrayList<String> answers = (ArrayList<String>) questionHashFromData.get("answers");

        return new TriviaQuestion("someKey", q, right, answers.get(0), answers.get(1), answers.get(2), answers.get(3), false);
    }

    public static HashMap<String, Object> maxBuildQuestion(ArrayList<HashMap<String, Object>> data, String questionKey, String answerKey, boolean maxMin) {
        ArrayList<HashMap<String, String>> fourAnswers = new ArrayList<>();
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<String> answers = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        Collections.shuffle(data);
        int i = 0, k =1;
        while (set.size() < 4) {
            HashMap<String, Object> answerHash = data.get(i);
            String name = (String) answerHash.get(questionKey);
            String number = (String) answerHash.get(answerKey);
            set.add(Integer.valueOf(number));
          if (set.size()==k) {
                HashMap<String,String> helper = new HashMap<>();
                helper.put("name", name);
                helper.put("number", number);
                fourAnswers.add(helper);
              k++;
            }
            i++;
            if (i == data.size()) {
                i = 1;
            }
        }
        Collections.sort(fourAnswers, new Comparator<HashMap<String, String>>() {
                    @Override
                    public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
                        if (Integer.valueOf(t1.get("number")) > Integer.valueOf(t2.get("number"))) {
                            return 1;
                        } else if (Integer.valueOf(t1.get("number")) < Integer.valueOf(t2.get("number"))) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
        );
        Log.d(MainActivity.TAG, "four answers = " + fourAnswers);
        String rightAnswer;
        if (maxMin) {
            rightAnswer = fourAnswers.get(3).get("name");
        } else {
            rightAnswer = fourAnswers.get(0).get("name");
        }
        Log.d(MainActivity.TAG, "right answer = " + rightAnswer);
        result.put("rightAnswer", rightAnswer);
            for (HashMap ins : fourAnswers) {
                answers.add((String) ins.get("name"));
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

    public static ArrayList<TriviaQuestion> generateQuestionsArray(int number_of_questions, String [] sheetsNames) {
        Log.d(MainActivity.TAG, "generateQuestionsArray");

        /*
        int ARRAY_SIZE = number_of_questions * 2;
        //IMPORTANT: when adding new generators must update this number:
        int NUMBER_OF_GENERATORS = 19;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_GENERATORS; i++) {
            numbers.add(i);
        }
        for (int i=0; i<5; i++){
            Collections.shuffle(numbers);
        }
        */

        ArrayList<TriviaQuestion> array = new ArrayList<>();
        Generator generator = new Generator();
        //int indexForRandom, randomNumber;
        //HashSet <Integer> indexes = new HashSet<>();
        //int safe = 0;

        for (int i = 0; i < number_of_questions; i++) {
            Log.d(MainActivity.TAG, "AAA");
            //Log.d(MainActivity.TAG, "indexes size = " + indexes.size());


            /*
            do {
                Log.d(MainActivity.TAG, "BBB");
                randomNumber = new Random().nextInt(50);
                indexForRandom = (randomNumber + runIndex) % NUMBER_OF_GENERATORS;
                safe++;
            } while (indexes.contains(indexForRandom) && safe < 100);

            safe = 0;

            indexes.add(indexForRandom);
            */

            //checkRandom(numbers.get(indexForRandom));
            //Log.d(MainActivity.TAG, "generateQuestionsArray:  i,  random number = " + i + "," + numbers.get((indexForRandom)));
            //add new generators down here AND update NUMBER_OF_GENERATORS above.

            //NEED TO ADD: "timeZones", "mountains" ?


            switch (sheetsNames[i]) {
                case "israelBands" : array.add(generator.groupGeneratorsIsraelBands());break;
                case "worldBands" : array.add(generator.groupGeneratorsWorldBands());break;
                case "authors" : array.add(generator.groupGeneratorsAuthors());break;
                case "brands" : array.add(generator.groupGeneratorsBrands());break;
                case "championships" : array.add(generator.groupGeneratorsChampionships());break;
                case "inventions" : array.add(generator.groupGeneratorsInventions());break;
                case "israelEvents" : array.add(generator.groupGeneratorsIsraelEvents());break;
                case "singers" : array.add(generator.groupGeneratorsSingers());break;
                case "countries" : array.add(generator.groupGeneratorsCountries());break;
                case "latitudes" : array.add(generator.groupGeneratorsLatitudes());break;
                case "worldCups" : array.add(generator.groupGeneratorsWorldCups());break;
                case "wifeHusband" : array.add(generator.groupGeneratorsWifeToHusband());break;
                case "femaleActors" : array.add(generator.groupGeneratorsFemaleActors());break;
                case "maleActors" : array.add(generator.groupGeneratorsMaleActors());break;
                case "bibleFathers" : array.add(generator.sonToFather());break;
                case "quotes" : array.add(generator.quoteToPerson());break;
                case "worldLeaders" : array.add(generator.leaderToYears());break;
                case "leadersYears" : array.add(generator.yearsToLeader());break;
                case "apps" : array.add(generator.groupGeneratorsApps());break;
                case "cars" : array.add(generator.carToCountry());break;
            }
        }
        Log.d(MainActivity.TAG, "generateQuestionsArray:  array.size = " + array.size());
        runIndex++;
        //printRandomCheck(); // for debugging the random
        return array;
    }
    public static ArrayList<TriviaQuestion> generateEnglishQuestionsArray(int number_of_questions, String [] sheetsNames) {
        Log.d(MainActivity.TAG, "generateEnglishQuestionsArray");

        ArrayList<TriviaQuestion> array = new ArrayList<>();
        EnglishGenerator generator = new EnglishGenerator();

        for (int i = 0; i < number_of_questions; i++) {
            Log.d(MainActivity.TAG, "AAA");
            switch (sheetsNames[i]) {
                case "worldBandsEnglish" : array.add(generator.groupGeneratorsWorldBands());break;
                case "brandsEnglish" : array.add(generator.groupGeneratorsBrands());break;
                case "inventionsEnglish" : array.add(generator.groupGeneratorsInventions());break;
                case "latitudesEnglish" : array.add(generator.groupGeneratorsLatitudes());break;
                case "worldCupsEnglish" : array.add(generator.groupGeneratorsWorldCups());break;
                case "wifeHusbandEnglish" : array.add(generator.groupGeneratorsWifeToHusband());break;
                case "actorsEnglish" : array.add(generator.groupGeneratorsActors());break;
                case "quotesEnglish" : array.add(generator.quoteToPerson());break;
                case "worldLeadersEnglish" : array.add(generator.leaderToYears());break;
                case "leadersYearsEnglish" : array.add(generator.yearsToLeader());break;
                case "appsEnglish" : array.add(generator.groupGeneratorsApps());break;
                case "carsEnglish" : array.add(generator.carToCountry());break;
            }
        }
        Log.d(MainActivity.TAG, "generateEnglishQuestionsArray:  array.size = " + array.size());
        runIndex++;
        //printRandomCheck(); // for debugging the random
        return array;
    }

    // ########## random checker - start ##############

    private static HashMap<Integer,Integer> randomChecker;
    public static void checkRandom (int x) {
        if (randomChecker == null)
            randomChecker = new HashMap<Integer, Integer>();
        if (randomChecker.get(x) != null)
            randomChecker.put(x, randomChecker.get(x) + 1);
        else
            randomChecker.put(x, 1);
    }

    public static void printRandomCheck () {
        Log.d(MainActivity.TAG, "runIndex = " + runIndex);

        int[] result = new int[randomChecker.values().toArray().length];
        for (int i = 0; i < randomChecker.values().toArray().length; i++) {
            result[i] = ((Integer)(randomChecker.values().toArray())[i]).intValue();
        }
        sortValue(randomChecker);
    }

    public static void sortValue(HashMap<?, Integer> t){
        //Transfer as List and sort it
        ArrayList<Map.Entry<?, Integer>> l = new ArrayList(t.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<?, Integer>>(){

            public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});

        Log.d(MainActivity.TAG, "randomChecker = " + l);
    }

    // ########## random checker  - end ##############

}

