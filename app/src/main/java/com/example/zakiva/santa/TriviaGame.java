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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zakiva.santa.Helpers.GeneratorHelper;
import com.example.zakiva.santa.Models.TriviaQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TriviaGame extends AppCompatActivity {
    private static TextView quest;
    private static TextView answer1;
    private static TextView answer2;
    private static TextView answer3;
    private static TextView answer4;
    private Button freeze;
    private RelativeLayout layout;
    private static int NUMBER_OF_QUESTIONS = 5;
    private static int wrongCount;
    private static int index;
    private static ArrayList<TriviaQuestion> questionsArray;
    private static final int FREEZE_TIME = 5000;
    private static long timeWhenStopped=0;
    private Chronometer clock;
    public static HashMap<String, ArrayList<HashMap<String,Object>>> dataHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_game);
        questionsArray = getQuestArray();
        wrongCount = 0;
        index = 0;
        quest = ((TextView) findViewById(R.id.quest));
        answer1 = ((TextView) findViewById(R.id.answer1));
        answer2 = ((TextView) findViewById(R.id.answer2));
        answer3 = ((TextView) findViewById(R.id.answer3));
        answer4 = ((TextView) findViewById(R.id.answer4));
        clock = (Chronometer) findViewById(R.id.clock);
        layout = (RelativeLayout) findViewById(R.id.layout);
        freeze = (Button)findViewById(R.id.freeze);

        nextQuestion(0);
        clock.setBase(SystemClock.elapsedRealtime());
        clock.start();
    }

    public static void addSheetToDataHash (String name, ArrayList<HashMap<String,Object>> data) {
        dataHash.put(name, data);
        Log.d(MainActivity.TAG, "TRIVIAGAME: putting in dataHash -> name: " + name);
    }

    public void answerClicked(final View view) {

        answer1.setClickable(false);
        answer2.setClickable(false);
        answer3.setClickable(false);
        answer4.setClickable(false);
        final TextView b = (TextView) view;
        String text = b.getText().toString();
        Log.d(MainActivity.TAG, "flash =" + index);
        Log.d(MainActivity.TAG, "array size = " + questionsArray.size());
        Log.d(MainActivity.TAG, "correct = " + questionsArray.get(index).correctAnswer);
        Log.d(MainActivity.TAG, "my pick=" + text);
        if (questionsArray.get(index).getCorrectAnswer().equals(text)) {
            changeToGray(view);
            changeToGreenDelayed(view);
        } else {
            changeToGray(view);
            changeToRedDelayed(view);
            changeToGreen();
            wrongCount++;
        }
        index++;
        nextQuestion(3000);
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
                        answer1.setVisibility(View.VISIBLE);
                        answer2.setVisibility(View.VISIBLE);
                        answer3.setVisibility(View.VISIBLE);
                        answer4.setVisibility(View.VISIBLE);
                        Log.d(MainActivity.TAG, "got here!");
                        answer1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        answer2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        answer3.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        answer4.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        answer1.setClickable(true);
                        answer2.setClickable(true);
                        answer3.setClickable(true);
                        answer4.setClickable(true);
                        loadQuestionToScreen(questionsArray.get(index));
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
        startActivity(intent);
    }

    public long calScore() {
        long elapsedMillis = ((SystemClock.elapsedRealtime() - clock.getBase()) / 1000);
        long score = 1000 - (wrongCount * 100) - elapsedMillis;
        Log.d(MainActivity.TAG, "score = " + score);
        Log.d(MainActivity.TAG, "seconds = " + elapsedMillis);
        Log.d(MainActivity.TAG, "mistakes = " + wrongCount);
        return score < 0 ? 0 : score;
    }

    public static ArrayList<TriviaQuestion> getQuestArray() {
        ArrayList<TriviaQuestion> a = GeneratorHelper.generateQuestionsArray();
        Log.d(MainActivity.TAG, "getQuestArray:  a.size = "+a.size());
        return a;
    }

    public void loadQuestionToScreen(TriviaQuestion m) {
        Log.d(MainActivity.TAG, "loadtoscren  ");
        Log.d(MainActivity.TAG, "queston  " + m.getQuestion());
        quest.setText(m.question);
        answer1.setText(m.getAnswerA());
        answer2.setText(m.getAnswerB());
        answer3.setText(m.getAnswerC());
        answer4.setText(m.getAnswerD());
    }
    public ArrayList<TextView> getAnswerArray(){
        ArrayList<TextView> a = new ArrayList<>();
        a.add(answer1);
        a.add(answer2);
        a.add(answer3);
        a.add(answer4);
        return a;
    }

    public void changeToGray(View view) {
        ArrayList<TextView> ans=getAnswerArray();
        for (TextView t :ans){
            if (t!=view){
                t.setBackgroundColor(Color.GRAY);
            }
        }
    }
    public void changeToRedDelayed(final View view){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                view.setBackgroundColor(Color.RED);
            }
        }, 650);
    }
    public void changeToGreenDelayed(final View view){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                view.setBackgroundColor(Color.GREEN);
            }
        }, 1000);
    }
    public void changeToGreen() {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final TextView v = (TextView) layout.getChildAt(i);
            String text1 = v.getText().toString();
            if (questionsArray.get(index).getCorrectAnswer().equals(text1)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                Log.d(MainActivity.TAG, "yayyyy");
                v.setBackgroundColor(Color.GREEN);
                    }
                }, 1000);
            }
        }
    }

    public void fiftyFifty(View view) {
        TriviaQuestion q = questionsArray.get(index);
        ArrayList<TextView> a=getAnswerArray();
        int m=0;
        for (TextView v : a) {
            if (v.getText().toString().equals(q.getCorrectAnswer())) {
               m = a.indexOf(v);
            }
        }
        a.remove(m);
        int g = new Random().nextInt(3);
        a.remove(g);
        for (TextView v : a) {
            v.setVisibility(View.INVISIBLE);
        }
        view.setClickable(false);
        }

    public void freezeGame(View view) {
        timeWhenStopped = clock.getBase() - SystemClock.elapsedRealtime();
        clock.stop();
        freeze.setClickable(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(MainActivity.TAG, "timewhenstop= "+timeWhenStopped);
                Log.d(MainActivity.TAG, "base= "+clock.getBase());
                Log.d(MainActivity.TAG, "system= "+SystemClock.elapsedRealtime());
                clock.setBase(SystemClock.elapsedRealtime() +timeWhenStopped);
                clock.start();
            }
        }, FREEZE_TIME);
    }

    public void skipQuest(View view) {
        index++;
        NUMBER_OF_QUESTIONS++;
        nextQuestion(1000);
        Log.d(MainActivity.TAG, "AVADA KADBRA!"+NUMBER_OF_QUESTIONS);
        view.setClickable(false);
    }
}
