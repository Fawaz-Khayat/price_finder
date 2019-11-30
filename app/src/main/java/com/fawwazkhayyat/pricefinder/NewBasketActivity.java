package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NewBasketActivity extends BasketActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ImageButton imageButton_save = findViewById(R.id.imageButton_save);
        imageButton_save.setEnabled(false);
        imageButton_save.setAlpha((float)0.4);
        findViewById(R.id.imageButton_scan).setEnabled(true);

        Intent intent = getIntent();
        storeId = intent.getStringExtra(SelectStoreActivity.EXTRA_STORE_ID);
        storeName = intent.getStringExtra(SelectStoreActivity.EXTRA_STORE_NAME);
        storeAddress = intent.getStringExtra(SelectStoreActivity.EXTRA_STORE_ADDRESS);
        storeTax = intent.getDoubleExtra(SelectStoreActivity.EXTRA_STORE_TAX,-1.00);
        //todo
        // check if storeTax <= 0
    }
    public void scan_click(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan(); // `this` is the current Activity
    }
    // Get the scan results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        textView_result.setText("");
        switch (requestCode){
            case IntentIntegrator.REQUEST_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if(result != null) {
                    if(result.getContents() == null) {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else {
                        String barcode = result.getContents();
                        //todo
                        // remove manual assignment to barcode
                        //762111898173 in ub store
                        //barcode = "762111898173";
                        String barcodeType = result.getFormatName();
                        // check if the product already in the basket
                        // if already in the basket, edit
                        // else, add
                        int productIndex = getProductPosition(barcode);
                        if(productIndex<0)
                            getProductInfo(storeId, barcode, barcodeType);
                        else
                            editProduct(productIndex);
                        //todo
                        // remove textView_result from layout
                        textView_result.setText(barcode);
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            //request code for new product info
            case REQUEST_CODE_ADD:
                Log.d("DEBUG_TAG", "onActivityResult: product info");
                switch (resultCode) {
                    case RESULT_CANCELED:
                        break;

                    case ProductInfoGetterActivity.RESULT_NOT_FOUND:
                        Toast.makeText(this,
                                "Product was not found in this store!",
                                Toast.LENGTH_LONG)
                                .show();
                        textView_result.setText("Sorry! Product was not found in this store!");
                        break;

                        default:
                    Product product = new Product(data.getStringExtra(ProductInfoActivity.EXTRA_BARCODE));
                    product.setName(data.getStringExtra(ProductInfoActivity.EXTRA_NAME));
                    product.setDescription(data.getStringExtra(ProductInfoActivity.EXTRA_DESCRIPTION));
                    product.setPrice(data.getDoubleExtra(ProductInfoActivity.EXTRA_PRICE, 0.0));
                    if (data.hasExtra(ProductInfoActivity.EXTRA_IS_TAXABLE)) {
                        product.setTaxable(data.getBooleanExtra(ProductInfoActivity.EXTRA_IS_TAXABLE, true));
                        if (product.isTaxable())
                            product.setTax(storeTax);
                    } else
                        Log.d("DEBUG_TAG", "onActivityResult: data has no EXTRA_IS_TAXABLE");
                    //todo
                    // handle the case of no taxable information
                    product.setQuantity(data.getIntExtra(ProductInfoActivity.EXTRA_QUANTITY, 0));
                    product.setImageRefPath(data.getStringExtra(ProductInfoActivity.EXTRA_IMAGE_PATH));
                    products.add(product);
                    adapter.notifyItemInserted(products.size());

                    recalculateBasket();
                    ImageButton imageButton_save = findViewById(R.id.imageButton_save);
                    imageButton_save.setEnabled(true);
                    imageButton_save.setAlpha((float) 1.0);
                    break;
                }
            // request code for edited product info
            case REQUEST_CODE_EDIT:
                if(resultCode == RESULT_CANCELED)
                    break;
                int position = data.getIntExtra(ProductInfoEditorActivity.EXTRA_POSITION,-1);
                int quantity = data.getIntExtra(ProductInfoEditorActivity.EXTRA_QUANTITY, -1);
                //todo
                // check if either position or quantity < 0
                if(quantity==0){
                    products.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                else{
                    products.get(position).setQuantity(quantity);
                    adapter.notifyItemChanged (position);
                }
                recalculateBasket();
                break;
        }
    }

    /**
     * Check if the product exists in the product list,
     * @param barcode product barcode
     * @return if exists, return the position of the product in the ArrayList
     *      * else, return -1
     */
    private int getProductPosition(String barcode){
        for(int i=0;i<products.size();i++){
            if(products.get(i).getBarcode().equals(barcode)){
                return i;
            }
        }
        return -1;
    }

    private void getProductInfo(String storeId, String  barcode, String barcodeType){
        Intent intent = new Intent(this, ProductInfoGetterActivity.class);
        intent.putExtra(SelectStoreActivity.EXTRA_STORE_ID,storeId);
        intent.putExtra(EXTRA_BARCODE,barcode);
        intent.putExtra(EXTRA_BARCODE_TYPE,barcodeType);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    /**
     * start ProductInfoEditorActivity to edit the quantity
     * result should be available inside onResult listener
     * @param position position of the product in the products ArrayList
     */
    private void editProduct(int position){
        Intent intent = new Intent(this, ProductInfoEditorActivity.class);
        String barcode = products.get(position).getBarcode();
        String name = products.get(position).getName();
        String description = products.get(position).getDescription();
        double price = products.get(position).getPrice();
        int quantity = products.get(position).getQuantity();
        String imageRefPath = products.get(position).getImageRefPath();

        intent.putExtra(EXTRA_POSITION,position);
        intent.putExtra(EXTRA_BARCODE, barcode);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_PRICE, price);
        intent.putExtra(EXTRA_QUANTITY, quantity);
        intent.putExtra(EXTRA_IMAGE_PATH, imageRefPath);

        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    public void item_click(View view){
        int position = (int)view.getTag();
        editProduct(position);
    }

    public void save_click(View view){
        ImageButton imageButton_save = findViewById(R.id.imageButton_save);
        imageButton_save.setEnabled(false);
        imageButton_save.setAlpha((float)0.4);
        String list_id;
        //todo
        // save basket items to the local SQLite database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // insert data into stores table
        ContentValues storesValues = new ContentValues(1);
        storesValues.put(SQLiteContract.Stores.COLUMN_NAME_STORE_ID,storeId);
        storesValues.put(SQLiteContract.Stores.COLUMN_NAME_NAME,storeName);
        storesValues.put(SQLiteContract.Stores.COLUMN_NAME_ADDRESS, storeAddress);

        db.insertWithOnConflict(SQLiteContract.Stores.TABLE_NAME,
                null,
                storesValues,
                SQLiteDatabase.CONFLICT_IGNORE);

        Log.d("DEBUG_TAG", "save_click: Inserting row into stores table");

        // insert data into products table
        ContentValues productsValues = new ContentValues(products.size());
        for(int i=0;i<products.size();i++){
            productsValues.put(SQLiteContract.Products.COLUMN_NAME_BARCODE,products.get(i).getBarcode());
            productsValues.put(SQLiteContract.Products.COLUMN_NAME_DESCRIPTION,products.get(i).getDescription());
            db.insertWithOnConflict(SQLiteContract.Products.TABLE_NAME,
                    null,
                    productsValues,
                    SQLiteDatabase.CONFLICT_IGNORE);
        }

        // insert data into lists table
        list_id = SQLiteContract.Lists.getNewDate();
        ContentValues listsValues = new ContentValues(1);
        listsValues.put(SQLiteContract.Lists.COLUMN_NAME_DATE_TIME,
                list_id);
        listsValues.put(SQLiteContract.Lists.COLUMN_NAME_STORE_ID, storeId);
        db.insert(SQLiteContract.Lists.TABLE_NAME,
                null,
                listsValues);

        // insert data into list_items table
        ContentValues listsItemsValues = new ContentValues(products.size());
        listsItemsValues.put(SQLiteContract.ListItems.COLUMN_NAME_LIST_ID,list_id);
        for(int i=0;i<products.size();i++){
            listsItemsValues.put(SQLiteContract.ListItems.COLUMN_NAME_BARCODE,products.get(i).getBarcode());
            listsItemsValues.put(SQLiteContract.ListItems.COLUMN_NAME_PRICE,products.get(i).getPrice());
            listsItemsValues.put(SQLiteContract.ListItems.COLUMN_NAME_QUANTITY,products.get(i).getQuantity());
            listsItemsValues.put(SQLiteContract.ListItems.COLUMN_NAME_TAX,products.get(i).getTax());
            db.insert(SQLiteContract.ListItems.TABLE_NAME,
                    null,
                    listsItemsValues);
        }
        db.close();

        Toast.makeText(this,"Basket is saved!",Toast.LENGTH_LONG).show();
    }
}
