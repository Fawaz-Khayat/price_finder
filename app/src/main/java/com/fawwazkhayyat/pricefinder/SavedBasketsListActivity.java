package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.util.ArrayList;

public class SavedBasketsListActivity extends AppCompatActivity {
    static final String EXTRA_LIST_ID = "com.fawwazkhayyat.pricefinder.LIST_ID";
    private RecyclerView recyclerView;
    private ToggleButton toggleButton_selectAll;
    private ArrayList<SavedListItem> savedListItems;
    private SavedBasketRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_baskets);

        recyclerView = findViewById(R.id.recyclerView);
        savedListItems = getSavedLists();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavedBasketRecyclerViewAdapter(savedListItems);
        recyclerView.setAdapter(adapter);

        toggleButton_selectAll = findViewById(R.id.toggleButton_selectAll);

    }

    /**
     * toggle check boxes of all list items
     * @param view toggleButton_selectAll
     */
    public void selectAll_click(View view){
        //this listener fires after the toggle button already changed the checked state
        toggleButton_selectAll = (ToggleButton) view;
        boolean isChecked = toggleButton_selectAll.isChecked();
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
    public void delete_click(View view){
        //todo
        for(int i=0;i<savedListItems.size();i++){
            if(savedListItems.get(i).isToggleButtonChecked()){
                //todo
                // delete the corresponding records from SQLite
            }
            //todo
            // notify the adapter of the change
        }
        //todo
        // uncheck all toggle buttons
        // display message to user confirming the delete
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
            String listId_date = listsCursor.getString(listsCursor.getColumnIndexOrThrow("date_time"));
            String localDate = null;
            try {
                localDate = SQLiteContract.Lists.getLocalDate(listId_date);
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
            SavedListItem savedListItem = new SavedListItem(listId_date, localDate, storeName, address);
            savedListItems.add(savedListItem);
            storeCursor.close();
        }
        listsCursor.close();
        db.close();
        return savedListItems;
    }

    public void showSavedBasket(View view){
        LinearLayout linearLayout_infoLayout = (LinearLayout) view;
        TextView textView_listId_date =
                linearLayout_infoLayout.findViewById(R.id.textView_listId_date);
        String listId_date = textView_listId_date.getText().toString();
        Intent intent = new Intent(this,SavedBasketActivity.class);
        intent.putExtra(EXTRA_LIST_ID,listId_date);
        startActivity(intent);
    }
}
