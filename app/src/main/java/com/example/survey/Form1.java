package com.example.survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Form1 extends AppCompatActivity {

    TextView txtHeading;
    MyRecyclerViewFormRow adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1);

        txtHeading = (TextView)findViewById(R.id.txtHeading);

        Intent intent = getIntent();
        String formNumber = intent.getStringExtra("formNumber");
        txtHeading.setText(formNumber);

        // data to populate the RecyclerView with
        ArrayList<String> questions = new ArrayList<>();
        questions.add("Question 1");
        questions.add("Question 2");
        questions.add("Question 3");
        questions.add("Question 4");
        questions.add("Question 5");

        ArrayList<QuestionObject> questionObjects = new ArrayList<>();

        QuestionObject qObj1 = new QuestionObject(1,"Question 1", "small ans", null);
        String[] str = new String[2];
        str[0] = "True";
        str[1] = "False";
        QuestionObject qObj2 = new QuestionObject(2,"Question 2", "radio button", str);

        questionObjects.add(qObj1);
        questionObjects.add(qObj2);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(Form1.this));
        adapter = new MyRecyclerViewFormRow(this, questions);

        recyclerView.setAdapter(adapter);


    }
}
