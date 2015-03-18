package com.pmc.dao;

import com.pmc.model.Place;

import java.util.List;

/**
 * Created by stephaneki on 03/03/15.
 */
public interface PlaceDAOCustom {
    public List<Place> findPlacesByPosition(double latitude, double longitude, int radius);
    public List<Place> findNearestPlaces(double latitude, double longitude, int maxRadius);
    public Double getOccupationRate(double latitude, double longitude, int radius);
}
