package com.example.zakiva.santa;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.GeneratorHelper;
import com.example.zakiva.santa.Helpers.Parser;
import com.example.zakiva.santa.Models.Generator;
import com.example.zakiva.santa.Models.TriviaQuestion;
import com.example.zakiva.santa.Testers.TriviaTester;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;

import static com.example.zakiva.santa.Helpers.Infra.*;

public class Trivia extends AppCompatActivity {

    private static TextView question;
    private static TextView answerA;
    private static TextView answerB;
    private static TextView answerC;
    private static TextView answerD;
    private Chronometer stopper;
    public static HashMap<String, ArrayList<HashMap<String,Object>>> dataHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        dataHash = new HashMap<>();
        getTriviaDataFromFirebase();

        question = ((TextView) findViewById(R.id.question));
        answerA = ((TextView) findViewById(R.id.answerA));
        answerB = ((TextView) findViewById(R.id.answerB));
        answerC = ((TextView) findViewById(R.id.answerC));
        answerD = ((TextView) findViewById(R.id.answerD));

        stopper = (Chronometer) findViewById(R.id.chronometer);
        //small test
        addTriviaQuestion("1", "כמה פעמים זכתה ברזיל במונדיאל?", "5", "4", "5", "6", "7",true);
        displayTriviaQuestion("1");
    }

    public static void addSheetToDataHash (String name, ArrayList<HashMap<String,Object>> data) {
        dataHash.put(name, data);
        Log.d(MainActivity.TAG, "putting in dataHash -> name: " + name);
    }
    public void parseClicked(View view) {
        try {
            Log.d(MainActivity.TAG, "start try =  ");
            int id = getResources().getIdentifier("sheets", "raw", this.getApplicationContext().getPackageName());
            InputStream raw = this.getApplicationContext().getResources().openRawResource(id);
            Parser p = new Parser();
            p.saveSheetToFirebase("micha", "latitudes", raw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void answerClicked(View view) {
        Log.d(MainActivity.TAG, "view id =  " + view.getId());
        Log.d(MainActivity.TAG, "view id =  " + view.getTag().toString());
    }
    public void changeQuestionClicked (View view) {

        //prints all the data hash
        for (String key : dataHash.keySet()) {
            GeneratorHelper.printSheet(key, dataHash.get(key));
        }

        Generator generator = new Generator ();


        loadQuestionToScreen(generator.hostToYear());
        /*

        int n = new Random().nextInt(3);
        if (n == 0)
            loadQuestionToScreen(generator.bandToAlbum());
        else if (n == 1)
            loadQuestionToScreen(generator.countryToCapital());
        else
            loadQuestionToScreen(generator.bandToYear());

            */
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

    public void startStopperClicked(View view) {
        stopper.start();
    }

    public void stopStopperClicked(View view) {
        stopper.stop();
    }
}
