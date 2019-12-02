package com.fawwazkhayyat.pricefinder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SavedBasketRecyclerViewAdapter extends RecyclerView.Adapter<SavedBasketRecyclerViewAdapter.ViewHolder>{


    private ArrayList<SavedListItem> savedListItems;
    private int checkCounter;

    public SavedBasketRecyclerViewAdapter(ArrayList<SavedListItem> savedListItems) {
        this.savedListItems = savedListItems;
        checkCounter = 0;
    }

    @NonNull
    @Override
    public SavedBasketRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_baskets_item, parent, false);
        final ConstraintLayout parentConstraintLayout = (ConstraintLayout) parent.getParent();
        final ImageButton imageButton_delete = parentConstraintLayout.findViewById(R.id.imageButton_delete);
        final ToggleButton toggleButton_selectAll = parentConstraintLayout.findViewById(R.id.toggleButton_selectAll);

        ToggleButton toggleButton = constraintLayout.findViewById(R.id.toggleButton_select);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleButton.setChecked(isChecked);
                if(isChecked)
                    checkCounter++;
                else
                    checkCounter--;
                //set the delete button at the header
                if(checkCounter>0){
                    imageButton_delete.setEnabled(true);
                    imageButton_delete.setAlpha((float)1.0);
                }
                else{
                    imageButton_delete.setEnabled(false);
                    imageButton_delete.setAlpha((float)0.4);
                }
                //set the toggleButton at the header
                if(checkCounter==savedListItems.size())
                    toggleButton_selectAll.setChecked(true);

                else if(checkCounter<savedListItems.size())
                    toggleButton_selectAll.setChecked(false);
            }
        });
        return new ViewHolder(constraintLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedBasketRecyclerViewAdapter.ViewHolder holder, int position) {
        ConstraintLayout constraintLayout = holder.constraintLayout;
        TextView textView_store = constraintLayout.findViewById(R.id.textView_store);
        TextView textView_address = constraintLayout.findViewById(R.id.textView_address);
        TextView textView_date = constraintLayout.findViewById(R.id.textView_date);
        TextView textView_listId_date = constraintLayout.findViewById(R.id.textView_listId_date);
        ToggleButton toggleButton_select = constraintLayout.findViewById(R.id.toggleButton_select);

        textView_store.setText(savedListItems.get(position).getStoreName());
        textView_address.setText(savedListItems.get(position).getStoreAddress());
        textView_date.setText(savedListItems.get(position).getLocalDate());
        textView_listId_date.setText(savedListItems.get(position).getListId_date());
        boolean isChecked = savedListItems.get(position).isToggleButtonChecked();
        toggleButton_select.setChecked(isChecked);
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
