package com.example.survey;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<FormObject> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<FormObject> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String formName = mData.get(position).formName;
        holder.myTextView.setText(formName);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.txtFormName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            Toast.makeText(context,"The Item Clicked is: "+getItem(getAdapterPosition())+" = "+mData.get(getAdapterPosition()).formId,Toast.LENGTH_SHORT).show();
            Intent intent =  new Intent(context, Form1.class);
            intent.putExtra("formNumber", String.valueOf(mData.get(getAdapterPosition()).formId));
            intent.putExtra("formName", getItem(getAdapterPosition()));
            context.startActivity(intent);
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).formName;
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
