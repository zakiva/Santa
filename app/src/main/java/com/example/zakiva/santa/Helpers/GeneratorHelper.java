package com.example.zakiva.santa.Helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by zakiva on 9/28/16.
 */

public class GeneratorHelper {

    public static HashMap<String, Object> buildQuestionHashFromData(ArrayList<HashMap<String, Object>> data, String questionKey, String answerKey) {
        HashMap<String, Object> result = new HashMap<>();
        Collections.shuffle(data);
        HashMap<String, Object>  rightAnswerHash = data.get(0);
        result.put("question", (String) rightAnswerHash.get(questionKey));
        String rightAnswer = chooseStringFromField(rightAnswerHash, answerKey);
        result.put("rightAnswer", rightAnswer);
        ArrayList<String> answers = new ArrayList<>();
        answers.add(rightAnswer);

        int i = 1;

        while (answers.size() < 4 && i < data.size()) {
            HashMap<String, Object>  answerHash = data.get(i);
            String answer = chooseStringFromField(answerHash, answerKey);
            if (!(answers.contains(answer)))
                answers.add(answer);
            i++;
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
