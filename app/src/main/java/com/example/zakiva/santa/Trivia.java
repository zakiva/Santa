package com.example.zakiva.santa;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zakiva.santa.Models.Generator;
import com.example.zakiva.santa.Models.TriviaQuestion;
import com.example.zakiva.santa.Testers.TriviaTester;

import java.util.Random;
import java.util.concurrent.TimeUnit;

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

    public void testRandomQuestionsClicked (View view) {
        final Button b = (Button) findViewById(R.id.test);
        b.setBackgroundColor(Color.YELLOW);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable()
        {
            public void run()
            {
                try {
                    TriviaTester.runRandomQuestions(b);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    public static void loadQuestionToScreen (TriviaQuestion q) {
        Log.d(MainActivity.TAG, "loadtoscren  ");
        Log.d(MainActivity.TAG, "queston  " + q.getQuestion());
        question.setText(q.question.toString());
        answerA.setText(q.getAnswerA().toString());
        answerB.setText(q.getAnswerB().toString());
        answerC.setText(q.getAnswerC().toString());
        answerD.setText(q.getAnswerD().toString());
    }
}
