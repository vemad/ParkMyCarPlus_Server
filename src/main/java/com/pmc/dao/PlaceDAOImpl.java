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

    /**
     *
     * @param id Id of the place to be found
     * @return the place founded or null otherwise
     */
    @Override
    public Place findById(int id) {
        Place place = hibernateTemplate.get(Place.class, id);
        return place;
    }

    @Override
    //TODO Modify this method to create a realistic place and save it
    public Place savePlace(){
        Place place = new Place();
        place.setLatitude(1.2);
        place.setLongitude(2.3);
        hibernateTemplate.save(place);
        return place;
    }

    /**
     * Desc TODO Fill the description
     * @param   latitude
     * @param   longitude
     * @return   List of all the places contained in the circle specified
     */
    @Override
    public List<Place> findPlacesByPosition(double longitude, double latitude, int radius){ //TODO: The request might be optimized
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
