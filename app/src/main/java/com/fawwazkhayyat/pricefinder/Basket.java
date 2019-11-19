package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Basket extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // random testing data
        ArrayList<Product> products = new ArrayList<>();
        Product product1 = new Product("043100633501");
        product1.setQuantity(2);
        product1.setPrice(12);
        products.add(product1);

        recyclerView.setAdapter(new BasketRecyclerViewAdapter(products));

    }
}
