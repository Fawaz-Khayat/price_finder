package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
    private ImageButton imageButton_delete;
    private ArrayList<SavedListItem> savedListItems;
    private SavedBasketRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_baskets);

        recyclerView = findViewById(R.id.recyclerView);
        savedListItems = new ArrayList<>();
        generateSavedLists();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavedBasketRecyclerViewAdapter(savedListItems);
        recyclerView.setAdapter(adapter);

        imageButton_delete = findViewById(R.id.imageButton_delete);
        disableButton(imageButton_delete);
        toggleButton_selectAll = findViewById(R.id.toggleButton_selectAll);
    }

    private void disableButton(View view){
        view.setEnabled(false);
        view.setAlpha((float) 0.4);
    }
    private void enableButton(View view){
        view.setEnabled(true);
        view.setAlpha((float) 1.0);
    }
    /**
     * toggle check boxes of all list items
     * @param view toggleButton_selectAll
     */
    public void selectAll_click(View view){
        //this listener fires after the toggle button already changed the checked state
        toggleButton_selectAll = (ToggleButton) view;
        boolean isChecked = toggleButton_selectAll.isChecked();
        //at lease one item selected, enable delete button
        if(isChecked)
            enableButton(imageButton_delete);
        //no item selected, disable delete button
        else
            disableButton(imageButton_delete);
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
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

    for(int i=0;i<savedListItems.size();i++){
        ToggleButton basketItemToggleButton =
                recyclerView.getChildAt(i).findViewById(R.id.toggleButton_select);
        if (basketItemToggleButton.isChecked()){
                // delete the corresponding records from SQLite
                db.delete(
                        SQLiteContract.ListItems.TABLE_NAME,
                        SQLiteContract.ListItems.COLUMN_NAME_LIST_ID + "= ?",
                        new String[]{savedListItems.get(i).getListId_date()});
                db.delete(
                        SQLiteContract.Lists.TABLE_NAME,
                        SQLiteContract.Lists.COLUMN_NAME_DATE_TIME + "= ?",
                        new String[]{savedListItems.get(i).getListId_date()});
            }
        }
        db.close();
        generateSavedLists();
        adapter.notifyDataSetChanged();
        Toast.makeText(this,"Deleted", Toast.LENGTH_SHORT).show();
    }

    void generateSavedLists(){
        savedListItems.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor listsCursor = db.query(true,
                SQLiteContract.Lists.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SQLiteContract.Lists.COLUMN_NAME_DATE_TIME,
                null);
        while (listsCursor.moveToNext()){
            String listId_date = listsCursor.getString(
                    listsCursor.getColumnIndexOrThrow(
                            SQLiteContract.Lists.COLUMN_NAME_DATE_TIME));
            String localDate = null;
            try {
                localDate = SQLiteContract.Lists.getLocalDate(listId_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String storeId = listsCursor.getString(
                    listsCursor.getColumnIndexOrThrow(
                            SQLiteContract.Lists.COLUMN_NAME_STORE_ID));

            Cursor storeCursor = db.query(true,
                    SQLiteContract.Stores.TABLE_NAME,
                    null,
                    SQLiteContract.Stores.COLUMN_NAME_STORE_ID + " = ?",
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
