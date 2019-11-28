package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DecimalFormat;
import java.util.ArrayList;

public abstract class BasketActivity extends AppCompatActivity {
    static final String EXTRA_POSITION = "com.fawwazkhayyat.pricefinder.POSITION";
    static final String EXTRA_BARCODE = "com.fawwazkhayyat.pricefinder.BARCODE";
    static final String EXTRA_BARCODE_TYPE = "com.fawwazkhayyat.pricefinder.BARCODE_TYPE";
    static final String EXTRA_NAME = "com.fawwazkhayyat.pricefinder.NAME";
    static final String EXTRA_DESCRIPTION = "com.fawwazkhayyat.pricefinder.DESCRIPTION";
    static final String EXTRA_PRICE = "com.fawwazkhayyat.pricefinder.PRICE";
    static final String EXTRA_QUANTITY = "com.fawwazkhayyat.pricefinder.QUANTITY";
    static final String EXTRA_IMAGE_PATH = "com.fawwazkhayyat.pricefinder.IMAGE_PATH";
    static final int REQUEST_CODE_ADD = 1000;
    static final int REQUEST_CODE_EDIT = 1100;


    protected RecyclerView recyclerView;
    protected TextView textView_result, textView_tax, textView_subtotal, textView_total;

    //final SharedDataSingleton singleton = SharedDataSingleton.getInstance();
    protected ArrayList<Product> products;
    protected String storeId, storeName, storeAddress;
    protected double subTotal, totalTax, total, storeTax;

    protected BasketRecyclerViewAdapter adapter;

    abstract public void item_click(View view);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        textView_result = findViewById(R.id.textView_result);
        textView_subtotal = findViewById(R.id.textView_subTotal);
        textView_tax = findViewById(R.id.textView_tax);
        textView_total = findViewById(R.id.textView_total);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        products = new ArrayList<>();
        adapter = new BasketRecyclerViewAdapter(products);
        recyclerView.setAdapter(adapter);
    }

    protected void recalculateBasket(){
        subTotal = 0;
        totalTax = 0;
        DecimalFormat decimalFormat = new DecimalFormat("$0.00");
        for(int i=0;i<products.size();i++){
            Product product = products.get(i);
            double productSubTotal = product.getPrice() * product.getQuantity();
            subTotal = subTotal + productSubTotal;
            if(product.isTaxable())
                totalTax = totalTax + (productSubTotal * product.getTax());
        }
        total = subTotal + totalTax;
        textView_subtotal.setText(decimalFormat.format(subTotal));
        textView_tax.setText(decimalFormat.format(totalTax));
        textView_total.setText(decimalFormat.format(total));
    }
}
