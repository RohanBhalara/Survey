package com.example.survey;

import java.util.ArrayList;

public interface AnswerChange {
    void onChange(int questionId, String ans, ArrayList<String> ansArr);

    void onMultiEditTextChange(int questionId, ArrayList<MultiEditTextObject> multiEditTextObjectArrayList);
}
