package com.util;

/**
 * Created by stephaneki on 24/02/15.
 */
public class Position {

    protected double latitude;
    protected double longitude;

    public Position setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Position setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
