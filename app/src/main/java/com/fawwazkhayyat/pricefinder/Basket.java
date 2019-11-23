package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class Basket extends AppCompatActivity {
    static final String EXTRA_BARCODE = "com.fawwazkhayyat.pricefinder.BARCODE";
    static final String EXTRA_BARCODE_TYPE = "com.fawwazkhayyat.pricefinder.BARCODE_TYPE";
    static final int REQUEST_CODE = 1000;

    RecyclerView recyclerView;
    TextView textView_result;

    private String storeId;
    ArrayList<Product> products;
    BasketRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        products = new ArrayList<>();
        adapter = new BasketRecyclerViewAdapter(products);
        MainActivity.BASKET_TYPE basketType;
        if (savedInstanceState == null){
            basketType = (MainActivity.BASKET_TYPE)
                    getIntent().getSerializableExtra(MainActivity.EXTRA_BASKET_TYPE);
            if(basketType != null && basketType.equals(MainActivity.BASKET_TYPE.NEW)){
                findViewById(R.id.imageButton_save).setEnabled(false);
                findViewById(R.id.imageButton_scan).setEnabled(true);

            }
        }

        storeId = getIntent().getStringExtra(SelectStore.EXTRA_STORE_ID);

        textView_result = findViewById(R.id.textView_result);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.close();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void scan_click(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan(); // `this` is the current Activity
    }

    // Get the scan results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case IntentIntegrator.REQUEST_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if(result != null) {
                    if(result.getContents() == null) {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else {
                        String barcode = result.getContents();
                        String barcodeType = result.getFormatName();
                        Toast.makeText(this, "Scanned: " + barcode, Toast.LENGTH_LONG).show();
                        openProductInfo(storeId, barcode, barcodeType);
                        textView_result.setText(barcode);
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            //request code for product info
            case REQUEST_CODE:
                Log.d("DEBUG_TAG", "onActivityResult: product info");
                Product product = new Product(data.getStringExtra(ProductInfo.EXTRA_BARCODE));
                product.setName(data.getStringExtra(ProductInfo.EXTRA_NAME));
                product.setPrice(data.getDoubleExtra(ProductInfo.EXTRA_PRICE,0.0));
                product.setQuantity(data.getIntExtra(ProductInfo.EXTRA_QUANTITY,0));
                products.add(product);
                adapter.notifyItemInserted(products.size());
                break;
        }
    }

    private void openProductInfo(String storeId, String  barcode, String barcodeType){
        barcode = "043100633501";
        Intent intent = new Intent(this, ProductInfo.class);
        intent.putExtra(SelectStore.EXTRA_STORE_ID,storeId);
        intent.putExtra(EXTRA_BARCODE,barcode);
        intent.putExtra(EXTRA_BARCODE_TYPE,barcodeType);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
