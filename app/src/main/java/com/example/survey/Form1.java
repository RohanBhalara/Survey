package com.example.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Form1 extends AppCompatActivity {

    TextView txtFormNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1);

        txtFormNumber = (TextView)findViewById(R.id.txtFormNumber);

        Intent intent = getIntent();
        String formNumber = intent.getStringExtra("formNumber");
        txtFormNumber.setText(formNumber);

        ArrayList<String> formList = new ArrayList<>();
        formList.add("Form 1");
        formList.add("Form 2");
        formList.add("Form 3");
        formList.add("Form 4");
        formList.add("Form 5");


    }
}
