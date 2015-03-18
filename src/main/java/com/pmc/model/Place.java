package com.pmc.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="place")
@JsonIgnoreProperties({"taken", "dateCreation"})
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

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY )
    @JoinColumn(name="creator_user_id")
    private User creator;

    public Place(){}

    public Place(User user){
        this.dateCreation = new DateTime();
        System.out.println(this.dateCreation);
        this.creator = user;
    }



    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Timestamp getDateCreation() {
        return new Timestamp(dateCreation.getMillis());
    }

    public Timestamp getDateLastRelease() {
        if(dateLastRelease == null) return null;
        return new Timestamp(dateLastRelease.getMillis());
    }

    public Timestamp getDateLastTake() {
        if(dateLastRelease == null) return null;
        return new Timestamp(dateLastTake.getMillis());
    }



    public Place setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
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
}