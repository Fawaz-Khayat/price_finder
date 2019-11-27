package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    static final String EXTRA_BASKET_TYPE = "com.fawwazkhayyat.pricefinder.BASKET_TYPE";
    enum BASKET_TYPE {
        NEW,
        SAVED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void new_click(View view){
        Intent  intent = new Intent(this, SelectStoreActivity.class);
        intent.putExtra(EXTRA_BASKET_TYPE, BASKET_TYPE.NEW);
        startActivity(intent);
    }

    public void saved_click(View view){
        Intent  intent = new Intent(this, SavedBasketsActivity.class);
        intent.putExtra(EXTRA_BASKET_TYPE, BASKET_TYPE.SAVED);
        startActivity(intent);
    }
}
