package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    static final int REQUEST_CODE_ADD = 1000;
    static final int REQUEST_CODE_EDIT = 1100;


    RecyclerView recyclerView;
    TextView textView_result;

    //final SharedDataSingleton singleton = SharedDataSingleton.getInstance();
    ArrayList<Product> products;
    private String storeId;

    BasketRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        products = new ArrayList<>();

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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BasketRecyclerViewAdapter(products);
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
                        //todo
                        // check if the product already in the basket
                        // if already in the basket, edit
                        // else, add
                        int productIndex = getProductPosition(barcode);
                        if(productIndex<0)
                            getProductInfo(storeId, barcode, barcodeType);
                        else

                            //todo
                        // edit the product info

                        textView_result.setText(barcode);
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            //request code for product info
            case REQUEST_CODE_ADD:
                Log.d("DEBUG_TAG", "onActivityResult: product info");

                Product product = new Product(data.getStringExtra(ProductGetter.EXTRA_BARCODE));
                product.setName(data.getStringExtra(ProductGetter.EXTRA_NAME));
                product.setPrice(data.getDoubleExtra(ProductGetter.EXTRA_PRICE,0.0));
                product.setQuantity(data.getIntExtra(ProductGetter.EXTRA_QUANTITY,0));
                products.add(product);
                adapter.notifyItemInserted(products.size());
                break;
            case REQUEST_CODE_EDIT:
                //todo
                // implement editing the product
                //adapter.notifyDataSetChanged ();
                break;
        }
    }

    /**
     * Check if the product exists in the product list,
     * @param barcode
     * @return if exists, return the position of the product in the ArrayList
     *      * else, return -1
     */
    private int getProductPosition(String barcode){
        for(int i=0;i<products.size();i++){
            if(products.get(i).getBarcode().equals(barcode)){
                return i;
            }
        }
        return -1;
    }

    private void getProductInfo(String storeId, String  barcode, String barcodeType){
        barcode = "043100633501";
        Intent intent = new Intent(this, ProductGetter.class);
        intent.putExtra(SelectStore.EXTRA_STORE_ID,storeId);
        intent.putExtra(EXTRA_BARCODE,barcode);
        intent.putExtra(EXTRA_BARCODE_TYPE,barcodeType);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }
    private void editProduct(Product product){
        Intent intent = new Intent(this, ProductGetter.class);

        //todo
        // implement edit product quantity
    }
}
