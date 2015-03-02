package com.pmc.controller;

import com.pmc.model.Place;
import com.pmc.service.PlaceService;
import com.pmc.service.PlaceServiceException.PlaceAlreadyReleased;
import com.pmc.service.PlaceServiceException.PlaceAlreadyTaken;
import com.pmc.service.PlaceServiceException.PlaceNotFound;
import com.util.*;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Stephane KI & Gaëtan DESHAYES
 */
@RestController
public class PlaceController {

    private static final int defaultRadius = 100;
    private static final int maxRadius = 1000;


    /**
     * Find a place by its id
     * @param id : id of the place to find
     * @return The place founded or false otherwise
     *
     */
    @RequestMapping("/place")
    public Place place(@RequestParam(value="id") int id) {
        return PlaceService.getInstance().getPlaceById(id);
    }

    @RequestMapping("/delete")
    public boolean deletePlace(@RequestParam(value="id") int id) {
        return PlaceService.getInstance().deletePlaceById(id);
    }



    @RequestMapping(value ="/place/released", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Place> placeReleased(@RequestBody Position position ){
        Place placeReleased = null;
        try {

            placeReleased = PlaceService.getInstance().releasePlace(position.getLatitude(), position.getLongitude());

            return new ResponseEntity(placeReleased, new HttpHeaders(), HttpStatus.OK);

        } catch (PlaceNotFound placeNotFound) {
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (PlaceAlreadyReleased placeAlreadyReleased) {
            return new ResponseEntity(placeReleased, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value ="/place/taken", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Place> placeTaken(@RequestBody Position position ){
        Place placeTaken = null;
        try {

            placeTaken = PlaceService.getInstance().takePlace(position.getLatitude(), position.getLongitude());

            return new ResponseEntity(placeTaken, new HttpHeaders(), HttpStatus.OK);

        } catch (PlaceAlreadyTaken placeAlreadyTaken) {
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping("/places")
    public List<Place> listPlacesByPosition(@RequestParam(value="latitude") double latitude,
                                            @RequestParam(value="longitude") double longitude,
                                            @RequestParam(value="radius", defaultValue="0") int radius){

        if(radius<1 || radius>maxRadius) radius = defaultRadius;

        return PlaceService.getInstance().listPlacesByPosition(latitude, longitude, radius);
    }
}