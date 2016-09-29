package com.example.zakiva.santa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zakiva.santa.Models.Generator;
import com.example.zakiva.santa.Models.TriviaQuestion;

import java.util.Random;

import static com.example.zakiva.santa.Helpers.Infra.*;

public class Trivia extends AppCompatActivity {

    private static TextView question;
    private static TextView answerA;
    private static TextView answerB;
    private static TextView answerC;
    private static TextView answerD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        question = ((TextView) findViewById(R.id.question));
        answerA = ((TextView) findViewById(R.id.answerA));
        answerB = ((TextView) findViewById(R.id.answerB));
        answerC = ((TextView) findViewById(R.id.answerC));
        answerD = ((TextView) findViewById(R.id.answerD));

        //small test
        addTriviaQuestion("1", "כמה פעמים זכתה ברזיל במונדיאל?", "5", "4", "5", "6", "7");
        displayTriviaQuestion("1");
    }

    public void answerClicked(View view) {
        Log.d(MainActivity.TAG, "view id =  " + view.getId());
        Log.d(MainActivity.TAG, "view id =  " + view.getTag().toString());

    }

    public void changeQuestionClicked (View view) {
        Generator generator = new Generator ();
        int n = new Random().nextInt(3);
        if (n == 0)
            loadQuestionToScreen(generator.bandToAlbum());
        else if (n == 1)
            loadQuestionToScreen(generator.countryToCapital());
        else
            loadQuestionToScreen(generator.bandToYear());
    }

    public static void loadQuestionToScreen (TriviaQuestion q) {
        question.setText(q.question.toString());
        answerA.setText(q.getAnswerA().toString());
        answerB.setText(q.getAnswerB().toString());
        answerC.setText(q.getAnswerC().toString());
        answerD.setText(q.getAnswerD().toString());
    }
}
