package com.example.survey;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyRecyclerViewEditText extends RecyclerView.Adapter<MyRecyclerViewEditText.ViewHolder>{

    private List<String> mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context context;
    private int questionId;
    private ArrayList<MultiEditTextObject> multiEditTextObjectArrayList = new ArrayList<>();
    private HashMap<Integer, Integer> ansIdList = new HashMap<>();
    AnswerChange answerChange;

    // data is passed into the constructor
    MyRecyclerViewEditText(Context context, List<String> data, AnswerChange answerChange,  int questionId) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.questionId = questionId;
        this.answerChange = answerChange;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyRecyclerViewEditText.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_edittextrow, parent, false);
        return new MyRecyclerViewEditText.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String defaultAnswer = mData.get(position);
        holder.editTextAnswer.setText(defaultAnswer);
        holder.editTextAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String s = charSequence.toString();
                MultiEditTextObject multiEditTextObject = new MultiEditTextObject(position, s);

                if (ansIdList.containsKey(position)) {
                    multiEditTextObjectArrayList.set(ansIdList.get(position), multiEditTextObject);
                    Log.d("STRING", "Mullti Edit Single Change");
                } else {
                    multiEditTextObjectArrayList.add(multiEditTextObject);
                    ansIdList.put(position, multiEditTextObjectArrayList.indexOf(multiEditTextObject));
                    Log.d("STRING", "Mullti Edit Single New");
                }
                for (MultiEditTextObject aObj : multiEditTextObjectArrayList) {
                    Log.d("STRING", aObj.ansId + " " + aObj.answer);
                }
                answerChange.onMultiEditTextChange(questionId, multiEditTextObjectArrayList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        EditText editTextAnswer;
        ViewHolder(View itemView) {
            super(itemView);
            editTextAnswer = itemView.findViewById(R.id.etMultiAnswer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(MyRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
