package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

//todo
// rewrite the class to only get information of newly added prodects
public abstract class ProductInfoActivity extends AppCompatActivity {
    static final int RESULT_CODE = 1001;
    static final String EXTRA_BARCODE = "com.fawwazkhayyat.pricefinder.BARCODE";
    static final String EXTRA_NAME = "com.fawwazkhayyat.pricefinder.NAME";
    static final String EXTRA_QUANTITY= "com.fawwazkhayyat.pricefinder.QUANTITY";
    static final String EXTRA_PRICE = "com.fawwazkhayyat.pricefinder.PRICE";

    protected TextView textView_name, textView_description, textView_price, textView_quantity;
    protected ImageView imageView_product;

    protected String storeId, barcode, barcodeType, name, description;
    protected double price, tax, total;
    protected int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        textView_name = findViewById(R.id.textView_name);
        textView_description = findViewById(R.id.textView_description);
        textView_price = findViewById(R.id.textView_price);
        textView_quantity = findViewById(R.id.textView_quantity);
        imageView_product = findViewById(R.id.imageView_product);
    }

    public void decrease_click(View view){
        if(quantity>1)
            quantity--;
        textView_quantity.setText(String.valueOf(quantity));
        total = quantity * price;
    }
    public void increase_click(View view){
        quantity++;
        textView_quantity.setText(String.valueOf(quantity));
        total = quantity * price;
    }


    //todo
    // temporary disable add to basket button
    // enable add to basket button
}
