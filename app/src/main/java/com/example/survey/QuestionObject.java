package com.example.survey;

public class QuestionObject {

    int id;
    String question;
    String questionType;
    String[] options = new String[10];
    String section;
    int parentQuestionId;
    String parentAnswer;

    QuestionObject(int id, String question, String questionType, String options[], String section, int parentQuestionId, String parentAnswer){
        this.id = id;
        this.question = question;
        this.questionType = questionType;
        this.options = options;
        this.section = section;
        this.parentQuestionId = parentQuestionId;
        this.parentAnswer = parentAnswer;
    }
}
