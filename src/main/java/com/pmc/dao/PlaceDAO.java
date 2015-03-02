package com.pmc.dao;

import com.pmc.model.Place;

import java.util.List;

/**
 * Created by Gaetan on 23/02/2015.
 */
public interface PlaceDAO extends DAO{
    public Place findById(int id);
    public boolean deleteById(int id);
    public Place savePlace(Place place);
    public List<Place> findPlacesByPosition(double latitude, double longitude, int radius);
    public List<Place> findNearestPlaces(double latitude, double longitude, int maxRadius);
}
