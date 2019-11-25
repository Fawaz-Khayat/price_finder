package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;


public class SelectStoreActivity extends AppCompatActivity {
    //final String EXTRA_BASKET_TYPE = "com.fawwazkhayyat.pricefinder.BASKET_TYPE";
    static final String EXTRA_STORE_ID = "com.fawwazkhayyat.pricefinder.STORE_ID";
    final String TAG = "DEBUG_TAG";

    private String storeId;

    Spinner spinner_selectStore;
    TextView textView_storeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);

        spinner_selectStore = findViewById(R.id.spinner_selectStore);
        textView_storeInfo = findViewById(R.id.textView_storeInfo);

        FireStoreViewModel fireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel.class);
        fireStoreViewModel.getStores().observe(this, stores -> {
            // update UI
            Log.d(TAG, "inside observer: " + Arrays.toString(stores));

            setSpinnerAdapter(stores);
            spinner_selectStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    storeId = stores[position].getId();
                    //todo
                    //add more info: Name, address, ...
                    textView_storeInfo.setText(storeId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });
    }
    private void setSpinnerAdapter(Store[] stores){
        int numberOfStores = stores.length;
        String[] storeNames = new String[numberOfStores];
        for(int i=0;i<numberOfStores;i++){
            storeNames[i] = stores[i].getName();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this ,
                R.layout.support_simple_spinner_dropdown_item,
                storeNames);
        spinner_selectStore.setAdapter(arrayAdapter);
    }

    public void gotoBasket_click(View view){
        Intent intent = new Intent(this, BasketActivity.class);
        // passing the BASKET_TYPE to basket activity
        intent.putExtra(MainActivity.EXTRA_BASKET_TYPE,
                getIntent().getStringExtra(MainActivity.EXTRA_BASKET_TYPE));
        intent.putExtra(EXTRA_STORE_ID, storeId);
        startActivity(intent);
    }
}
