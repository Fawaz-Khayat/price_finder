package com.fawwazkhayyat.pricefinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.BasketRecyclerViewHolder> {
    private ArrayList<Product> products;

    public BasketRecyclerViewAdapter(ArrayList<Product> products){
        super();
        this.products = products;
    }

    @NonNull
    @Override
    public BasketRecyclerViewAdapter.BasketRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.basket_item, parent, false);

        BasketRecyclerViewHolder viewHolder = new BasketRecyclerViewHolder(linearLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BasketRecyclerViewAdapter.BasketRecyclerViewHolder holder, int position) {
        LinearLayout linearLayout = holder.linearLayout;
        ImageButton imageButton_edit = linearLayout.findViewById(R.id.imageButton_edit);
        TextView textView_name = linearLayout.findViewById(R.id.textView_name);
        TextView textView_quantity = linearLayout.findViewById(R.id.textView_quantity);
        TextView textView_price = linearLayout.findViewById(R.id.textView_price);
        TextView textView_total = linearLayout.findViewById(R.id.textView_total);

        Product product = products.get(position);
        textView_name.setText(product.getName());
        textView_quantity.setText(String.valueOf(product.getQuantity()) + "x");
        textView_price.setText("$" + String.valueOf(product.getPrice()));
        textView_total.setText("$"+(product.getPrice()*product.getQuantity()));

        imageButton_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton imageButton_edit = (ImageButton) v;

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class BasketRecyclerViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        public BasketRecyclerViewHolder(@NonNull LinearLayout linearLayout) {
            super(linearLayout);
            this.linearLayout = linearLayout;
        }
    }
}
