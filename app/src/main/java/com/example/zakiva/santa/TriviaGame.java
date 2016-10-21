package com.example.zakiva.santa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zakiva.santa.Models.Generator;
import com.example.zakiva.santa.Models.TriviaQuestion;

import java.util.ArrayList;
import java.util.Collections;

public class TriviaGame extends AppCompatActivity {
    private static TextView quest;
    private static TextView answer1;
    private static TextView answer2;
    private static TextView answer3;
    private static TextView answer4;
    private RelativeLayout layout;
    private static int NUMBER_OF_QUESTIONS=5;
    private static int wrongCount;
    private static int index;
    private static ArrayList<TriviaQuestion> QuestionsArray = getQuestArray();
    private Chronometer clock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_game);
        wrongCount = 0;
        index = 0;
        quest = ((TextView)findViewById(R.id.quest));
        answer1 = ((TextView)findViewById(R.id.answer1));
        answer2 = ((TextView)findViewById(R.id.answer2));
        answer3 = ((TextView)findViewById(R.id.answer3));
        answer4 = ((TextView)findViewById(R.id.answer4));
        clock = (Chronometer) findViewById(R.id.clock);
        layout = (RelativeLayout) findViewById(R.id.layout);

        nextQuestion(0);
        clock.setBase(SystemClock.elapsedRealtime());
        clock.start();
    }

    public void answerClicked(final View view) {
        answer1.setClickable(false);
        answer2.setClickable(false);
        answer3.setClickable(false);
        answer4.setClickable(false);
        final TextView b = (TextView) view;
        String text = b.getText().toString();
        Log.d(MainActivity.TAG, "flash =" + index);
        Log.d(MainActivity.TAG, "array size = " + QuestionsArray.size());
        Log.d(MainActivity.TAG, "correct = "+QuestionsArray.get(index).correctAnswer);
        Log.d(MainActivity.TAG, "my pick=" + text);
        if (QuestionsArray.get(index).getCorrectAnswer().equals(text)) {
            view.setBackgroundColor(Color.GREEN);
        } else {
            view.setBackgroundColor(Color.RED);
            changeToGreen();
            wrongCount++;
        }
        index++;
        nextQuestion(2000);
    }

    public void nextQuestion(int delay) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (index == NUMBER_OF_QUESTIONS) {
                    clock.stop();
                    sendScore();
                } else {

                    try {
                        Log.d(MainActivity.TAG, "got here!");
                        answer1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        answer2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        answer3.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        answer4.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        answer1.setClickable(true);
                        answer2.setClickable(true);
                        answer3.setClickable(true);
                        answer4.setClickable(true);
                        loadQuestionToScreen(QuestionsArray.get(index));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }, delay);
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
    public static ArrayList<TriviaQuestion> getQuestArray() {
        ArrayList<TriviaQuestion> a = new ArrayList<>();
        Generator gen = new Generator();
        a.add(gen.gene1());
        a.add(gen.gene2());
        a.add(gen.gene3());
        a.add(gen.gene4());
        a.add(gen.gene1());
        a.add(gen.gene2());
        a.add(gen.gene3());
        a.add(gen.gene4());
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
    private void changeToGreen() {
        for (int i = 0; i < layout.getChildCount(); i++) {
            TextView v = (TextView)layout.getChildAt(i);
            String text1 = v.getText().toString();
            if (QuestionsArray.get(index).getCorrectAnswer().equals(text1)) {
                Log.d(MainActivity.TAG, "yayyyy");
                v.setBackgroundColor(Color.GREEN);
            }
        }
    }
}