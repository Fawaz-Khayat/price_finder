package com.fawwazkhayyat.pricefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
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
    final String STATE_SUBTOTAL = "SUBTOTAL";
    final String STATE_TOTAL_TAX = "TOTAL_TAX";
    final String STATE_SUB_TOTAL = "SUB_TOTAL";
    final String STATE_TOTAL = "TOTAL";
    final String TAG = "DEBUG_TAG_BASKET";


    protected RecyclerView recyclerView;
    protected TextView textView_result, textView_tax, textView_subtotal, textView_total;
    protected ArrayList<Product> products;
    protected String storeId, storeName, storeAddress;
    protected double subTotal, totalTax, total, storeTax;

    protected BasketRecyclerViewAdapter adapter;

    abstract public void save_click(View view);
    abstract public void scan_click(View view);
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

        if(savedInstanceState==null)
            products = new ArrayList<>();
        else
            products = savedInstanceState.getParcelableArrayList("products");

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

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");

        subTotal = savedInstanceState.getDouble(STATE_SUB_TOTAL);
        totalTax = savedInstanceState.getDouble(STATE_TOTAL_TAX);
        total = savedInstanceState.getDouble(STATE_TOTAL);
        textView_result.setText(savedInstanceState.getString("textView_result"));
        textView_subtotal.setText(savedInstanceState.getString("textView_subtotal"));
        textView_tax.setText(savedInstanceState.getString("textView_tax"));
        textView_total.setText(savedInstanceState.getString("textView_total"));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");

        outState.putDouble(STATE_SUB_TOTAL,subTotal);
        outState.putDouble(STATE_TOTAL_TAX, totalTax);
        outState.putDouble(STATE_TOTAL, total);
        outState.putString("textView_result",textView_result.getText().toString());
        outState.putString("textView_subtotal",textView_subtotal.getText().toString());
        outState.putString("textView_tax",textView_tax.getText().toString());
        outState.putString("textView_total",textView_total.getText().toString());
        outState.putParcelableArrayList("products",products);
    }
}
