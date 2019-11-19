package com.fawwazkhayyat.pricefinder;

//https://developer.android.com/training/data-storage/sqlite
//https://sqlite.org/foreignkeys.html
//https://stackoverflow.com/questions/734689/sqlite-primary-key-on-multiple-columns

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
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_PICTURE = "picture";
        static final String COLUMN_NAME_DESCRIPTION = "description";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_BARCODE + " TEXT NOT NULL PRIMARY KEY, " +
                        COLUMN_NAME_NAME + " TEXT, " +
                        COLUMN_NAME_PICTURE + " TEXT, " +
                        COLUMN_NAME_DESCRIPTION + " TEXT);";

        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static class Lists{
        static final String TABLE_NAME = "lists";
        static final String COLUMN_NAME_LIST_ID = "_id";
        static final String COLUMN_NAME_STORE_ID = "store_id";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_TIME = "time";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_LIST_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        COLUMN_NAME_STORE_ID + " TEXT NOT NULL, " +
                        COLUMN_NAME_DATE + " TEXT, " +
                        COLUMN_NAME_TIME + " TEXT, " +

                        //FOREIGN KEY(store_id) REFERENCES stores(store_id)
                        "FOREIGN KEY(" + COLUMN_NAME_STORE_ID + ") REFERENCES " +
                            Stores.TABLE_NAME + "(" + Stores.COLUMN_NAME_STORE_ID + ")" +
                        ");";

        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static class ListItems{
        static final String TABLE_NAME = "list_items";
        static final String COLUMN_NAME_LIST_ID = "list_id";
        static final String COLUMN_NAME_BARCODE = "barcode";
        static final String COLUMN_NAME_PRICE = "price";
        static final String COLUMN_NAME_QUANTITY = "quantity";
        static final String COLUMN_NAME_TAX = "tax";
        static final String COLUMN_NAME_TOTAL = "total";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_LIST_ID + " INTEGER NOT NULL, " +
                        COLUMN_NAME_BARCODE + " TEXT NOT NULL, " +
                        COLUMN_NAME_PRICE + " REAL, " +
                        COLUMN_NAME_QUANTITY + " INTEGER, " +
                        COLUMN_NAME_TAX + " REAL, " +
                        COLUMN_NAME_TOTAL + " REAL, " +

                        //FOREIGN KEY(list_id) REFERENCES lists(_id)
                        "FOREIGN KEY(" + COLUMN_NAME_LIST_ID + ") REFERENCES " +
                            Lists.TABLE_NAME + "(" + Lists.COLUMN_NAME_LIST_ID + "), " +
                        //FOREIGN KEY(barcode) REFERENCES products(_barcode)
                        "FOREIGN KEY(" + COLUMN_NAME_BARCODE + ") REFERENCES " +
                            Products.TABLE_NAME + "(" + Products.COLUMN_NAME_BARCODE + "), " +

                        "PRIMARY KEY (" + COLUMN_NAME_LIST_ID + "," + COLUMN_NAME_BARCODE + ")" +
                        ");";

        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
