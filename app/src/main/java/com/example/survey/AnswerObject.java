package com.example.survey;

public class AnswerObject {
    int id;
    String answerString;
    String answer[];

    AnswerObject(int id, String answerString, String ans[]){
        this.id = id;
        this.answerString = answerString;
        this.answer = ans;
    }
}
