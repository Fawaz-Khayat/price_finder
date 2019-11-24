package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        SharedDataSingleton sharedDataSingleton = SharedDataSingleton.getInstance();
        sharedDataSingleton.setNewDate();

        Intent  intent = new Intent(this, SelectStore.class);
        intent.putExtra(EXTRA_BASKET_TYPE, BASKET_TYPE.NEW);
        startActivity(intent);
    }

    public void saved_click(View view){
        Intent  intent = new Intent(this, SelectStore.class);
        intent.putExtra(EXTRA_BASKET_TYPE, BASKET_TYPE.SAVED);

        // todo
        // start saved lists activity
    }
}
