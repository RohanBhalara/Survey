package com.example.survey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewRadioButtonAdapter extends RecyclerView.Adapter<MyRecyclerViewRadioButtonAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private int lastSelectedPosition = -1;
    private AnswerChange answerChange;
    private int questionId;

    // data is passed into the constructor
    MyRecyclerViewRadioButtonAdapter(Context context, List<String> data, AnswerChange answerChange, int questionId) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.answerChange = answerChange;
        this.questionId = questionId;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_radiobuttonrow, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String radioButtonOption = mData.get(position);
        holder.myRadioButton.setText(radioButtonOption);

        holder.myRadioButton.setChecked(lastSelectedPosition == position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RadioButton myRadioButton;

        ViewHolder(View itemView) {
            super(itemView);
            myRadioButton = itemView.findViewById(R.id.rbAnswer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            lastSelectedPosition = getAdapterPosition();

            answerChange.onChange(questionId, getItem(getAdapterPosition()), null);
            Toast.makeText(context,"The Item Clicked is: "+getItem(getAdapterPosition()),Toast.LENGTH_SHORT).show();

            notifyDataSetChanged();
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
