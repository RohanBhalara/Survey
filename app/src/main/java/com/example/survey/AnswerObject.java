package com.example.survey;

import android.net.Uri;

public class AnswerObject {
    int id;
    String answerString;
    String answer[];
    Uri fileAnser;

    AnswerObject(int id, String answerString, String ans[]){
        this.id = id;
        this.answerString = answerString;
        this.answer = ans;
    }

    AnswerObject(int id,Uri fileAnser){
        this.id = id;
        this.fileAnser = fileAnser;
    }
}
