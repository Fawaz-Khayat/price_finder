package com.fawwazkhayyat.pricefinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductInfoGetterActivity extends ProductInfoActivity {
    static final int RESULT_FOUND = 1001;
    static final int RESULT_NOT_FOUND = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeId = getIntent().getStringExtra(SelectStoreActivity.EXTRA_STORE_ID);
        barcode = getIntent().getStringExtra(BasketActivity.EXTRA_BARCODE);
        barcodeType = getIntent().getStringExtra(BasketActivity.EXTRA_BARCODE_TYPE);

        minmumQuantity = 1;
        //get the product info from firestore
        FireStoreViewModel fireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel.class);
        fireStoreViewModel.getProduct(storeId, barcode).observe(this, product -> {
            Log.d("DEBUG_TAG", "ProductInfoActivity: receiving data from fireStoreViewModel");
            if(product!=null){
                quantity = 1;
                getDataFromProduct(product);
                populateProductInfo();
            }
            else{
                setResult(RESULT_NOT_FOUND);
                finish();
            }
        });
    }

    public void addToBasket_click(View view){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BARCODE,barcode);
        intent.putExtra(EXTRA_NAME,name);
        intent.putExtra(EXTRA_DESCRIPTION,description);
        intent.putExtra(EXTRA_QUANTITY,quantity);
        intent.putExtra(EXTRA_PRICE,price);
        intent.putExtra(EXTRA_IS_TAXABLE,isTaxable);
        intent.putExtra(EXTRA_IMAGE_PATH,imagePath);
        setResult(RESULT_FOUND, intent);
        finish();
    }
}
