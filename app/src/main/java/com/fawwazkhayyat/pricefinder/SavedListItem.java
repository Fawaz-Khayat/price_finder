package com.fawwazkhayyat.pricefinder;

public class SavedListItem {

    private String listId_date;
    private String LocalDate;
    private String storeName;
    private String storeAddress;
    private boolean toggleButtonChecked;

    SavedListItem(String listId_date, String localDate, String storeName, String storeAddress) {
        this.listId_date = listId_date;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.LocalDate = localDate;
        this.toggleButtonChecked = false;
    }
    public String getListId_date() {
        return listId_date;
    }

    public void setListId_date(String listId_date) {
        this.listId_date = listId_date;
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

    String getLocalDate() {
        return LocalDate;
    }

    public void setLocalDate(String date) {
        this.LocalDate = date;
    }

    String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
}
