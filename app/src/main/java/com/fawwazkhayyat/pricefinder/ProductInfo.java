package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductInfo extends AppCompatActivity {
    static final int RESULT_CODE = 1001;
    static final String EXTRA_BARCODE = "com.fawwazkhayyat.pricefinder.BARCODE";
    static final String EXTRA_NAME = "com.fawwazkhayyat.pricefinder.NAME";
    static final String EXTRA_QUANTITY= "com.fawwazkhayyat.pricefinder.QUANTITY";
    static final String EXTRA_PRICE = "com.fawwazkhayyat.pricefinder.PRICE";

    final SharedDataSingleton singleton = SharedDataSingleton.getInstance();
    TextView textView_name, textView_description, textView_price, textView_quantity;
    ImageView imageView_product;
    //ImageButton imageButton_decrease, imageButton_increase, imageButton_addToBasket;

    String storeId, barcode, barcodeType, name;
    int listId;
    double price, tax, total;
    int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        storeId = getIntent().getStringExtra(SelectStore.EXTRA_STORE_ID);
        barcode = getIntent().getStringExtra(Basket.EXTRA_BARCODE);
        barcodeType = getIntent().getStringExtra(Basket.EXTRA_BARCODE_TYPE);

        textView_name = findViewById(R.id.textView_name);
        textView_description = findViewById(R.id.textView_description);
        textView_price = findViewById(R.id.textView_price);
        textView_quantity = findViewById(R.id.textView_quantity);

        FireStoreViewModel fireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel.class);
        fireStoreViewModel.getProduct(storeId, barcode).observe(this, product -> {
            Log.d("DEBUG_TAG", "ProductInfo: receiving data from fireStoreViewModel");
            name = product.getName();
            price = product.getPrice();
            quantity = getAdjustedQuantity();
            textView_name.setText(name);
            //textView_description.setText(product.getDescription());
            textView_price.setText("$"+String.valueOf(price));
            textView_quantity.setText(String.valueOf(quantity));
        });
    }

    private int getAdjustedQuantity(){
        ArrayList<Product> products = singleton.getProductsArrayList();
        for(int i=0;i<products.size();i++){
            if(products.get(i).getBarcode().equals(barcode)){
                return products.get(i).getQuantity();
            }
        }
        return 1;
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
        String date = singleton.getNewDate();

        Intent intent = new Intent();
        intent.putExtra(EXTRA_BARCODE,barcode);
        intent.putExtra(EXTRA_NAME,name);
        intent.putExtra(EXTRA_QUANTITY,quantity);
        intent.putExtra(EXTRA_PRICE,price);
        setResult(RESULT_OK, intent);
        finish();
        //todo
        // temporary disable add to basket button
        // enable add to basket button
    }
}
