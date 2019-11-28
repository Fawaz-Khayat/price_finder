package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void new_click(View view){
        Intent  intent = new Intent(this, SelectStoreActivity.class);
        startActivity(intent);
    }

    public void saved_click(View view){
        Intent  intent = new Intent(this, SavedBasketsListActivity.class);
        startActivity(intent);
    }
}
