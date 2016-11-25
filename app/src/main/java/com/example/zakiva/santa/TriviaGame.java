package com.example.zakiva.santa;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zakiva.santa.Helpers.GeneratorHelper;
import com.example.zakiva.santa.Helpers.Infra;
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
    private RelativeLayout activityBackground;
    private ImageView freeze;
    private ImageView fifty_fifty;
    private ImageView skip_quest;
    private RelativeLayout freezeBox;
    private RelativeLayout fiftyFiftyBox;
    private RelativeLayout skipQuestBox;
    private ImageView bonusRound;
    private ProgressBar freezeBar;
    private RelativeLayout layout;
    private static int NUMBER_OF_QUESTIONS;
    private int timeForGreen;
    private int timeForRed;
    private int timeForNextQuestion;
    private int timeForSkipQuestion;
    private int fiftyFiftyPrice;
    private int freezeGamePrice;
    private int skipQuestPrice;
    private TextView freezePriceTextView;
    private TextView fiftyFiftyPriceTextView;
    private TextView skipQuestPriceTextView;
    private static int wrongCount;
    private static int index;
    private static ArrayList<TriviaQuestion> questionsArray;
    private int timeForAnswer;
    private int timeForBonus;
    private static int FREEZE_TIME;
    private static long timeWhenStopped;
    private int mProgressStatus=0;
    private Chronometer clock;
    private TextView candiesTextView;
    public static HashMap<String, ArrayList<HashMap<String,Object>>> dataHash;
    private static HashMap<String,Boolean> enableHelpers,disableHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_game);
        activityBackground = (RelativeLayout) findViewById(R.id.activityBackground);
        activityBackground.getBackground().setAlpha(0);
        skipQuestPrice = 200;
        freezeGamePrice = 100;
        fiftyFiftyPrice = 150;
        NUMBER_OF_QUESTIONS = 5;
        timeWhenStopped = 0;
        timeForBonus = 5700;
        timeForAnswer = 2000;
        FREEZE_TIME = 5000;
        questionsArray = getQuestArray();
        wrongCount = 0;
        index = 0;
        timeForNextQuestion = 3000;
        timeForRed = 650;
        timeForGreen = 1000;
        timeForSkipQuestion=1000;
        enableHelpers = new HashMap<>();
        disableHelpers = new HashMap<>();
        initDisableEnable(disableHelpers, false, false, false);
        initDisableEnable(enableHelpers, true, true, true);
        Log.d(MainActivity.TAG, "enable = " + enableHelpers);
        Log.d(MainActivity.TAG, "disable = " + disableHelpers);
        quest = ((TextView) findViewById(R.id.quest));
        answer1 = ((TextView) findViewById(R.id.answer1));
        answer2 = ((TextView) findViewById(R.id.answer2));
        answer3 = ((TextView) findViewById(R.id.answer3));
        answer4 = ((TextView) findViewById(R.id.answer4));
        clock = (Chronometer) findViewById(R.id.clock);
        freeze = (ImageView) findViewById(R.id.freeze);
        fifty_fifty = (ImageView) findViewById(R.id.btn50);
        skip_quest = (ImageView) findViewById(R.id.skipBtn);
        bonusRound = (ImageView) findViewById(R.id.bonus);
        fiftyFiftyBox = (RelativeLayout) findViewById(R.id.fiftyBox);
        skipQuestBox = (RelativeLayout) findViewById(R.id.skipBox);
        freezeBox = (RelativeLayout) findViewById(R.id.freezeBox);
        candiesTextView = (TextView) findViewById(R.id.candiesNumber);
        freezePriceTextView = (TextView) findViewById(R.id.freezePrice);
        fiftyFiftyPriceTextView = (TextView) findViewById(R.id.fiftyFiftyPrice);
        skipQuestPriceTextView = (TextView) findViewById(R.id.skipQuestPrice);
        freezeBar = (ProgressBar) findViewById(R.id.circular_progress_bar);
        nextQuestion(0);
        clock.setBase(SystemClock.elapsedRealtime());
        clock.start();
        displayCandies();
        displayHelpersPrices();
        addFont();
    }

    public void displayHelpersPrices () {
        freezePriceTextView.setText(freezeGamePrice + "");
        fiftyFiftyPriceTextView.setText(fiftyFiftyPrice + "");
        skipQuestPriceTextView.setText(skipQuestPrice + "");
    }

    public static void addSheetToDataHash (String name, ArrayList<HashMap<String,Object>> data) {
        dataHash.put(name, data);
        Log.d(MainActivity.TAG, "TRIVIAGAME: putting in dataHash -> name: " + name);
    }

    public void answerClicked(final View view) {

        disableEnableViews(false, disableHelpers);
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
            changeToGreen(view);
            wrongCount++;
        }
        index++;
        if (index == NUMBER_OF_QUESTIONS - 1) {
            bonusRoundMethod();
        } else {
            nextQuestion(timeForNextQuestion);
        }
    }
    public void nextQuestion(int delay) {
        Log.d(MainActivity.TAG, "enable = " + enableHelpers);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (index == NUMBER_OF_QUESTIONS) {
                    clock.stop();
                    sendScore();
                }else{
                    try {
                        answer1.setVisibility(View.VISIBLE);
                        answer2.setVisibility(View.VISIBLE);
                        answer3.setVisibility(View.VISIBLE);
                        answer4.setVisibility(View.VISIBLE);
                        Log.d(MainActivity.TAG, "got here!");
                        answer1.setTextColor(Color.parseColor("#9254ff"));
                        answer2.setTextColor(Color.parseColor("#9254ff"));
                        answer3.setTextColor(Color.parseColor("#9254ff"));
                        answer4.setTextColor(Color.parseColor("#9254ff"));
                        disableEnableViews(true,enableHelpers);
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
        ArrayList<TriviaQuestion> a = GeneratorHelper.generateQuestionsArray(NUMBER_OF_QUESTIONS);
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
            if (t != view) {
                t.setTextColor(Color.parseColor("#dddddd"));
            }
        }
    }
    public void changeToRedDelayed(final View view){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                TextView tv = (TextView) view;
                tv.setTextColor(Color.parseColor("#f34444"));
            }
        }, timeForRed);
    }
    public void changeToGreenDelayed(final View view){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
TextView tv = (TextView) view;
            tv.setTextColor(Color.parseColor("#49c358"));}
        }, timeForGreen);
    }
    public void changeToGreen(View view) {
        ArrayList<TextView> ans = getAnswerArray();
        for (final TextView t : ans) {
            String text1 = t.getText().toString();
            if (questionsArray.get(index).getCorrectAnswer().equals(text1)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d(MainActivity.TAG, "yayyyy");
                        t.setTextColor(Color.parseColor("#49c358"));
                    }
                }, timeForGreen);
            }
        }
    }

    public void fiftyFifty(View view) {
        if (!updateCandies(fiftyFiftyPrice)) {
            notifyNoCandies();
            return;
        }
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
        enableHelpers.put("fifty",false);
        fifty_fifty.setBackgroundResource(R.drawable.split_disable);
        disableEnableViews(true,enableHelpers);
    }

    public void freezeGame(View view) {
        if (!updateCandies(freezeGamePrice)) {
            notifyNoCandies();
            return;
        }
        enableHelpers.put("freeze",false);
        freeze.setBackgroundResource(R.drawable.extra_time_disable);
        freezeProgressBar();
        disableEnableViews(true,enableHelpers);
        timeWhenStopped = clock.getBase() - SystemClock.elapsedRealtime();
        clock.stop();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(MainActivity.TAG, "timewhenstop= "+timeWhenStopped);
                Log.d(MainActivity.TAG, "base= "+clock.getBase());
                Log.d(MainActivity.TAG, "system= "+SystemClock.elapsedRealtime());
                clock.setBase(SystemClock.elapsedRealtime() +timeWhenStopped);
                clock.start();
                freezeBar.setVisibility(View.GONE);
            }
        }, FREEZE_TIME);
    }

    public void skipQuest(View view) {
        if (!updateCandies(skipQuestPrice)) {
            notifyNoCandies();
            return;
        }
        enableHelpers.put("skip",false);
        skip_quest.setBackgroundResource(R.drawable.next_disable);
        disableEnableViews(false,disableHelpers);
        index++;
        NUMBER_OF_QUESTIONS++;
        nextQuestion(timeForSkipQuestion);
        Log.d(MainActivity.TAG, "AVADA KADBRA!"+NUMBER_OF_QUESTIONS);
    }

    public void disableEnableViews(boolean flag, HashMap<String,Boolean> helpers){
        answer1.setClickable(flag);
        answer2.setClickable(flag);
        answer3.setClickable(flag);
        answer4.setClickable(flag);
        freeze.setClickable(helpers.get("freeze"));
        freezeBox.setClickable(helpers.get("freeze"));
        fifty_fifty.setClickable(helpers.get("fifty"));
        fiftyFiftyBox.setClickable(helpers.get("fifty"));
        skip_quest.setClickable(helpers.get("skip"));
        skipQuestBox.setClickable(helpers.get("skip"));
    }
    public void initDisableEnable(HashMap helpers,boolean fifty,boolean skip, boolean freeze) {
        helpers.put("fifty",fifty);
        helpers.put("skip",skip);
        helpers.put("freeze",freeze);
    }
    public void addFont(){
        Typeface openSans = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSansHebrew-Regular.ttf");
        answer1.setTypeface(openSans);
        answer2.setTypeface(openSans);
        answer3.setTypeface(openSans);
        answer4.setTypeface(openSans);
        quest.setTypeface(openSans);
    }
    public void bonusRoundMethod(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bonusRound.setVisibility(View.VISIBLE);
                changeBackgroundOpacity(220, View.VISIBLE);
            }
        },timeForAnswer);
        handler.postDelayed(new Runnable() {
            public void run() {
                bonusRound.setVisibility(View.GONE);
                changeBackgroundOpacity(0, View.GONE);
            }
        }, timeForBonus);
        nextQuestion(timeForBonus);
    }

    public void changeBackgroundOpacity (int opacity, int visible) {
        activityBackground.getBackground().setAlpha(opacity);
        activityBackground.setVisibility(visible);
    }

    public boolean updateCandies (int price) {
        if (MainActivity.candies < price)
            return false;
        long new_candies = MainActivity.candies - price;
        Infra.addCandiesToUser(new_candies);
        MainActivity.setCandies(new_candies);
        displayCandies();
        return true;
    }

    public void notifyNoCandies () {
        Toast toast = Toast.makeText(this, "Need more candies mateee!!", Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        toast.show();
    }

    public void displayCandies() {
        candiesTextView.setText(MainActivity.candies + "");
    }

    public void freezeProgressBar() {
        freezeBar.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofInt(freezeBar, "progress", 0, 100);
        anim.setDuration(17500);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
    }
    }

