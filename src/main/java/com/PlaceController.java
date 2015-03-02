package com;

import com.pmc.config.DAOManager;
import com.pmc.dao.PlaceDAO;
import com.pmc.model.Place;
import com.util.*;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import sun.rmi.runtime.Log;

/**
 * @author Stephane KI & Gaëtan DESHAYES
 */
@RestController
public class PlaceController {

    private static final int defaultRadius = 100;
    private static final int maxRadius = 1000;
    private static final int maxRadiusOfPlace = 3;

    /**
     * Find a place by its id
     * @param id : id of the place to find
     * @return The place founded or false otherwise
     * TODO Authentication for
     */
    @RequestMapping("/place")
    public Place place(@RequestParam(value="id") int id) {

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);

        return placeDAO.findById(id);
    }

    @RequestMapping("/delete")
    public boolean deletePlace(@RequestParam(value="id") int id) {

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);

        return placeDAO.deleteById(id);
    }



    @RequestMapping(value ="/place/released", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Place> placeReleased(@RequestBody Position position ){

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        List<Place> listNearestPlace = placeDAO.findNearestPlaces(position.getLatitude(), position.getLongitude(), maxRadiusOfPlace);

        //If no nearest place found do nothing
        if(listNearestPlace.isEmpty()){
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        else{ //else release the first: the nearest place
            Place place = listNearestPlace.get(0);
            if(place.isTaken()){
                place.releasePlace();
                placeDAO.savePlace(place);
                return new ResponseEntity(place, new HttpHeaders(), HttpStatus.OK);
            }
            else{
                return new ResponseEntity(place, new HttpHeaders(), HttpStatus.CONFLICT);
            }
        }
    }

    @RequestMapping(value ="/place/taken", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Place> placeTaken(@RequestBody Position position ){

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        List<Place> listNearestPlace = placeDAO.findNearestPlaces(position.getLatitude(), position.getLongitude(), maxRadiusOfPlace);

        //If no nearest place found,create one
        if(listNearestPlace.isEmpty()){
            Place newPlace = new Place();
            newPlace.setLatitude(position.getLatitude()).setLongitude(position.getLongitude()).takePlace();
            placeDAO.savePlace(newPlace);

            return new ResponseEntity(newPlace, new HttpHeaders(), HttpStatus.OK);
        }
        else{ //else take the first: the nearest place
            //TODO Change the algo to manager better the taking of a place use
            Place place = listNearestPlace.get(0);
            if(place.isTaken()){
                return new ResponseEntity(null, new HttpHeaders(), HttpStatus.CONFLICT);
            }
            else{
                place.takePlace();
                placeDAO.savePlace(place);
                return new ResponseEntity(place, new HttpHeaders(), HttpStatus.OK);
            }
        }
    }


    @RequestMapping("/places")
    public List<Place> listPlacesByPosition(@RequestParam(value="latitude") double latitude,
                                            @RequestParam(value="longitude") double longitude,
                                            @RequestParam(value="radius", defaultValue="0") int radius){

        if(radius<1 || radius>maxRadius) radius = defaultRadius;

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        List<Place> listPlace = placeDAO.findPlacesByPosition(latitude, longitude, radius);
        System.out.println("Nb: " + listPlace.size());
        return listPlace;
    }
}