package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductInfo extends AppCompatActivity {
    final String barcode = "043100633501";
    final String storeId = "sirc";

    TextView textView_name, textView_description, textView_price;
    ImageView imageView_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        textView_name = findViewById(R.id.textView_name);
        textView_description = findViewById(R.id.textView_description);
        textView_price = findViewById(R.id.textView_price);

        FireStoreViewModel fireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel.class);
        fireStoreViewModel.getProduct(storeId, barcode).observe(this, product -> {
            Log.d("DEBUG_TAG", "ProductInfo onCreate: ");
            textView_name.setText(product.getName());
            //textView_description.setText(product.getDescription());
            textView_price.setText("$"+String.valueOf(product.getPrice()));
        });

    }
}
