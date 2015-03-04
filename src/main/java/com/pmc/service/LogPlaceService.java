package com.pmc.service;

import com.pmc.model.LogPlace;
import com.pmc.model.Place;

/**
 * Created by stephaneki on 03/03/15.
 */
public interface LogPlaceService {
    public void logPlaceTaken(Place place, double latitude, double longitude);
    public void logPlaceReleased(Place place, double latitude, double longitude);
    public void logPlaceCreated(Place place, double latitude, double longitude);
    public void saveLogPlace(LogPlace logPlace);

}
