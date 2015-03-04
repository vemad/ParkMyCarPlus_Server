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

import javax.annotation.Resource;


@RestController
@RequestMapping("rest/places")
public class PlaceController {

    private static final int defaultRadius = 100;
    private static final int maxRadius = 1000;

    @Resource
    private PlaceService placeService;


    /**
     * Find a place by its id
     * @param id : id of the place to find
     * @return The place founded or false otherwise
     *
     */
    @RequestMapping("/{id}")
    public Place place(@PathVariable("id") int id) {
        return placeService.getPlaceById(id);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deletePlace(@PathVariable("id") int id) {
        placeService.deletePlaceById(id);
    }



    @RequestMapping(value ="/released", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Place> placeReleased(@RequestBody Position position ){
        Place placeReleased = null;
        try {

            placeReleased = placeService.releasePlace(position.getLatitude(), position.getLongitude());

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

    @RequestMapping(value ="/taken", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Place> placeTaken(@RequestBody Position position ){
        Place placeTaken = null;
        try {

            placeTaken = placeService.takePlace(position.getLatitude(), position.getLongitude());

            return new ResponseEntity(placeTaken, new HttpHeaders(), HttpStatus.OK);

        } catch (PlaceAlreadyTaken placeAlreadyTaken) {
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping("")
    public List<Place> listPlacesByPosition(@RequestParam(value="latitude") double latitude,
                                            @RequestParam(value="longitude") double longitude,
                                            @RequestParam(value="radius", defaultValue="0") int radius){

        if(radius<1 || radius>maxRadius) radius = defaultRadius;

        return placeService.listPlacesByPosition(latitude, longitude, radius);
    }
}