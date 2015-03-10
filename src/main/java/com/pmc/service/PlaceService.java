package com.pmc.service;

import com.pmc.model.Place;
import com.pmc.model.User;
import com.pmc.service.PlaceServiceException.PlaceAlreadyReleased;
import com.pmc.service.PlaceServiceException.PlaceAlreadyTaken;
import com.pmc.service.PlaceServiceException.PlaceNotFound;

import java.util.List;

/**
 * Created by stephaneki on 03/03/15.
 */
public interface PlaceService {
    public Place getPlaceById(int id);
    public void deletePlaceById(int id);
    public Place takePlace(double latitude, double longitude,User user) throws PlaceAlreadyTaken;
    public List<Place> listPlacesByPosition(double latitude, double longitude, int radius);
    public Place releasePlace(double latitude, double longitude, User user) throws PlaceNotFound, PlaceAlreadyReleased;
    public List<Place> findNearestPlacesByPosition(double latitude, double longitude);

}
