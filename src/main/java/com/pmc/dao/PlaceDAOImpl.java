package com.pmc.dao;

import com.pmc.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import java.util.List;

/**
 * Created by Gaetan on 23/02/2015.
 */
public class PlaceDAOImpl implements PlaceDAOCustom{

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public List<Place> findPlacesByPosition(double latitude, double longitude, int radius){
    //TODO: The request might be optimized
        String request = "FROM Place WHERE " +
                    radius + " > (" + getRequestDistanceCalculatePart(latitude, longitude) + ")";
        return (List<Place>)hibernateTemplate.find(request);
    }

    @Override
    public List<Place> findNearestPlaces(double latitude, double longitude, int maxRadius) { //TODO: optimize request
        String request = "FROM Place WHERE " + getRequestDistanceCalculatePart(latitude, longitude) + "< " + maxRadius +
                        " ORDER BY " +getRequestDistanceCalculatePart(latitude, longitude);// +" LIMIT " + nbPlaces;
        return (List<Place>)hibernateTemplate.find(request);
    }

    @Override
    public Double getOccupationRate(double latitude, double longitude, int radius){
        String request = "SELECT avg(case when isTaken = 1 then 1 else 0 end) FROM Place WHERE " +
                radius + " > (" + getRequestDistanceCalculatePart(latitude, longitude) + ")";
        List<Double> listRes = (List<Double>)hibernateTemplate.find(request);
        if(listRes.isEmpty() || listRes.get(0) == null){
            return null;
        }
        else{
            return listRes.get(0);
        }

    }

    /* Method to get the part of the request that calculate the distance between each place and a position*/
    private String getRequestDistanceCalculatePart(double latitude, double longitude){
        return "(12756274 * ATAN(SQRT(SIN((latitude - " + latitude + " ) * PI() / 180/2) * SIN((latitude - " + latitude + " ) * PI() / 180/2) + " +
                "COS( " + latitude + " * PI() / 180) * COS(latitude * PI() / 180) * " +
                "POWER(SIN((longitude - " + longitude + " ) * PI() / 180/2),2)) , " +
                "SQRT(1-(SIN((latitude - " + latitude + " ) * PI() / 180/2) * SIN((latitude - " + latitude + " ) * PI() / 180/2) + " +
                "COS(" + latitude + " * PI() / 180) * COS(latitude * PI() / 180) * " +
                "POW(SIN((longitude - " + longitude + " ) * PI() / 180/2),2)))))";
    }
}
