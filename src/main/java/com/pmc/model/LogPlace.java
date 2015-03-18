package com.pmc.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Gaetan on 02/03/2015.
 */
@Entity(name="logplace")
public class LogPlace {
    public static enum Action {create, release, take};

    private  static final String EUROPE_PARIS = "Europe/Paris";

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

    @ManyToOne(cascade = {CascadeType.DETACH} )
    @JoinColumn(name="user_id")
    private User user;

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime",
            parameters = {@org.hibernate.annotations.Parameter(name = "databaseZone", value = EUROPE_PARIS)})
    private DateTime date;

    @Column
    private double latitude;
    @Column
    private double longitude;

    public LogPlace(Place place, Action action, User user, double latitude, double longitude) {
        this.place = place;
        this.action = action;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = new DateTime();
        this.user = user;
    }

    public LogPlace() {
    }
}
