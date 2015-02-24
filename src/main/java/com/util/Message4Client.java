package com.util;

/**
 * Created by stephaneki on 24/02/15.
 */
public class Message4Client {

    protected String message;

    public Message4Client(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public Message4Client setMessage(String message) {
        this.message = message;
        return this;
    }

}
