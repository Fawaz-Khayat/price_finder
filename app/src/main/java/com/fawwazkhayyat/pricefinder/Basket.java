package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

public class Basket extends AppCompatActivity {
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        final String EXTRA_NAME = "com.fawwazkhayyat.pricefinder";
        String config;
        if (savedInstanceState == null){
            config = getIntent().getStringExtra(EXTRA_NAME);
            if(config != null && config.equals("NEW")){
                findViewById(R.id.imageButton_save).setEnabled(false);
            }
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.close();

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

    // todo
    // get list_items from the database

    // todo
    // implement button to start scanner activity
}
