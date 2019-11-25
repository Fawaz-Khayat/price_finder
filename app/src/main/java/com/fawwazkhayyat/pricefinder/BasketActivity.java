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

public class BasketActivity extends AppCompatActivity {
    static final String EXTRA_BARCODE = "com.fawwazkhayyat.pricefinder.BARCODE";
    static final String EXTRA_BARCODE_TYPE = "com.fawwazkhayyat.pricefinder.BARCODE_TYPE";
    static final String EXTRA_NAME = "com.fawwazkhayyat.pricefinder.NAME";
    static final String EXTRA_DESCRIPTION = "com.fawwazkhayyat.pricefinder.DESCRIPTION";
    static final String EXTRA_PRICE = "com.fawwazkhayyat.pricefinder.PRICE";
    static final String EXTRA_QUANTITY = "com.fawwazkhayyat.pricefinder.QUANTITY";
    static final String EXTRA_IMAGE_PATH = "com.fawwazkhayyat.pricefinder.IMAGE_PATH";
    static final int REQUEST_CODE_ADD = 1000;
    static final int REQUEST_CODE_EDIT = 1100;


    private RecyclerView recyclerView;
    private TextView textView_result;

    //final SharedDataSingleton singleton = SharedDataSingleton.getInstance();
    private ArrayList<Product> products;
    private String storeId;

    private BasketRecyclerViewAdapter adapter;
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

        storeId = getIntent().getStringExtra(SelectStoreActivity.EXTRA_STORE_ID);

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
                            editProduct(productIndex);

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

                Product product = new Product(data.getStringExtra(ProductInfoActivity.EXTRA_BARCODE));
                product.setName(data.getStringExtra(ProductInfoActivity.EXTRA_NAME));
                product.setPrice(data.getDoubleExtra(ProductInfoActivity.EXTRA_PRICE,0.0));
                product.setQuantity(data.getIntExtra(ProductInfoActivity.EXTRA_QUANTITY,0));
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
     * @param barcode product barcode
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
        //todo
        // remove manual assignment to barcode
        barcode = "043100633501";
        Intent intent = new Intent(this, ProductInfoGetterActivity.class);
        intent.putExtra(SelectStoreActivity.EXTRA_STORE_ID,storeId);
        intent.putExtra(EXTRA_BARCODE,barcode);
        intent.putExtra(EXTRA_BARCODE_TYPE,barcodeType);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    /**
     * start ProductInfoEditorActivity to edit the quantity
     * result should be available inside onResult listener
     * @param position position of the product in the products ArrayList
     */
    private void editProduct(int position){
        Intent intent = new Intent(this, ProductInfoEditorActivity.class);
        String name = products.get(position).getName();
        String description = products.get(position).getDescription();
        double price = products.get(position).getPrice();
        int quantity = products.get(position).getQuantity();
        String imageRefPath = products.get(position).getImageRefPath();

        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_PRICE, price);
        intent.putExtra(EXTRA_QUANTITY, quantity);
        intent.putExtra(EXTRA_IMAGE_PATH, imageRefPath);

        startActivityForResult(intent, REQUEST_CODE_EDIT);
        //todo
        // implement edit product quantity
    }
}
