package com.pmc.service;

import com.pmc.model.LogPlace;
import com.pmc.model.Place;
import com.pmc.model.User;

/**
 * Created by stephaneki on 03/03/15.
 */
public interface LogPlaceService {
    public void logPlaceTaken(Place place,User user, double latitude, double longitude);
    public void logPlaceReleased(Place place,User user, double latitude, double longitude);
    public void logPlaceCreated(Place place,User user, double latitude, double longitude);
    public void saveLogPlace(LogPlace logPlace);

}
