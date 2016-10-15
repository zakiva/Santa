package com.example.zakiva.santa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.zakiva.santa.Models.TriviaQuestion;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.zakiva.santa.Models.Generator.gene1;
import static com.example.zakiva.santa.Models.Generator.gene2;
import static com.example.zakiva.santa.Models.Generator.gene3;
import static com.example.zakiva.santa.Models.Generator.gene4;

public class TriviaGame extends AppCompatActivity {
    private static TextView quest;
    private static TextView answer1;
    private static TextView answer2;
    private static TextView answer3;
    private static TextView answer4;
    private static Button startBtn;
    private static int count = 0;
    private static int wrongCount=0;
    private static int flash = 0;
    private static ArrayList<TriviaQuestion> boom = questArray();
    private Chronometer clock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_game);
        quest = ((TextView) findViewById(R.id.quest));
        answer1 = ((TextView) findViewById(R.id.answer1));
        answer2 = ((TextView) findViewById(R.id.answer2));
        answer3 = ((TextView) findViewById(R.id.answer3));
        answer4 = ((TextView) findViewById(R.id.answer4));
        startBtn = (Button) findViewById(R.id.startBtn);
        clock = (Chronometer) findViewById(R.id.clock);
    }

    public void startGameClicked(View view) {
        loadQuestionToScreen((questArray().get(0)));
        clock.setBase(SystemClock.elapsedRealtime());
        clock.start();
        startBtn.setVisibility(View.INVISIBLE);
    }

    public void answerClicked(final View view) {
        final TextView b = (TextView) view;
        String text = b.getText().toString();
        Log.d(MainActivity.TAG, "flash =" + flash);
        Log.d(MainActivity.TAG, "array size = " + boom.size());
        if (flash == 3) {
            sendScore();
            Log.d(MainActivity.TAG, "success");
        }
        Log.d(MainActivity.TAG, "correct = "+boom.get(flash).correctAnswer);
        Log.d(MainActivity.TAG, text);
        if (boom.get(flash).getCorrectAnswer() == text) {
            count++;
            flash++;
            view.setBackgroundColor(Color.GREEN);
            changeColor(view);
        } else {
            wrongCount++;
            flash++;
            view.setBackgroundColor(Color.RED);
            changeColor(view);
        }
    }
    public void changeColor(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    loadQuestionToScreen(boom.get(flash));
                    Log.d(MainActivity.TAG, "got here!");
                    view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1500);
    }

    public void sendScore() {
        final Intent intent = new Intent(TriviaGame.this, Score.class);
        intent.putExtra("score", calScore());
        intent.putExtra("game", "trivia");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
            }
        }, 2000);
    }
    public Long calScore(){
        long elapsedMillis = ((SystemClock.elapsedRealtime() - clock.getBase()) / 1000);
        long score = 1000-(wrongCount*100)-elapsedMillis;
        Log.d(MainActivity.TAG, "score = "+score);
        Log.d(MainActivity.TAG, "seconds = "+elapsedMillis);
        Log.d(MainActivity.TAG, "mistakes = "+wrongCount);
        return score < 0 ? 0 : score;
    }
    public static ArrayList<TriviaQuestion> questArray() {
        ArrayList<TriviaQuestion> a = new ArrayList<>();
        a.add(gene1());
        a.add(gene2());
        a.add(gene3());
        a.add(gene4());
        a.add(gene1());
        a.add(gene2());
        a.add(gene3());
        a.add(gene4());
        Collections.shuffle(a);
        return a;
    }

    public static void loadQuestionToScreen(TriviaQuestion m) {
        Log.d(MainActivity.TAG, "loadtoscren  ");
        Log.d(MainActivity.TAG, "queston  " + m.getQuestion());
        quest.setText(m.question);
        answer1.setText(m.getAnswerA());
        answer2.setText(m.getAnswerB());
        answer3.setText(m.getAnswerC());
        answer4.setText(m.getAnswerD());
    }
}