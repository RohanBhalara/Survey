package com.example.survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Form1 extends AppCompatActivity {

    TextView txtHeading;
    Button btnSave;
    MyRecyclerViewFormRow adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1);

        txtHeading = (TextView)findViewById(R.id.txtHeading);
        btnSave = (Button)findViewById(R.id.btnSave);

        Intent intent = getIntent();
        String formNumber = intent.getStringExtra("formNumber");
        txtHeading.setText(formNumber);

        // data to populate the RecyclerView with
        ArrayList<QuestionObject> questionObjects = new ArrayList<>();

        QuestionObject qObj1 = new QuestionObject(1,"Question 1", "Small Answer", null);
        QuestionObject qObj2 = new QuestionObject(1,"Question 2", "Big Answer", null);
        String[] str = new String[2];
        str[0] = "Java";
        str[1] = "C++";
        QuestionObject qObj3 = new QuestionObject(2,"Question 3", "Checkbox", str);
        String[] str2 = new String[2];
        str2[0] = "True";
        str2[1] = "False";
        QuestionObject qObj4 = new QuestionObject(2,"Question 4", "Radio Button", str2);
        String[] str3 = new String[2];
        str3[0] = "True";
        str3[1] = "False";
        QuestionObject qObj5 = new QuestionObject(2,"Question 5", "Radio Button", str3);

        questionObjects.add(qObj1);
        questionObjects.add(qObj2);
        questionObjects.add(qObj3);
        questionObjects.add(qObj4);
        questionObjects.add(qObj5);

        // set up the RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.rvQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(Form1.this));
        adapter = new MyRecyclerViewFormRow(this, questionObjects);

        recyclerView.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
