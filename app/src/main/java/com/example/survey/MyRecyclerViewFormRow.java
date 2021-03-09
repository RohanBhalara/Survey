package com.example.survey;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyRecyclerViewFormRow extends RecyclerView.Adapter<MyRecyclerViewFormRow.ViewHolder> implements AnswerChange,FileResponseListen, AdapterView.OnItemSelectedListener {
    private List<QuestionObject> mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context context;

    private HashMap<Integer, Integer> questionIdList = new HashMap<>();
    ArrayList<AnswerObject> answerObjectList = new ArrayList<>();
    ArrayList<String> sectionList = new ArrayList<>();
    private FilePick filePick;
    ArrayList<String> options = new ArrayList<>();
    private int spinnerQuestionId;
    private ArrayList<DependantQuestionObject> dependantQuestionList = new ArrayList<>();

    private static final int FILE_SELECT_CODE = 0;

    // data is passed into the constructor
    MyRecyclerViewFormRow(Context context, List<QuestionObject> data,FilePick filePick) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.filePick = filePick;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_formrow, parent, false);
        return new MyRecyclerViewFormRow.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final int questionId = mData.get(position).id;
        Log.d("STRING", "question id = " + questionId);
        String question = mData.get(position).question;
        String questionType = mData.get(position).questionType;
        String section = mData.get(position).section;
        int parentQuestionId = mData.get(position).parentQuestionId;
        String parentAnswer = mData.get(position).parentAnswer;

        if(parentQuestionId != 0){
            DependantQuestionObject dqObj = new DependantQuestionObject(position, holder);
            dependantQuestionList.add(dqObj);
            holder.cardViewQuestion.setVisibility(View.GONE);
        }

        if(!sectionList.contains(section)){
            holder.textViewSection.setText(section);
            holder.textViewSection.setVisibility(View.VISIBLE);
            sectionList.add(section);
        }

        holder.myTextView.setText(question);

        holder.etSmallAnswer.setVisibility(View.GONE);
        holder.etBigAnswer.setVisibility(View.GONE);
        holder.btnUpload.setVisibility(View.GONE);
        holder.spinnerAnswer.setVisibility(View.GONE);

        if (questionType.equals("Small Answer")) {
            holder.etSmallAnswer.setVisibility(View.VISIBLE);
            holder.etSmallAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String s = charSequence.toString();
                    AnswerObject answerObject = new AnswerObject(questionId, s, null);

                    if (questionIdList.containsKey(questionId)) {
                        answerObjectList.set(questionIdList.get(questionId), answerObject);
                        Log.d("STRING", "Small Change");
                    } else {
                        answerObjectList.add(answerObject);
                        questionIdList.put(questionId, answerObjectList.indexOf(answerObject));
                        Log.d("STRING", "Small New");
                    }
                    for (AnswerObject aObj : answerObjectList) {
                        Log.d("STRING", aObj.id + " " + aObj.answerString);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else if (questionType.equals("Big Answer")) {
            holder.etBigAnswer.setVisibility(View.VISIBLE);
            holder.etBigAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String s = charSequence.toString();
                    AnswerObject answerObject = new AnswerObject(questionId, s, null);

                    if (questionIdList.containsKey(questionId)) {
                        answerObjectList.set(questionIdList.get(questionId), answerObject);
                        Log.d("STRING", "Big Change");
                    } else {
                        answerObjectList.add(answerObject);
                        questionIdList.put(questionId, answerObjectList.indexOf(answerObject));
                        Log.d("STRING", "Big New");
                    }
                    for (AnswerObject aObj : answerObjectList) {
                        Log.d("STRING", aObj.id + " " + aObj.answerString);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else if (questionType.equals("Checkbox")) {
            MyRecyclerViewCheckboxAdapter checkboxAdapter;
            ArrayList<String> checkboxOptions = new ArrayList<>();

            for (String str : mData.get(position).options) {
                checkboxOptions.add(str);
            }

            // set up the RecyclerView
            holder.recyclerViewCheckbox.setLayoutManager(new LinearLayoutManager(context));
            checkboxAdapter = new MyRecyclerViewCheckboxAdapter(context, checkboxOptions, this, mData.get(position).id);

            holder.recyclerViewCheckbox.setAdapter(checkboxAdapter);
        } else if (questionType.equals("Radio Button")) {
            MyRecyclerViewRadioButtonAdapter radioButtonAdapter;
            ArrayList<String> radioButtonOptions = new ArrayList<>();

            for (String str : mData.get(position).options) {
                radioButtonOptions.add(str);
            }

            //set up the RecyclerView
            holder.recyclerViewRadioButton.setLayoutManager(new LinearLayoutManager(context));
            radioButtonAdapter = new MyRecyclerViewRadioButtonAdapter(context, radioButtonOptions, this, questionId);

            holder.recyclerViewRadioButton.setAdapter(radioButtonAdapter);
        } else if (questionType.equals("Upload")) {
            holder.btnUpload.setVisibility(View.VISIBLE);
            holder.btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filePick.showFilePicker(questionId);
                }
            });
        }
        else if(questionType.equals("Multiple Textbox")){
            MyRecyclerViewEditText editTextAdapter;
            ArrayList<String> editTexts = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                editTexts.add("");
            }

            // set up the RecyclerView
            holder.recyclerViewEditText.setLayoutManager(new LinearLayoutManager(context));
            editTextAdapter = new MyRecyclerViewEditText(context, editTexts, this, mData.get(position).id);

            holder.recyclerViewEditText.setAdapter(editTextAdapter);
        }
        else if(questionType.equals("Spinner")){
            options.add("Select...");
            for (String str : mData.get(position).options) {
                options.add(str);
            }

            spinnerQuestionId = questionId;
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, options);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            holder.spinnerAnswer.setAdapter(adapter);
            holder.spinnerAnswer.setVisibility(View.VISIBLE);
            holder.spinnerAnswer.setOnItemSelectedListener(this);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onChange(int questionId, String ans, ArrayList<String> ansArr) {
        String strArr[] = new String[10];
        AnswerObject answerObject;

        if (ansArr != null) {
            int i = -1;
            for (String str : ansArr) {
                i++;
                strArr[i] = str;
            }
        }

        if (ans == null) {
            answerObject = new AnswerObject(questionId, null, strArr);
        } else {
            answerObject = new AnswerObject(questionId, ans, null);
            checkOptionOfParentQuestion(questionId, ans);
        }

        if (questionIdList.containsKey(questionId)) {
            answerObjectList.set(questionIdList.get(questionId), answerObject);
            Log.d("STRING", "CR Change");
        } else {
            answerObjectList.add(answerObject);
            questionIdList.put(questionId, answerObjectList.indexOf(answerObject));
            Log.d("STRING", "CR New");
        }

        for (AnswerObject aObj : answerObjectList) {
            Log.d("STRING", aObj.id + " " + aObj.answerString + " " + Arrays.toString(aObj.answer));
        }
    }

    public void onMultiEditTextChange(int questionId, ArrayList<MultiEditTextObject> ansArr) {
        AnswerObject answerObject;

        answerObject = new AnswerObject(questionId, ansArr);

        if (questionIdList.containsKey(questionId)) {
            answerObjectList.set(questionIdList.get(questionId), answerObject);
            Log.d("STRING", "MultiEditText Change");
        } else {
            answerObjectList.add(answerObject);
            questionIdList.put(questionId, answerObjectList.indexOf(answerObject));
            Log.d("STRING", "MultiEditText New");
        }

        for (AnswerObject aObj : answerObjectList) {
            Log.d("STRING", aObj.id+" ");
        }
    }

    @Override
    public void fileResult(Uri uri, int index) {
        answerObjectList.add(new AnswerObject(index,uri));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s = options.get(position);
        AnswerObject answerObject = new AnswerObject(spinnerQuestionId, s, null);

        if (questionIdList.containsKey(spinnerQuestionId)) {
            answerObjectList.set(questionIdList.get(spinnerQuestionId), answerObject);
            Log.d("STRING", "Spinner Change");
            Toast.makeText(context, "Spinner Change", Toast.LENGTH_SHORT).show();
        } else {
            answerObjectList.add(answerObject);
            questionIdList.put(spinnerQuestionId, answerObjectList.indexOf(answerObject));
            Log.d("STRING", "Spinner New");
            Toast.makeText(context, "Spinner New", Toast.LENGTH_SHORT).show();
        }
        for (AnswerObject aObj : answerObjectList) {
            Log.d("STRING", aObj.id + " " + aObj.answerString);
        }
        checkOptionOfParentQuestion(spinnerQuestionId, s);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewSection;
        TextView myTextView;
        EditText etSmallAnswer;
        EditText etBigAnswer;
        RecyclerView recyclerViewCheckbox;
        RecyclerView recyclerViewRadioButton;
        Button btnUpload;
        RecyclerView recyclerViewEditText;
        Spinner spinnerAnswer;
        CardView cardViewQuestion;

        ViewHolder(View itemView) {
            super(itemView);
            textViewSection = itemView.findViewById(R.id.txtSection);
            myTextView = itemView.findViewById(R.id.questionText);
            etSmallAnswer = itemView.findViewById(R.id.etSmallAnswer);
            etBigAnswer = itemView.findViewById(R.id.etBigAnswer);
            recyclerViewCheckbox = itemView.findViewById(R.id.rvCheckbox);
            recyclerViewRadioButton = itemView.findViewById(R.id.rvRadioButton);
            btnUpload = itemView.findViewById(R.id.btnUpload);
            recyclerViewEditText = itemView.findViewById(R.id.rvMultipleTextBox);
            spinnerAnswer = itemView.findViewById(R.id.spinnerAnswer);
            cardViewQuestion = itemView.findViewById(R.id.cardViewQuestion);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            Toast.makeText(context, "The Item Clicked is: " + getItem(getAdapterPosition()), Toast.LENGTH_SHORT).show();
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

    public ArrayList<AnswerObject> getAnswerObjectList(){
        return this.answerObjectList;
    }

    public void checkOptionOfParentQuestion(int questionId, String ans){
        for(DependantQuestionObject dqObj : dependantQuestionList){
            if(mData.get(dqObj.position).parentQuestionId == questionId){
                if(mData.get(dqObj.position).parentAnswer == ans){
                    Log.d("String", "TRUE "+mData.get(dqObj.position).id+" -> "+questionId);
                    dqObj.holder.cardViewQuestion.setVisibility(View.VISIBLE);
                }
                else{
                    Log.d("String", "FALSE "+mData.get(dqObj.position).id+" -> "+questionId);
                    dqObj.holder.cardViewQuestion.setVisibility(View.GONE);
                    AnswerObject answerObject = new AnswerObject();
                    if (questionIdList.containsKey(mData.get(dqObj.position).id)) {
                        answerObjectList.set(questionIdList.get(mData.get(dqObj.position).id), answerObject);
                    }
                }
            }
        }
    }
}