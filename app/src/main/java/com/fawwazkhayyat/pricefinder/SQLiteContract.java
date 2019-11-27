package com.fawwazkhayyat.pricefinder;

//https://developer.android.com/training/data-storage/sqlite
//https://sqlite.org/foreignkeys.html
//https://stackoverflow.com/questions/734689/sqlite-primary-key-on-multiple-columns

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

class SQLiteContract {

    private SQLiteContract() {}

    /* Inner class that defines the table contents */
    static class Stores{
        static final String TABLE_NAME = "stores";
        static final String COLUMN_NAME_STORE_ID = "_id";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_ADDRESS = "address";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        // Store ID is TEXT because it match the store id in Firestore
                        COLUMN_NAME_STORE_ID + " TEXT NOT NULL PRIMARY KEY, " +
                        COLUMN_NAME_NAME + " TEXT, " +
                        COLUMN_NAME_ADDRESS + " TEXT);";

        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    static class Products{
        static final String TABLE_NAME = "products";
        static final String COLUMN_NAME_BARCODE = "_barcode";
        static final String COLUMN_NAME_DESCRIPTION = "description";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_BARCODE + " TEXT NOT NULL PRIMARY KEY, " +
                        COLUMN_NAME_DESCRIPTION + " TEXT);";

        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static class Lists{
        static String DATE_TIME_SAVE_FORMAT = "dd MMM yyyy,HH:mm,z";
        static String DATE_TIME_OUTPUT_FORMAT = "EE dd MMM yyyy, hh:mm a, z";

        static final String TABLE_NAME = "lists";
        static final String COLUMN_NAME_STORE_ID = "store_id";
        static final String COLUMN_NAME_DATE_TIME = "date_time";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_DATE_TIME + " TEXT NOT NULL PRIMARY KEY, " +
                        COLUMN_NAME_STORE_ID + " TEXT NOT NULL, " +

                        //FOREIGN KEY(store_id) REFERENCES stores(store_id)
                        "FOREIGN KEY(" + COLUMN_NAME_STORE_ID + ") REFERENCES " +
                            Stores.TABLE_NAME + "(" + Stores.COLUMN_NAME_STORE_ID + ")" +
                        ");";

        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        static String getNewDate(){
            //https://stackoverflow.com/questions/21349475/calendar-getinstancetimezone-gettimezoneutc-is-not-returning-utc-time
            // save the date in the table in GMT time zone
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            Calendar calendar = Calendar.getInstance(timeZone);
            SimpleDateFormat saveDateFormat = new SimpleDateFormat(DATE_TIME_SAVE_FORMAT, Locale.CANADA);
            saveDateFormat.setTimeZone(timeZone);
            return saveDateFormat.format(calendar.getTime());
        }

        static String getLocalDate(String GMTDate) throws ParseException {
            Date date = new SimpleDateFormat(DATE_TIME_SAVE_FORMAT, Locale.CANADA).parse(GMTDate);
            String localDate = new SimpleDateFormat(DATE_TIME_OUTPUT_FORMAT, Locale.CANADA).format(date);
            return localDate;
        }
    }

    static class ListItems{
        static final String TABLE_NAME = "list_items";
        static final String COLUMN_NAME_LIST_ID = "list_id";
        static final String COLUMN_NAME_BARCODE = "barcode";
        static final String COLUMN_NAME_PRICE = "price";
        static final String COLUMN_NAME_QUANTITY = "quantity";
        static final String COLUMN_NAME_TAX = "tax";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_LIST_ID + " INTEGER NOT NULL, " +
                        COLUMN_NAME_BARCODE + " TEXT NOT NULL, " +
                        COLUMN_NAME_PRICE + " REAL, " +
                        COLUMN_NAME_QUANTITY + " INTEGER, " +
                        COLUMN_NAME_TAX + " REAL, " +

                        //FOREIGN KEY(list_id) REFERENCES lists(_id)
                        "FOREIGN KEY(" + COLUMN_NAME_LIST_ID + ") REFERENCES " +
                            Lists.TABLE_NAME + "(" + Lists.COLUMN_NAME_DATE_TIME + "), " +
                        //FOREIGN KEY(barcode) REFERENCES products(_barcode)
                        "FOREIGN KEY(" + COLUMN_NAME_BARCODE + ") REFERENCES " +
                            Products.TABLE_NAME + "(" + Products.COLUMN_NAME_BARCODE + "), " +

                        "PRIMARY KEY (" + COLUMN_NAME_LIST_ID + "," + COLUMN_NAME_BARCODE + ")" +
                        ");";

        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
