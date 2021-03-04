package com.example.survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    SwipeRefreshLayout swipeRefresh;
    private static final String TAG = "DEBUG";
    DBHelper db;
    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        db = new DBHelper(Main3Activity.this);

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
        ArrayList<FormObject> forms = new ArrayList<>();
        FormObject formObject1 = new FormObject(1,"Form 1");
        FormObject formObject2 = new FormObject(2,"Form 2");
        FormObject formObject3 = new FormObject(3,"Form 3");
        FormObject formObject4 = new FormObject(4,"Form 4");
        FormObject formObject5 = new FormObject(5,"Form 5");
        forms.add(formObject1);
        forms.add(formObject2);
        forms.add(formObject3);
        forms.add(formObject4);
        forms.add(formObject5);

        JSONArray formsJsArray = new JSONArray(forms);

        //Storing in database
        String formsList = db.getForms(1);
        Log.d(TAG, "FORMS LIST = "+formsList);
        if(formsList == null){
            if(db.insertForms(1, formsJsArray.toString())){
                Toast.makeText(getApplicationContext(), "Inset done", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getApplicationContext(), "Insert not done", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            if(db.updateForms(1, formsJsArray.toString())){
                Toast.makeText(getApplicationContext(), "Update done", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getApplicationContext(), "Update not done", Toast.LENGTH_SHORT).show();
            }
        }

        //Swipe Refresh Section
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
            }
        });

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvFormName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(Main3Activity.this, forms);

        recyclerView.setAdapter(adapter);
    }
}
