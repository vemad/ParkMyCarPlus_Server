package com.pmc.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="place")
@JsonIgnoreProperties({"taken", "dateCreation", "dateLastRelease", "dateLastTake"})
public class Place implements Serializable{

    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    /*
    Degrees representing the position of the place
     */
    @Column
    private double latitude;
    @Column
    private double longitude;

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateCreation;

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateLastRelease;

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateLastTake;

    @Column
    @JsonProperty("isTaken")
    private boolean isTaken;

    public Place(){
        this.dateCreation = new DateTime();
    }

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
        this.dateLastTake = new DateTime();
        this.isTaken = true;
        return this;
    }
    public Place releasePlace() {
        this.dateLastRelease = new DateTime();
        this.isTaken = false;
        return this;
    }

    public DateTime getDateCreation() {
        return dateCreation;
    }

    public DateTime getDateLastRelease() {
        return dateLastRelease;
    }

    public DateTime getDateLastTake() {
        return dateLastTake;
    }
}