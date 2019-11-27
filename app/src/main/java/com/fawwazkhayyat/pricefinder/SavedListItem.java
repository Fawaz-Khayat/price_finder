package com.fawwazkhayyat.pricefinder;

public class SavedListItem {


    private String date;
    private String storeName;
    private String storeAddress;
    private boolean toggleButtonChecked;

    SavedListItem(String date, String storeName, String storeAddress) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.date = date;
        this.toggleButtonChecked = false;
    }
    boolean isToggleButtonChecked() {
        return toggleButtonChecked;
    }

    public void setToggleButtonChecked(boolean toggleButtonChecked) {
        this.toggleButtonChecked = toggleButtonChecked;
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
