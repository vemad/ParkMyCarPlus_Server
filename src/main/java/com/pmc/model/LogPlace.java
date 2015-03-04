package com.pmc.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Gaetan on 02/03/2015.
 */
@Entity(name="logplace")
public class LogPlace {
    public static enum Action {create, release, take};

    @Id
    @Column
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne( cascade = {CascadeType.DETACH} )
    @JoinColumn(name="place_id")
    private Place place;

    @Column
    @Enumerated(EnumType.STRING)
    private Action action;

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime date;

    @Column
    private double latitude;
    @Column
    private double longitude;

    public LogPlace(Place place, Action action, double latitude, double longitude) {
        this.place = place;
        this.action = action;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = new DateTime();
    }
}
