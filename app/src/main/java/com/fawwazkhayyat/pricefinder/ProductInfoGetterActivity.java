package com.fawwazkhayyat.pricefinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductInfoGetterActivity extends ProductInfoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeId = getIntent().getStringExtra(SelectStoreActivity.EXTRA_STORE_ID);
        barcode = getIntent().getStringExtra(BasketActivity.EXTRA_BARCODE);
        barcodeType = getIntent().getStringExtra(BasketActivity.EXTRA_BARCODE_TYPE);

        //get the product info from firestore
        FireStoreViewModel fireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel.class);
        fireStoreViewModel.getProduct(storeId, barcode).observe(this, product -> {
            Log.d("DEBUG_TAG", "ProductInfoActivity: receiving data from fireStoreViewModel");
            quantity = 1;
            getDataFromProduct(product);
            populateProductInfo();
        });
    }

    public void addToBasket_click(View view){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BARCODE,barcode);
        intent.putExtra(EXTRA_NAME,name);
        intent.putExtra(EXTRA_QUANTITY,quantity);
        intent.putExtra(EXTRA_PRICE,price);
        intent.putExtra(EXTRA_IMAGE_PATH,imagePath);
        setResult(RESULT_OK, intent);
        finish();
    }
}
