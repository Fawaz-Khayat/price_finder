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
    final String EXTRA_TYPE = "com.fawwazkhayyat.pricefinder.main.type";
    final String DATE_TIME_SAVE_FORMAT = "dd MMM yyyy,HH:mm,z";
    final String DATE_TIME_OUTPUT_FORMAT = "EE dd MMM yyyy, HH:mm, z";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void new_click(View view){
        //https://stackoverflow.com/questions/21349475/calendar-getinstancetimezone-gettimezoneutc-is-not-returning-utc-time
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat saveDateFormat = new SimpleDateFormat(DATE_TIME_SAVE_FORMAT, Locale.CANADA);
        saveDateFormat.setTimeZone(timeZone);
        String saveDate = saveDateFormat.format(calendar.getTime());

        Intent  intent = new Intent(this, SelectStore.class);
        intent.putExtra(EXTRA_TYPE, "new");
        startActivity(intent);
    }

    public void saved_click(View view){
        Intent  intent = new Intent(this, SelectStore.class);
        intent.putExtra(EXTRA_TYPE, "saved");

        // todo
        // start saved lists activity
    }
}
