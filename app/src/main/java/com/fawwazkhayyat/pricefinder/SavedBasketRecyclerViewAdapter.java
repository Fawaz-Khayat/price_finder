package com.fawwazkhayyat.pricefinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SavedBasketRecyclerViewAdapter extends RecyclerView.Adapter<SavedBasketRecyclerViewAdapter.ViewHolder>{
    private ArrayList<SavedListItem> savedListItems;

    public SavedBasketRecyclerViewAdapter(ArrayList<SavedListItem> savedListItems) {
        this.savedListItems = savedListItems;
    }

    @NonNull
    @Override
    public SavedBasketRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_baskets_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(constraintLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedBasketRecyclerViewAdapter.ViewHolder holder, int position) {
        ConstraintLayout constraintLayout = holder.constraintLayout;
        TextView textView_store = constraintLayout.findViewById(R.id.textView_store);
        TextView textView_address = constraintLayout.findViewById(R.id.textView_address);
        TextView textView_date = constraintLayout.findViewById(R.id.textView_date);

        textView_store.setText(savedListItems.get(position).getStoreName());
        textView_address.setText(savedListItems.get(position).getStoreAddress());
        textView_date.setText(savedListItems.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return savedListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull ConstraintLayout constraintLayout) {
            super(constraintLayout);
            this.constraintLayout = constraintLayout;
        }
    }
}
