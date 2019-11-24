package com.fawwazkhayyat.pricefinder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

class SharedDataSingleton {
    private static final SharedDataSingleton ourInstance = new SharedDataSingleton();

    static SharedDataSingleton getInstance() {
        return ourInstance;
    }

    private SharedDataSingleton() {
    }

    // todo
    // date save format
    // the new date
    // store id

    final String DATE_TIME_SAVE_FORMAT = "dd MMM yyyy,HH:mm,z";
    final String DATE_TIME_OUTPUT_FORMAT = "EE dd MMM yyyy, HH:mm, z";

    private String newDate = "";
    private ArrayList<Product> productsArrayList;



    public void setNewDate(){
        //https://stackoverflow.com/questions/21349475/calendar-getinstancetimezone-gettimezoneutc-is-not-returning-utc-time
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat saveDateFormat = new SimpleDateFormat(DATE_TIME_SAVE_FORMAT, Locale.CANADA);
        saveDateFormat.setTimeZone(timeZone);
        newDate = saveDateFormat.format(calendar.getTime());
    }

    public String getNewDate(){
        return newDate;
    }

    public void setProductsArrayList(){
        productsArrayList = new ArrayList<>();
    }

    public ArrayList<Product> getProductsArrayList(){
        return productsArrayList;
    }
}
