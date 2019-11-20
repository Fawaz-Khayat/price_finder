package com.fawwazkhayyat.pricefinder;

import androidx.annotation.NonNull;
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


public class SelectStore extends AppCompatActivity {
    final String TAG = "DEBUG_TAG";
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


                    textView_storeInfo.setText(stores[position].getName());
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

    public void gotoBasket(View view){
        final String EXTRA_NAME = "com.fawwazkhayyat.pricefinder";
        Intent intent = new Intent(this, Basket.class);
        intent.putExtra(EXTRA_NAME, "NEW");

        startActivity(intent);
    }
}
