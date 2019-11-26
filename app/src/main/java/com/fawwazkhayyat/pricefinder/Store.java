package com.fawwazkhayyat.pricefinder;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeopoint(GeoPoint geopoint) {
        this.geoPoint = geopoint;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
}

