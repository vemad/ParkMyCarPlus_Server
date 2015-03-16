package com.pmc.service.FavoriteServiceException;

/**
 * Created on 10/03/15.
 *
 * @author stephaneki
 */
public class FavoriteNotFound extends Exception {
    public FavoriteNotFound() {
        super("Favorite not found");
    }
}
