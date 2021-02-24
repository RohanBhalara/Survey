package com.example.survey;

public class QuestionObject {

    int id;
    String question;
    String questionType;
    String[] options = new String[10];

    QuestionObject(int id, String question, String questionType, String options[]){
        this.id = id;
        this.question = question;
        this.questionType = questionType;
        this.options = options;
    }
}
