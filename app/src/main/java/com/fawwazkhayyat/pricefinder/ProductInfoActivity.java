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
public class ProductInfoActivity extends AppCompatActivity {
    static final int RESULT_CODE = 1001;
    static final String EXTRA_BARCODE = "com.fawwazkhayyat.pricefinder.BARCODE";
    static final String EXTRA_NAME = "com.fawwazkhayyat.pricefinder.NAME";
    static final String EXTRA_QUANTITY= "com.fawwazkhayyat.pricefinder.QUANTITY";
    static final String EXTRA_PRICE = "com.fawwazkhayyat.pricefinder.PRICE";

    TextView textView_name, textView_description, textView_price, textView_quantity;
    ImageView imageView_product;

    String storeId, barcode, barcodeType, name;
    double price, tax, total;
    int quantity;
    StorageReference imagesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        storeId = getIntent().getStringExtra(SelectStoreActivity.EXTRA_STORE_ID);
        barcode = getIntent().getStringExtra(BasketActivity.EXTRA_BARCODE);
        barcodeType = getIntent().getStringExtra(BasketActivity.EXTRA_BARCODE_TYPE);

        textView_name = findViewById(R.id.textView_name);
        textView_description = findViewById(R.id.textView_description);
        textView_price = findViewById(R.id.textView_price);
        textView_quantity = findViewById(R.id.textView_quantity);
        imageView_product = findViewById(R.id.imageView_product);

        //get the product info from firestore
        FireStoreViewModel fireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel.class);
        fireStoreViewModel.getProduct(storeId, barcode).observe(this, product -> {
            Log.d("DEBUG_TAG", "ProductInfoActivity: receiving data from fireStoreViewModel");
            GlideApp.with(this)
                    .load(product.getImageRef())
                    .into(imageView_product);

            name = product.getName();
            price = product.getPrice();
            quantity = 1;

            textView_name.setText(name);
            //textView_description.setText(product.getDescription());
            textView_price.setText("$"+String.valueOf(price));
            textView_quantity.setText(String.valueOf(quantity));


        });
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

    public void addToBasket_click(View view){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BARCODE,barcode);
        intent.putExtra(EXTRA_NAME,name);
        intent.putExtra(EXTRA_QUANTITY,quantity);
        intent.putExtra(EXTRA_PRICE,price);
        setResult(RESULT_OK, intent);
        finish();
    }
    //todo
    // temporary disable add to basket button
    // enable add to basket button
}
