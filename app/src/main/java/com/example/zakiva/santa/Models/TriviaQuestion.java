package com.example.zakiva.santa.Models;

/**
 * Created by zakiva on 8/31/16.
 */

public class TriviaQuestion {
    public String key;
    public String question;
    public String correctAnswer;
    public String answerA;
    public String answerB;
    public String answerC;
    public String answerD;


    public TriviaQuestion() {}

    public TriviaQuestion(String key, String q, String ca, String a, String b, String c, String d) {
        this.key = key;
        this.question = q;
        this.correctAnswer = ca;
        this.answerA = a;
        this.answerB = b;
        this.answerC = c;
        this.answerD = d;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
