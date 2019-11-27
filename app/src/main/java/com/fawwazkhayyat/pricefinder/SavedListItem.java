package com.fawwazkhayyat.pricefinder;

public class SavedListItem {


    private String date;
    private String storeName;
    private String storeAddress;

    SavedListItem(String date, String storeName, String storeAddress) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.date = date;
    }

    String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
}
