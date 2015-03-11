package com.pmc.service;

import com.pmc.dao.PlaceDAO;
import com.pmc.model.Place;
import com.pmc.model.User;
import com.pmc.service.PlaceServiceException.PlaceAlreadyReleased;
import com.pmc.service.PlaceServiceException.PlaceAlreadyTaken;
import com.pmc.service.PlaceServiceException.PlaceNotFound;
import com.pmc.service.PlaceServiceException.PlaceNotUsedByUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Gaetan on 02/03/2015.
 */
@Service(value = "placeService")
public class PlaceServiceImpl implements PlaceService {

    /*Some Parameters*/
    private static final int maxRadiusOfPlace = 3;

    @Resource
    private LogPlaceService logPlaceService;

    @Resource
    private CustomUserDetailsService userService;

    @Resource
    private PlaceDAO placeDAO;

    public PlaceServiceImpl() {
    }

    public Place getPlaceById(int id){
        return placeDAO.findOne(id);
    }

    public void deletePlaceById(int id){
        placeDAO.delete(id);
    }

    public Place releasePlace(double latitude, double longitude, User user) throws PlaceNotFound, PlaceAlreadyReleased, PlaceNotUsedByUser {
        //Place placeReleased = findPlaceByPosition(latitude, longitude);
        Place placeReleased = user.getPlace();
        if(placeReleased == null){
            throw new PlaceNotUsedByUser();
        }
        else if(placeReleased.isTaken()){

            placeReleased.releasePlace();
            placeDAO.save(placeReleased);
            userService.releasePlace(user);

            //Log the event
            logPlaceService.logPlaceReleased(placeReleased,user, latitude, longitude);

            return placeReleased;
        }
        else{
            throw new PlaceAlreadyReleased();
        }
    }

    public Place takePlace(double latitude, double longitude, User user) throws PlaceAlreadyTaken {

        Place placeTaken = user.getPlace();
        if(placeTaken != null){
            throw new PlaceAlreadyTaken();
        }

        try {
            placeTaken = findPlaceByPosition(latitude, longitude);

        } catch (PlaceNotFound placeNotFound) {
            //If no place found,create one
            placeTaken = new Place(user);
            placeTaken.setLatitude(latitude).setLongitude(longitude).takePlace();
            placeDAO.save(placeTaken);
            userService.takePlace(user, placeTaken);

            //Log the event
            logPlaceService.logPlaceCreated(placeTaken,user, latitude, longitude);
            logPlaceService.logPlaceTaken(placeTaken,user, latitude, longitude);

            return placeTaken;
        }

        if(placeTaken.isTaken()){
            throw new PlaceAlreadyTaken();
        }
        else{
            placeTaken.takePlace();
            placeDAO.save(placeTaken);
            userService.takePlace(user, placeTaken);

            //Log the event
            logPlaceService.logPlaceTaken(placeTaken, user, latitude, longitude);

            return placeTaken;
        }
    }

    public List<Place> listPlacesByPosition(double latitude, double longitude, int radius){
        return placeDAO.findPlacesByPosition(latitude, longitude, radius);
    }

    public Place findPlaceByPosition(double latitude, double longitude) throws PlaceNotFound {
        List<Place> listPlace = findNearestPlacesByPosition(latitude, longitude);
        if(listPlace.isEmpty()){
            throw new PlaceNotFound();
        }
        else{
            return listPlace.get(0);
        }
    }

    public List<Place> findNearestPlacesByPosition(double latitude, double longitude){
        return placeDAO.findNearestPlaces(latitude, longitude, maxRadiusOfPlace);
    }

}
