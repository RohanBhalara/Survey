package com.example.survey;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyRecyclerViewFormRow extends RecyclerView.Adapter<MyRecyclerViewFormRow.ViewHolder> implements AnswerChange{
    private List<QuestionObject> mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context context;

    private HashMap<Integer,Integer> questionIdList = new HashMap<>();
    private ArrayList<AnswerObject> answerObjectList = new ArrayList<>();
    // data is passed into the constructor
    MyRecyclerViewFormRow(Context context, List<QuestionObject> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_formrow, parent, false);
        return new MyRecyclerViewFormRow.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int questionId = mData.get(position).id;
        Log.d("STRING", "question id = "+questionId);
        String question = mData.get(position).question;
        String questionType = mData.get(position).questionType;

        holder.myTextView.setText(question);

        holder.etSmallAnswer.setVisibility(View.GONE);
        holder.etBigAnswer.setVisibility(View.GONE);
        if(questionType.equals("Small Answer")) {
            holder.etSmallAnswer.setVisibility(View.VISIBLE);
            holder.etSmallAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String s = charSequence.toString();
                    AnswerObject answerObject = new AnswerObject(questionId, s, null);

                    if(questionIdList.containsKey(questionId)){
                        answerObjectList.set(questionIdList.get(questionId), answerObject);
                        Log.d("STRING", "Small Change");
                    }
                    else{
                        answerObjectList.add(answerObject);
                        questionIdList.put(questionId, answerObjectList.indexOf(answerObject));
                        Log.d("STRING", "Small New");
                    }
                    for(AnswerObject aObj : answerObjectList){
                        Log.d("STRING", aObj.id+" "+aObj.answerString);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        else if (questionType.equals("Big Answer")){
            holder.etBigAnswer.setVisibility(View.VISIBLE);
            holder.etBigAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String s = charSequence.toString();
                    AnswerObject answerObject = new AnswerObject(questionId, s,null);

                    if(questionIdList.containsKey(questionId)){
                        answerObjectList.set(questionIdList.get(questionId), answerObject);
                        Log.d("STRING", "Big Change");
                    }
                    else{
                        answerObjectList.add(answerObject);
                        questionIdList.put(questionId, answerObjectList.indexOf(answerObject));
                        Log.d("STRING", "Big New");
                    }
                    for(AnswerObject aObj : answerObjectList){
                        Log.d("STRING", aObj.id+" "+aObj.answerString);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        else if (questionType.equals("Checkbox")){
            MyRecyclerViewCheckboxAdapter checkboxAdapter;
            ArrayList<String> checkboxOptions = new ArrayList<>();

            for(String str : mData.get(position).options){
                checkboxOptions.add(str);
            }

            // set up the RecyclerView
            holder.recyclerViewCheckbox.setLayoutManager(new LinearLayoutManager(context));
            checkboxAdapter = new MyRecyclerViewCheckboxAdapter(context, checkboxOptions,this, mData.get(position).id);

            holder.recyclerViewCheckbox.setAdapter(checkboxAdapter);
        }
        else if(questionType.equals("Radio Button")){
            MyRecyclerViewRadioButtonAdapter radioButtonAdapter;
            ArrayList<String> radioButtonOptions = new ArrayList<>();

            for(String str : mData.get(position).options){
                radioButtonOptions.add(str);
            }

            //set up the RecyclerView
            holder.recyclerViewRadioButton.setLayoutManager(new LinearLayoutManager(context));
            radioButtonAdapter = new MyRecyclerViewRadioButtonAdapter(context, radioButtonOptions, this, questionId);

            holder.recyclerViewRadioButton.setAdapter(radioButtonAdapter);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onChange(int questionId,String ans, ArrayList<String> ansArr) {
        String strArr[] = new String[10];
        AnswerObject answerObject;

        if(ansArr != null){
            int i = -1;
            for(String str : ansArr) {
                i++;
                strArr[i] = str;
            }
        }

        if(ans == null){
            answerObject = new AnswerObject(questionId, null, strArr);
        }
        else{
            answerObject = new AnswerObject(questionId, ans, null);
        }

        if(questionIdList.containsKey(questionId)){
            answerObjectList.set(questionIdList.get(questionId), answerObject);
            Log.d("STRING", "CR Change");
        }
        else{
            answerObjectList.add(answerObject);
            questionIdList.put(questionId, answerObjectList.indexOf(answerObject));
            Log.d("STRING", "CR New");
        }

        for(AnswerObject aObj : answerObjectList){
            Log.d("STRING", aObj.id+" "+aObj.answerString+" "+Arrays.toString(aObj.answer));
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        EditText etSmallAnswer;
        EditText etBigAnswer;
        RecyclerView recyclerViewCheckbox;
        RecyclerView recyclerViewRadioButton;
        Button btnUpload;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.questionText);
            etSmallAnswer = itemView.findViewById(R.id.etSmallAnswer);
            etBigAnswer = itemView.findViewById(R.id.etBigAnswer);
            recyclerViewCheckbox = itemView.findViewById(R.id.rvCheckbox);
            recyclerViewRadioButton = itemView.findViewById(R.id.rvRadioButton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            Toast.makeText(context,"The Item Clicked is: "+getItem(getAdapterPosition()),Toast.LENGTH_SHORT).show();
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).question;
    }

    // allows clicks events to be caught
    void setClickListener(MyRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
