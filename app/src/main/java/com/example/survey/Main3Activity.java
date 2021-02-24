package com.example.survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class Main3Activity extends AppCompatActivity {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        try{
            String jsonstring = "{ 'child': { 'something': 'value', 'something2': 'value' } }";
            JSONObject resobj = new JSONObject(jsonstring);
            Iterator<?> keys = resobj.keys();
            while(keys.hasNext()) {
                String key = (String) keys.next();
                if (resobj.get(key) instanceof JSONObject) {
                    JSONObject obj = new JSONObject(resobj.get(key).toString());
                    Log.d("res1", obj.getString("something"));
                    Log.d("res2", obj.getString("something2"));
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Form 1");
        animalNames.add("Form 2");
        animalNames.add("Form 3");
        animalNames.add("Form 4");
        animalNames.add("Form 5");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(Main3Activity.this, animalNames);

        recyclerView.setAdapter(adapter);
    }
}
