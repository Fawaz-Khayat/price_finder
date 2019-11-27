package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.text.ParseException;
import java.util.ArrayList;

public class SavedBasketsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    //ArrayList<SavedListItem> savedListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_baskets);

        recyclerView = findViewById(R.id.recyclerView);

        ArrayList<SavedListItem> savedListItems = getSavedLists();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SavedBasketRecyclerViewAdapter adapter = new SavedBasketRecyclerViewAdapter(savedListItems);
        recyclerView.setAdapter(adapter);

    }

    ArrayList<SavedListItem> getSavedLists(){
        ArrayList<SavedListItem> savedListItems = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor listsCursor = db.query(true,
                SQLiteContract.Lists.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "date_time",
                null);
        while (listsCursor.moveToNext()){
            String date = listsCursor.getString(listsCursor.getColumnIndexOrThrow("date_time"));
            String localDate = null;
            try {
                localDate = SQLiteContract.Lists.getLocalDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String storeId = listsCursor.getString(listsCursor.getColumnIndexOrThrow("store_id"));

            Cursor storeCursor = db.query(true,
                    SQLiteContract.Stores.TABLE_NAME,
                    null,
                    "_id = ?",
                    new String[]{storeId},
                    null,
                    null,
                    null,
                    null);
            storeCursor.moveToFirst();
            String storeName = storeCursor.getString(storeCursor.getColumnIndexOrThrow("name"));
            String address = storeCursor.getString(storeCursor.getColumnIndexOrThrow("address"));
            SavedListItem savedListItem = new SavedListItem(localDate, storeName, address);
            savedListItems.add(savedListItem);
            storeCursor.close();
        }

        listsCursor.close();
        db.close();
        return savedListItems;
    }
}
