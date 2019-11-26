package com.fawwazkhayyat.pricefinder;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ProductInfoEditorActivity extends ProductInfoActivity {
    static final String EXTRA_POSITION = "com.fawwazkhayyat.pricefinder.POSITION";
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        barcode = intent.getStringExtra(BasketActivity.EXTRA_BARCODE);
        name = intent.getStringExtra(BasketActivity.EXTRA_NAME);
        description = intent.getStringExtra(BasketActivity.EXTRA_DESCRIPTION);
        price = intent.getDoubleExtra(BasketActivity.EXTRA_PRICE,0.0);
        quantity = intent.getIntExtra(BasketActivity.EXTRA_QUANTITY,0);
        imagePath = intent.getStringExtra(BasketActivity.EXTRA_IMAGE_PATH);
        position = intent.getIntExtra(BasketActivity.EXTRA_POSITION,-1);

        //todo
        // check if position is < 0
        populateProductInfo();
    }

    @Override
    public void addToBasket_click(View view) {
        Intent intent = new Intent();

        intent.putExtra(EXTRA_POSITION,position);
        intent.putExtra(EXTRA_QUANTITY,quantity);
        setResult(RESULT_OK, intent);
        finish();
    }
}
