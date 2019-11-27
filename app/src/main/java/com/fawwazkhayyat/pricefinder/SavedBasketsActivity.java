package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.util.ArrayList;

public class SavedBasketsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ToggleButton toggleButton_selectAll;
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

        toggleButton_selectAll = findViewById(R.id.toggleButton_selectAll);
        //toggle check boxes of all list items
        toggleButton_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for(int i=0;i<savedListItems.size();i++){
                        savedListItems.get(i).setToggleButtonChecked(isChecked);
                    }
                    // https://stackoverflow.com/questions/43221847/cannot-call-this-method-while-recyclerview-is-computing-a-layout-or-scrolling-wh
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
            }
        });
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
