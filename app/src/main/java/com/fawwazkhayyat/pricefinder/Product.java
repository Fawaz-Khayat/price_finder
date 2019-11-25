package com.fawwazkhayyat.pricefinder;

import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Product {
    private String barcode;
    private String name;
    private double price;
    private String imageRefPath;
    private String description;
    private int quantity;

    Product(String barcode){
        this.barcode = barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageRefPath() {
        return imageRefPath;
    }

    public void setImageRefPath(String imageRefPath) {
        this.imageRefPath = imageRefPath;
    }
}
