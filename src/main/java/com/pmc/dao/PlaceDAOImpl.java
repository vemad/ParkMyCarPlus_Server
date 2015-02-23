package com.pmc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
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
    public Place savePlace(){
        Place place = new Place();
        place.setLatitude(1.2);
        place.setLongitude(2.3);
        hibernateTemplate.save(place);
        return place;
    }

    @Override
    public List<Place> findPlacesByPosition(double longitude, double latitude, int radius){ //TODO: maybe optimization request
        String request = "FROM Place WHERE " +
                    radius + " > 12756274 * ATAN(SQRT(SIN((latitude - " + latitude + " ) * PI() / 180/2) * SIN((latitude - " + latitude + " ) * PI() / 180/2) + " +
                    "COS( " + latitude + " * PI() / 180) * COS(latitude * PI() / 180) * " +
                    "POW(SIN((longitude - " + longitude + " ) * PI() / 180/2),2)) , " +
                    "SQRT(1-(SIN((latitude - " + latitude + " ) * PI() / 180/2) * SIN((latitude - " + latitude + " ) * PI() / 180/2) + " +
                    "COS(" + latitude + " * PI() / 180) * COS(latitude * PI() / 180) * " +
                    "POW(SIN((longitude - " + longitude + " ) * PI() / 180/2),2))))";
        return (List<Place>)hibernateTemplate.find(request);
    }
}
