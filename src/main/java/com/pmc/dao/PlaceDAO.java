package com.pmc.dao;

import com.pmc.model.Place;

import java.util.List;

/**
 * Created by Gaetan on 23/02/2015.
 */
public interface PlaceDAO extends DAO{
    public Place findById(int id);
    public Place savePlace();
    public List<Place> findPlacesByPosition(double longitude, double latitude, int radius);
}
