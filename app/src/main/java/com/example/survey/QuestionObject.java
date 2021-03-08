package com.example.survey;

public class QuestionObject {

    int id;
    String question;
    String questionType;
    String[] options = new String[10];
    String section;

    QuestionObject(int id, String question, String questionType, String options[], String section){
        this.id = id;
        this.question = question;
        this.questionType = questionType;
        this.options = options;
        this.section = section;
    }
}
