package com.fawwazkhayyat.pricefinder;


import android.content.Intent;
import android.os.Bundle;

public class ProductInfoEditorActivity extends ProductInfoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        name = intent.getStringExtra(BasketActivity.EXTRA_NAME);
        description = intent.getStringExtra(BasketActivity.EXTRA_DESCRIPTION);
        price = intent.getDoubleExtra(BasketActivity.EXTRA_PRICE,0.0);
        quantity = intent.getIntExtra(BasketActivity.EXTRA_QUANTITY,0);
        imagePath = intent.getStringExtra(BasketActivity.EXTRA_IMAGE_PATH);

        populateProductInfo();
    }
}
