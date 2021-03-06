package com.util;

/**
 * Object used to collect position send in the body request sent by the client
 * @author stephaneki
 */
public class Position {

    protected double latitude;
    protected double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Position() {
    }

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


}
