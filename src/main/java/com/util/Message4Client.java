package com.util;

/**
 * Created by stephaneki on 24/02/15.
 */
public class Message4Client {

    protected final Double latitude;
    protected final Integer message;

    public Message4Client(Double latitude, Integer message) {
        this.latitude = latitude;
        this.message = message;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "{" +
                "latitude=" + latitude +
                ", message='" + message + '\'' +
                '}';
    }
}
