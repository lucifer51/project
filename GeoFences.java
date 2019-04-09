package com.example.lucifer.androidappforcrimereporting;

/**
 * Created by Lucifer on 12/9/2017.
 */

public class GeoFences {
    //nt id;
    String ID;
    double longitude,latitude;
    int Radius;

    public GeoFences(String id, double longitude, double latitude,int    Radius) {
        //this.id = id;
        this.ID = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.Radius = Radius;
    }

    public String getId() {
        return ID;
    }


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getRadius() {
        return Radius;
    }
}
