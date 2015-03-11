package com.pmc.service.PlaceServiceException;

/**
 * Created by Gaetan on 10/03/2015.
 */
public class PlaceNotUsedByUser extends Exception {
    public PlaceNotUsedByUser() {
        super("Place not used by this user");
    }
}
