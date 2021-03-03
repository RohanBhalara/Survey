package com.example.survey;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class MyRecyclerViewFormRow extends RecyclerView.Adapter<MyRecyclerViewFormRow.ViewHolder> implements AnswerChange,FileResponseListen {
    private List<QuestionObject> mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context context;

    private HashMap<Integer, Integer> questionIdList = new HashMap<>();
    ArrayList<AnswerObject> answerObjectList = new ArrayList<>();
    private FilePick filePick;

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

        holder.myTextView.setText(question);

        holder.etSmallAnswer.setVisibility(View.GONE);
        holder.etBigAnswer.setVisibility(View.GONE);
        holder.btnUpload.setVisibility(View.GONE);

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
        } else if (questionType == "Upload") {

            holder.btnUpload.setVisibility(View.VISIBLE);
            holder.btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filePick.showFilePicker(position);
                }
            });
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

    @Override
    public void fileResult(Uri uri, int index) {
        answerObjectList.add(new AnswerObject(index,uri));
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
            btnUpload = itemView.findViewById(R.id.btnUpload);

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
}