package com.pmc.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

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

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "date_creation")
    private DateTime dateCreation;

    @Column(name = "date_last_release")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateLastRelease;

    @Column(name = "date_last_take")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateLastTake;

    @Column(name = "is_taken")
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