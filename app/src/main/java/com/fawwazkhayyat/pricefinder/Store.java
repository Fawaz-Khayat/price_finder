package com.fawwazkhayyat.pricefinder;

import com.google.firebase.firestore.GeoPoint;

public class Store {
    private String id;
    private String name;
    private String address;
    private String country;
    private String province;
    private GeoPoint geoPoint;
    private double tax;

    Store(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    void setProvince(String province) {
        this.province = province;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    void setGeopoint(GeoPoint geopoint) {
        this.geoPoint = geopoint;
    }

    double getTax() {
        return tax;
    }

    void setTax(double tax) {
        this.tax = tax;
    }
}

