package com.pmc.service;

import com.pmc.dao.DAOManager;
import com.pmc.dao.PlaceDAO;
import com.pmc.model.Place;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.pmc.service.PlaceServiceException.*;

/**
 * Created by Gaetan on 02/03/2015.
 */
public class PlaceService {

    /*Some Parameters*/
    private static final int maxRadiusOfPlace = 3;



    /*
    * PlaceService is a singleton
    */
    private static PlaceService placeService = new PlaceService();

    public static PlaceService getInstance(){
        return placeService;
    }

    private PlaceService(){}

    public Place getPlaceById(int id){
        PlaceDAO placeDAO = (PlaceDAO) DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        return placeDAO.findById(id);
    }

    public boolean deletePlaceById(int id){
        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        return placeDAO.deleteById(id);
    }

    public Place releasePlace(double latitude, double longitude) throws PlaceNotFound, PlaceAlreadyReleased {

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);

        Place placeReleased = findPlaceByPosition(latitude, longitude);;
        if(placeReleased.isTaken()){
            placeReleased.releasePlace();
            placeDAO.savePlace(placeReleased);

            //Log the event
            LogPlaceService.getInstance().logPlaceReleased(placeReleased, latitude, longitude);

            return placeReleased;
        }
        else{
            throw new PlaceAlreadyReleased();
        }
    }

    public Place takePlace(double latitude, double longitude) throws PlaceAlreadyTaken {

        Place placeTaken = null;
        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);

        try {
            placeTaken = findPlaceByPosition(latitude, longitude);

        } catch (PlaceNotFound placeNotFound) {
            //If no place found,create one
            placeTaken = new Place();
            placeTaken.setLatitude(latitude).setLongitude(longitude).takePlace();
            placeDAO.savePlace(placeTaken);

            //Log the event
            LogPlaceService.getInstance().logPlaceCreated(placeTaken, latitude, longitude);
            LogPlaceService.getInstance().logPlaceTaken(placeTaken, latitude, longitude);

            return placeTaken;
        }

        if(placeTaken.isTaken()){
            throw new PlaceAlreadyTaken();
        }
        else{
            placeTaken.takePlace();
            placeDAO.savePlace(placeTaken);

            //Log the event
            LogPlaceService.getInstance().logPlaceTaken(placeTaken, latitude, longitude);

            return placeTaken;
        }
    }

    public List<Place> listPlacesByPosition(double latitude, double longitude, int radius){

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        List<Place> listPlace = placeDAO.findPlacesByPosition(latitude, longitude, radius);
        return listPlace;
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
        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        return placeDAO.findNearestPlaces(latitude, longitude, maxRadiusOfPlace);
    }

}
