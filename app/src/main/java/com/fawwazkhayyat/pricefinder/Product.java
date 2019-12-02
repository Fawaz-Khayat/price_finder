package com.fawwazkhayyat.pricefinder;
//https://developer.android.com/reference/android/os/Parcelable.html
//https://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String barcode;
    private String name;
    private double price;
    private boolean isTaxable;
    private double tax;
    private String imageRefPath;
    private String description;
    private int quantity;

    Product(String barcode){
        this.barcode = barcode;
    }

    private Product(Parcel in){
        barcode = in.readString();
        name = in.readString();
        price = in.readDouble();
        //check reference at the top of the page
        isTaxable = in.readByte() != 0;
        tax = in.readDouble();
        imageRefPath = in.readString();
        description = in.readString();
        quantity = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(barcode);
        dest.writeString(name);
        dest.writeDouble(price);
        //check reference at the top of the page
        dest.writeByte((byte) (isTaxable ? 1 : 0));
        dest.writeDouble(tax);
        dest.writeString(imageRefPath);
        dest.writeString(description);
        dest.writeInt(quantity);
    }

    public static final Parcelable.Creator<Product> CREATOR
            = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };


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

    boolean isTaxable() {
        return isTaxable;
    }

    void setTaxable(boolean taxable) {
        isTaxable = taxable;
    }

    double getTax() {
        return tax;
    }

    void setTax(double tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    int getQuantity() {
        return quantity;
    }

    void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    String getImageRefPath() {
        return imageRefPath;
    }

    void setImageRefPath(String imageRefPath) {
        this.imageRefPath = imageRefPath;
    }
}
