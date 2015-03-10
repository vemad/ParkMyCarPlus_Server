package com.pmc.service.PlaceServiceException;

/**
 * Throw when a place is already released
 * Created by Gaetan on 02/03/2015.
 */
public class PlaceAlreadyReleased extends Exception {
    public PlaceAlreadyReleased() {
        super("Place already released");
    }
}
