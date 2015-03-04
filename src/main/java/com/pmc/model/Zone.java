package com.pmc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by stephaneki on 04/03/15.
 */
@Entity
@JsonIgnoreProperties({"date"})
@Table(name="zone")
public class Zone {

    public static enum Density {LOW, MEDIUM, HIGH };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double longitude;
    private double latitude;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime date;

    @Enumerated(EnumType.STRING)
    private Density density;

    public int getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Density getDensity() {
        return density;
    }

    public Zone setDate(DateTime date) {
        this.date = date;
        return this;
    }
}
