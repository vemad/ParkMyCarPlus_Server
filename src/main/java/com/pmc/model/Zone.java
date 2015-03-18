package com.pmc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private  static final String EUROPE_PARIS = "Europe/Paris";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double longitude;
    private double latitude;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime",
            parameters = {@org.hibernate.annotations.Parameter(name = "databaseZone", value = EUROPE_PARIS)})
    private DateTime date;

    @Enumerated(EnumType.STRING)
    private Density density;

    @Transient
    @JsonProperty("intensity")
    private float intensity;

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

    public Zone setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Zone setDensity(Density density) {
        this.density = density;
        return this;
    }

    public Zone setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Zone setDate(DateTime date) {
        this.date = date;
        return this;
    }

    public Zone setIntensity(float intensity) {
        this.intensity = intensity;
        return this;
    }
}
