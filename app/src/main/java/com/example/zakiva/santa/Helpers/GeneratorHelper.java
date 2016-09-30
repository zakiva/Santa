package com.example.zakiva.santa.Helpers;

import android.util.Log;

import com.example.zakiva.santa.MainActivity;
import com.example.zakiva.santa.Models.TriviaQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by zakiva on 9/28/16.
 */

public class GeneratorHelper {

    public static TriviaQuestion generateQuestionWithData (ArrayList<HashMap<String, Object>> data, String q, String questionKey, String answerKey) {

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
}
