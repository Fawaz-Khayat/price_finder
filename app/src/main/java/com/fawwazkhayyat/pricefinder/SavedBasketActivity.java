package com.fawwazkhayyat.pricefinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class SavedBasketActivity extends BasketActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.imageButton_save).setVisibility(View.GONE);
        findViewById(R.id.imageButton_scan).setVisibility(View.GONE);
        textView_result.setVisibility(View.GONE);

        Intent intent = getIntent();
        String listId_date = intent.getStringExtra(SavedBasketsListActivity.EXTRA_LIST_ID);
        updateProductsList(listId_date);
        adapter.notifyDataSetChanged();
        recalculateBasket();
    }

    void updateProductsList(String listId_date){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor listItemCursor = db.query(true,
                SQLiteContract.ListItems.TABLE_NAME,
                null,
                SQLiteContract.ListItems.COLUMN_NAME_LIST_ID + "=?",
                new String[]{listId_date},
                null,
                null,
                null,
                null);
        while (listItemCursor.moveToNext()){
            String barcode;
            double price, tax;
            int quantity;

            barcode = listItemCursor.getString(
                    listItemCursor.getColumnIndexOrThrow(
                            SQLiteContract.ListItems.COLUMN_NAME_BARCODE));
            price = listItemCursor.getDouble(
                    listItemCursor.getColumnIndexOrThrow(
                            SQLiteContract.ListItems.COLUMN_NAME_PRICE));
            tax = listItemCursor.getDouble(
                    listItemCursor.getColumnIndexOrThrow(
                            SQLiteContract.ListItems.COLUMN_NAME_TAX));
            quantity = listItemCursor.getInt(
                    listItemCursor.getColumnIndexOrThrow(
                            SQLiteContract.ListItems.COLUMN_NAME_QUANTITY));

            Cursor productCursor = db.query(
                    true,
                    SQLiteContract.Products.TABLE_NAME,
                    null,
                    SQLiteContract.Products.COLUMN_NAME_BARCODE + "=?",
                    new String[]{barcode},
                    null,
                    null,
                    null,
                    null);

            productCursor.moveToFirst();
            String description = productCursor.getString(
                    productCursor.getColumnIndexOrThrow(
                            SQLiteContract.Products.COLUMN_NAME_DESCRIPTION));

            Product product = new Product(barcode);
            product.setPrice(price);
            product.setTax(tax);
            if(tax>0)
                product.setTaxable(true);
            else
                product.setTaxable(false);
            product.setQuantity(quantity);
            product.setDescription(description);

            products.add(product);

            productCursor.close();
        }
        listItemCursor.close();
        db.close();
    }

    @Override
    public void item_click(View view){

    }
}
