package com.pmc.dao;

import com.pmc.model.Zone;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by stephaneki on 04/03/15.
 */
public class ZoneDAOImpl implements ZoneDAOCustom {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public List<Zone> findZonesByPositionAfterDate(double latitude, double longitude, DateTime date, int radius) {
        return findZonesByPositionBetweenDates(latitude, longitude, date, new DateTime(), radius);
    }
    @Override
    public List<Zone> allZones() {
        //TODO: The request might be optimized
        String request = "FROM Zone";
        return (List<Zone>)hibernateTemplate.find(request);
    }

    @Override
    public List<Zone> findZonesByPositionBetweenDates(double latitude, double longitude, DateTime dateStart, DateTime dateStop, int radius) {
        //TODO: The request might be optimized
        String request = "FROM Zone WHERE " +
                radius + " > (" + getRequestDistanceCalculatePart(latitude, longitude) + ")" +
                " AND '" + new Timestamp(dateStart.getMillis()) + "' < date" +
                " AND '" + new Timestamp(dateStop.getMillis()) + "' > date" ;
        return (List<Zone>)hibernateTemplate.find(request);
    }

    //This methods retrieves all zones declared on the same day of the week and in the interval of an hour.
    @Override
    public List<Zone> findZonesOfHourAndDay(double latitude, double longitude, DateTime date, int radius){
        Timestamp currentDate = new Timestamp(date.getMillis());
        String request = "FROM Zone WHERE " +
                radius + " > (" + getRequestDistanceCalculatePart(latitude, longitude) + ")" +
                " AND WEEKDAY('" + currentDate + "') = WEEKDAY(date)" +
                " AND HOUR('" + currentDate + "')*60+MINUTE('" + currentDate + "')-30 < HOUR(date)*60+MINUTE(date)" +
                " AND HOUR('" + currentDate + "')*60+MINUTE('" + currentDate + "')+30 > HOUR(date)*60+MINUTE(date)";
        return (List<Zone>)hibernateTemplate.find(request);
    }


    /* Method to get the part of the request that calculate the distance between each place and a position*/
    private String getRequestDistanceCalculatePart(double latitude, double longitude){
        return "(12756274 * ATAN(SQRT(SIN((latitude - " + latitude + " ) * PI() / 180/2) * SIN((latitude - " + latitude + " ) * PI() / 180/2) + " +
                "COS( " + latitude + " * PI() / 180) * COS(latitude * PI() / 180) * " +
                "POW(SIN((longitude - " + longitude + " ) * PI() / 180/2),2)) , " +
                "SQRT(1-(SIN((latitude - " + latitude + " ) * PI() / 180/2) * SIN((latitude - " + latitude + " ) * PI() / 180/2) + " +
                "COS(" + latitude + " * PI() / 180) * COS(latitude * PI() / 180) * " +
                "POW(SIN((longitude - " + longitude + " ) * PI() / 180/2),2)))))";
    }
}
