package com.example.survey;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Comparator;

public class AnswerObject {
    int id;
    String answerString;
    String answer[];
    Uri fileAnser;
    ArrayList<MultiEditTextObject> multiAnswer;

    AnswerObject(int id, String answerString, String ans[]){
        this.id = id;
        this.answerString = answerString;
        this.answer = ans;
    }

    AnswerObject(int id,Uri fileAnser){
        this.id = id;
        this.fileAnser = fileAnser;
    }

    AnswerObject(int id, ArrayList<MultiEditTextObject> multiAnswer){
        this.multiAnswer = multiAnswer;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    /*Comparator for sorting the list by roll no*/
    public static Comparator<AnswerObject> QuestionId = new Comparator<AnswerObject>() {

        public int compare(AnswerObject a1, AnswerObject a2) {
            int id1 = a1.getId();
            int id2 = a2.getId();

            return id1-id2;
        }};
}
