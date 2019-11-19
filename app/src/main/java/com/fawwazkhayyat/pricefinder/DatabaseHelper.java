package com.fawwazkhayyat.pricefinder;
//https://developer.android.com/training/data-storage/sqlite#java

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "basket.db";

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteContract.Stores.SQL_CREATE_TABLE);
        db.execSQL(SQLiteContract.Products.SQL_CREATE_TABLE);
        db.execSQL(SQLiteContract.Lists.SQL_CREATE_TABLE);
        db.execSQL(SQLiteContract.ListItems.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLiteContract.ListItems.SQL_DELETE_TABLE);
        db.execSQL(SQLiteContract.Lists.SQL_DELETE_TABLE);
        db.execSQL(SQLiteContract.Products.SQL_DELETE_TABLE);
        db.execSQL(SQLiteContract.Stores.SQL_DELETE_TABLE);

        onCreate(db);
    }
}
