package com.fawwazkhayyat.pricefinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.BasketRecyclerViewHolder> {
    private ArrayList<Product> products;
    static final int KEY_POSITION = 1;

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
        linearLayout.setTag(position);
        TextView textView_name = linearLayout.findViewById(R.id.textView_name);
        TextView textView_quantity = linearLayout.findViewById(R.id.textView_quantity);
        TextView textView_price = linearLayout.findViewById(R.id.textView_price);
        TextView textView_total = linearLayout.findViewById(R.id.textView_total);
        ImageView imageView_product = linearLayout.findViewById(R.id.imageView_product);

        Product product = products.get(position);
        // because of limited data in the database,
        // use description instead of name for now.
        textView_name.setText(product.getDescription());
        textView_quantity.setText(String.valueOf(product.getQuantity()) + "x");
        textView_price.setText("$" + String.valueOf(product.getPrice()));
        textView_total.setText("$"+(product.getPrice()*product.getQuantity()));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        //assume images path in google cloud always = /images/{barcode}.jpg
        StorageReference imageRef = storageReference.child("/images/products/" + product.getBarcode() + ".jpg");
        GlideApp.with(holder.itemView.getContext())
                .load(imageRef)
                .into(imageView_product);
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
