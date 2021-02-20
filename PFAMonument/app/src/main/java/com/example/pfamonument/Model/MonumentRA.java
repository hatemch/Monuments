package com.example.pfamonument.Model;

public class MonumentRA {

    private String mName;
    private double mLatitude ;
    private double mLongitude ;

    public MonumentRA(String newName, double newLatitude, double newLongitude) {
        this.mName = newName;
        this.mLatitude = newLatitude;
        this.mLongitude = newLongitude;
    }

    public String getPoiName() {
        return mName;
    }
    public double getPoiLatitude() {
        return mLatitude;
    }
    public double getPoiLongitude() {
        return mLongitude;
    }

}
