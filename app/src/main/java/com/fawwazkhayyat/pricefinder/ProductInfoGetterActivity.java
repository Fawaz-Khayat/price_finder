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
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference imageRef = storageReference.child(product.getImageRefPath());
            GlideApp.with(this)
                    .load(imageRef)
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

    public void addToBasket_click(View view){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BARCODE,barcode);
        intent.putExtra(EXTRA_NAME,name);
        intent.putExtra(EXTRA_QUANTITY,quantity);
        intent.putExtra(EXTRA_PRICE,price);
        setResult(RESULT_OK, intent);
        finish();
    }
}
