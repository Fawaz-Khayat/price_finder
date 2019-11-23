package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductInfo extends AppCompatActivity {
    final String barcode = "043100633501";
    final String storeId = "sirc";

    TextView textView_name, textView_description, textView_price, textView_quantity;
    ImageView imageView_product;
    //ImageButton imageButton_decrease, imageButton_increase, imageButton_addToBasket;

    int listId;
    double price, tax, total;
    int quantity;

    //todo
    //get the date from singleton
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        textView_name = findViewById(R.id.textView_name);
        textView_description = findViewById(R.id.textView_description);
        textView_price = findViewById(R.id.textView_price);
        textView_quantity = findViewById(R.id.textView_quantity);

        quantity = 0;

        FireStoreViewModel fireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel.class);
        fireStoreViewModel.getProduct(storeId, barcode).observe(this, product -> {
            Log.d("DEBUG_TAG", "ProductInfo onCreate: ");
            textView_name.setText(product.getName());
            //textView_description.setText(product.getDescription());
            textView_price.setText("$"+String.valueOf(product.getPrice()));
        });
    }

    public void decrease_click(View view){
        if(quantity>0)
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
        SharedDataSingleton singleton = SharedDataSingleton.getInstance();
        String date = singleton.getNewDate();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /*
        INSERT INTO TABLE_NAME (column1, column2, column3,...columnN)
        VALUES (value1, value2, value3,...valueN);
         */

        // save to temporary list
        ContentValues insertData = new ContentValues(6);
        insertData.put(SQLiteContract.TemporaryList.COLUMN_NAME_DATE_TIME, date);
        insertData.put(SQLiteContract.TemporaryList.COLUMN_NAME_STORE_ID, storeId);
        insertData.put(SQLiteContract.TemporaryList.COLUMN_NAME_BARCODE, barcode);
        insertData.put(SQLiteContract.TemporaryList.COLUMN_NAME_PRICE, price);
        insertData.put(SQLiteContract.TemporaryList.COLUMN_NAME_QUANTITY, quantity);

        db.insert(SQLiteContract.TemporaryList.TABLE_NAME ,
                null,
                insertData);
        db.close();

        // todo
        // temporary disable add to basket button
        // create intent to for basket activity
        // enable add to basketn button
        // start basket activity
    }
}
