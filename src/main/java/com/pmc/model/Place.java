package com.pmc.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="place")
@JsonIgnoreProperties({"taken"})
public class Place implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private double latitude;
    private double longitude;

    @JsonProperty("isTaken")
    private boolean isTaken;


    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public Place setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Place setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public boolean isTaken() { return isTaken; }

    public Place takePlace() {
        this.isTaken = true;
        return this;
    }
    public Place releasePlace() {
        this.isTaken = false;
        return this;
    }
}