package com.pmc.service;

import com.pmc.dao.PlaceDAO;
import com.pmc.model.Place;
import com.pmc.model.User;
import com.pmc.service.PlaceServiceException.PlaceAlreadyReleased;
import com.pmc.service.PlaceServiceException.PlaceAlreadyTaken;
import com.pmc.service.PlaceServiceException.PlaceNotFound;
import com.pmc.service.PlaceServiceException.PlaceNotUsedByUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Gaetan on 02/03/2015.
 */
@Service(value = "placeService")
public class PlaceServiceImpl implements PlaceService {

    /*Some Parameters*/
    private static final int MAX_RADIUS_OF_PLACE = 3;
    private static final int MAX_RADIUS_ALLOW_TO_RELEASE_PLACE = 10;

    private static final int SCORE_ADDED_WHEN_TAKEN = 5;
    private static final int SCORE_ADDED_WHEN_RELEASED = 5;
    private static final int SCORE_ADDED_WHEN_RELEASED_NOT_ON_PLACE = -5; //TODO refactoring
    private static final int SCORE_ADDED_WHEN_TAKEN_NEW_PLACE = 10;

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

        Place placeReleased = user.getPlace();
        if(placeReleased == null){
            throw new PlaceNotUsedByUser();
        }
        else if(placeReleased.isTaken()){
            //Release the place of the user
            placeReleased.releasePlace();
            placeDAO.save(placeReleased);

            //Check if the place is really around the user for the score
            List<Place> placesAroundUser = placeDAO.findNearestPlaces(latitude, longitude, MAX_RADIUS_ALLOW_TO_RELEASE_PLACE);
            boolean userIsAroundPlace = false;
            for(Place p : placesAroundUser){
                if(p.getId() == placeReleased.getId()){
                    userIsAroundPlace=true;
                    break;
                }
            }
            if(userIsAroundPlace){
                userService.releasePlace(user, SCORE_ADDED_WHEN_RELEASED);
            }
            else{
                userService.releasePlace(user, SCORE_ADDED_WHEN_RELEASED_NOT_ON_PLACE);
            }

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
            userService.takePlace(user, placeTaken, SCORE_ADDED_WHEN_TAKEN_NEW_PLACE);

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
            userService.takePlace(user, placeTaken, SCORE_ADDED_WHEN_TAKEN);

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
        return placeDAO.findNearestPlaces(latitude, longitude, MAX_RADIUS_OF_PLACE);
    }

}
