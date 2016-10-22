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

    public static TriviaQuestion generateQuestionWithData (ArrayList<HashMap<String, Object>> data, String q, String questionKey, String answerKey) {

        Log.d(TAG, "generateQuestionWithData: " + q);

        HashMap<String, Object> questionHashFromData = GeneratorHelper.buildQuestionHashFromData(data, questionKey, answerKey);
        String q$ = (String) questionHashFromData.get("question");
        String right = (String) questionHashFromData.get("rightAnswer");
        ArrayList <String> answers = (ArrayList<String>) questionHashFromData.get("answers");

        q = q.replace("#$#", q$);

        TriviaQuestion question = new TriviaQuestion("someKey", q, right, answers.get(0), answers.get(1), answers.get(2), answers.get(3));
        return question;
    }

    public static HashMap<String, Object> buildQuestionHashFromData(ArrayList<HashMap<String, Object>> data, String questionKey, String answerKey) {
        HashMap<String, Object> result = new HashMap<>();
        Collections.shuffle(data);
        HashMap<String, Object>  rightAnswerHash = data.get(0);
        result.put("question", (String) rightAnswerHash.get(questionKey));
        String rightAnswer = chooseStringFromField(rightAnswerHash, answerKey);
        result.put("rightAnswer", rightAnswer);
        ArrayList<String> answers = new ArrayList<>();
        answers.add(rightAnswer);

        int i = 1, k = 0;

        while (answers.size() < 4) {
            HashMap<String, Object>  answerHash = data.get(i);
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

    public static String chooseStringFromField (HashMap<String, Object> hash, String key) {

        if (hash.get(key).getClass().equals(String.class))
            return (String) hash.get(key);

        ArrayList<String> lst = (ArrayList<String>) hash.get(key);
        Collections.shuffle(lst);
        return lst.get(0);
    }


    public static void printSheet (String name, ArrayList<HashMap<String, Object>> sheet) {
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
    public static ArrayList<String> getNumericAnswers(int n) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            int test = n - 10 + (int) (Math.random() * (n + 10));
            if (test > 4)
                list.add(String.valueOf(test));

            else
                list.add(0, String.valueOf(test + 1));
            list.add(1, String.valueOf(test + 2));
            list.add(2, String.valueOf(test + 3));
        }

        Collections.shuffle(list);

        return list;
    }

    public static ArrayList<TriviaQuestion> generateQuestionsArray(int number_of_questions) {
        Log.d(MainActivity.TAG, "generateQuestionsArray");

        int ARRAY_SIZE = number_of_questions * 2;
        //IMPORTANT: when adding new generators must update this number:
        int NUMBER_OF_GENERATORS = 5;
        List <Integer> numbers = new ArrayList<>();
        for(int i = 0; i < NUMBER_OF_GENERATORS; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        ArrayList<TriviaQuestion> array = new ArrayList<>();
        Generator generator = new Generator();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            Log.d(MainActivity.TAG, "generateQuestionsArray:  i,  random number = " + i + "," + numbers.get(i % NUMBER_OF_GENERATORS));
            //add new generators down here AND update NUMBER_OF_GENERATORS above.
            switch (numbers.get(i % NUMBER_OF_GENERATORS))  {
                case 0: array.add(generator.bandToAlbum()); break;
                case 1: array.add(generator.bandToYear()); break;
                case 2: array.add(generator.countryToCapital()); break;
                case 3: array.add(generator.inventionToInventor()); break;
                case 4: array.add(generator.gene1()); break;

            }
        }
        Log.d(MainActivity.TAG, "generateQuestionsArray:  array.size = "+array.size());

        return array;
    }
}
