package com.pmc.dao;

import org.hibernate.annotations.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.pmc.model.Place;

import java.util.List;

import javax.transaction.Transactional;

/**
 * Created by Gaetan on 23/02/2015.
 */


@Transactional
public class PlaceDAOImpl implements PlaceDAO{
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Place findById(int id) {
        Place place = hibernateTemplate.get(Place.class, id);
        return place;
    }

    @Override
    public Place savePlace(Place place){
        hibernateTemplate.saveOrUpdate(place);
        return place;
    }

    @Override
    public List<Place> findPlacesByPosition(double latitude, double longitude, int radius){
        String request = "FROM Place WHERE " +
                    radius + " > " + getRequestDistanceCalculatePart(latitude, longitude);
        return (List<Place>)hibernateTemplate.find(request);
    }

    @Override
    public List<Place> findNearestPlaces(double latitude, double longitude, int maxRadius) { //TODO: optimize request
        String request = "FROM Place WHERE " + getRequestDistanceCalculatePart(latitude, longitude) + "< " + maxRadius +
                        " ORDER BY " +getRequestDistanceCalculatePart(latitude, longitude);// +" LIMIT " + nbPlaces;
        return (List<Place>)hibernateTemplate.find(request);
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
