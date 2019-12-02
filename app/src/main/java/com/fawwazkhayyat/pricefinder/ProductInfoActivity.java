package com.fawwazkhayyat.pricefinder;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class ProductInfoActivity extends AppCompatActivity {
    static final String EXTRA_BARCODE = "com.fawwazkhayyat.pricefinder.BARCODE";
    static final String EXTRA_NAME = "com.fawwazkhayyat.pricefinder.NAME";
    static final String EXTRA_DESCRIPTION = "com.fawwazkhayyat.pricefinder.DESCRIPTION";
    static final String EXTRA_QUANTITY= "com.fawwazkhayyat.pricefinder.QUANTITY";
    static final String EXTRA_PRICE = "com.fawwazkhayyat.pricefinder.PRICE";
    static final String EXTRA_IS_TAXABLE = "com.fawwazkhayyat.pricefinder.IS_TAXABLE";
    static final String EXTRA_IMAGE_PATH = "com.fawwazkhayyat.pricefinder.IMAGE_PATH";

    protected TextView textView_name, textView_description,
            textView_price, textView_quantity, textView_barcode;
    protected ImageView imageView_product;

    protected String storeId, barcode, barcodeType, name, description, imagePath;
    protected double price, tax, total;
    protected int quantity, minmumQuantity;
    protected boolean isTaxable;

    abstract public void addToBasket_click(View view);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        textView_name = findViewById(R.id.textView_name);
        textView_description = findViewById(R.id.textView_description);
        textView_price = findViewById(R.id.textView_price);
        textView_quantity = findViewById(R.id.textView_quantity);
        imageView_product = findViewById(R.id.imageView_product);
        textView_barcode = findViewById(R.id.textView_barcode);
    }

    public void decrease_click(View view){
        if(quantity>minmumQuantity)
            quantity--;
        textView_quantity.setText(String.valueOf(quantity));
        total = quantity * price;
    }
    public void increase_click(View view){
        quantity++;
        textView_quantity.setText(String.valueOf(quantity));
        total = quantity * price;
    }

    protected void getDataFromProduct(Product product){
        barcode = product.getBarcode();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        isTaxable = product.isTaxable();
    }
    protected void populateProductInfo(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        //assume images path in google cloud always = /images/{barcode}.jpg
        StorageReference imageRef = storageReference.child("/images/products/" + barcode + ".jpg");
        GlideApp.with(this)
                .load(imageRef)
                .into(imageView_product);

        textView_name.setText(name);
        textView_description.setText(description);
        textView_price.setText("$"+String.valueOf(price));
        textView_quantity.setText(String.valueOf(quantity));
        textView_barcode.setText(barcode);
    }
}
