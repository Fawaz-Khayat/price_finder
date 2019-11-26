package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;


public class SelectStoreActivity extends AppCompatActivity {
    //final String EXTRA_BASKET_TYPE = "com.fawwazkhayyat.pricefinder.BASKET_TYPE";
    static final String EXTRA_STORE_ID = "com.fawwazkhayyat.pricefinder.STORE_ID";
    static final String EXTRA_STORE_TAX = "com.fawwazkhayyat.pricefinder.STORE_TAX";
    final String TAG = "DEBUG_TAG";

    private String storeId, storeAddress;
    private double tax;

    Spinner spinner_selectStore;
    TextView textView_name,textView_address;
    ImageView imageView_store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);

        spinner_selectStore = findViewById(R.id.spinner_selectStore);
        textView_name = findViewById(R.id.textView_name);
        textView_address = findViewById(R.id.textView_address);
        imageView_store = findViewById(R.id.imageView_store);

        FireStoreViewModel fireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel.class);
        fireStoreViewModel.getStores().observe(this, stores -> {
            // update UI
            Log.d(TAG, "inside observer: " + Arrays.toString(stores));

            setSpinnerAdapter(stores);
            spinner_selectStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    storeId = stores[position].getId();
                    storeAddress = stores[position].getAddress();
                    tax = stores[position].getTax();
                    //todo
                    //add more info: Name, address, ...
                    textView_name.setText(storeId);
                    textView_address.setText(storeAddress);

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();
                    //assume images path in google cloud always = /images/{barcode}.jpg
                    StorageReference imageRef = storageReference.child("/images/stores/" + storeId + ".jpg");
                    GlideApp.with(view.getContext())
                            .load(imageRef)
                            .into(imageView_store);
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
        intent.putExtra(EXTRA_STORE_TAX, tax);
        startActivity(intent);
    }
}
