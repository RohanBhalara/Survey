package com.example.survey;

public class DependantQuestionObject {
    int position;
    MyRecyclerViewFormRow.ViewHolder holder;

    DependantQuestionObject(int position, MyRecyclerViewFormRow.ViewHolder holder){
        this.position = position;
        this.holder = holder;
    }
}
