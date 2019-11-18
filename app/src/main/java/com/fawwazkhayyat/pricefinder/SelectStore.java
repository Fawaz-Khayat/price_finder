package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

public class SelectStore extends AppCompatActivity {
    Spinner spinner_selectStore;
    AppFirestoreDatabase firestoreDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);

        firestoreDatabase = new AppFirestoreDatabase();
        spinner_selectStore = findViewById(R.id.spinner_selectStore);
        
    }
}
