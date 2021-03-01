package com.example.survey;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewCheckboxAdapter extends RecyclerView.Adapter<MyRecyclerViewCheckboxAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private AnswerChange answerChange;
    private int questionId;
    private ArrayList<String> answerArr = new ArrayList<>();

    // data is passed into the constructor
    MyRecyclerViewCheckboxAdapter(Context context, List<String> data, AnswerChange answerChange, int questionId) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.answerChange = answerChange;
        this.questionId = questionId;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_checkboxrow, parent, false);
        return new MyRecyclerViewCheckboxAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String checkboxOption = mData.get(position);
        holder.myCheckBox.setText(checkboxOption);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox myCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            myCheckBox = itemView.findViewById(R.id.chkbAnswer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            if(myCheckBox.isChecked()){
                answerArr.add(getItem(getAdapterPosition()));
            }
            else{
                if(answerArr.contains(getItem(getAdapterPosition()))){
                    answerArr.remove(getItem(getAdapterPosition()));
                }
            }
            answerChange.onChange(questionId, null, answerArr);
            Toast.makeText(context,"The Item Clicked is: "+getItem(getAdapterPosition()),Toast.LENGTH_SHORT).show();
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

        //answerChange.onChange(questionId, );
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
