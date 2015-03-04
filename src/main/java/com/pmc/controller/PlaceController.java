package com.pmc.controller;

import com.pmc.model.Place;
import com.pmc.model.User;
import com.pmc.service.PlaceService;
import com.pmc.service.PlaceServiceException.PlaceAlreadyReleased;
import com.pmc.service.PlaceServiceException.PlaceAlreadyTaken;
import com.pmc.service.PlaceServiceException.PlaceNotFound;
import com.util.*;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * @return The place founded or null otherwise. Check response status for more details
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Place> place(@PathVariable("id") int id) {
        try{
            Place place=placeService.getPlaceById(id);
            HttpStatus status = HttpStatus.OK;
            if (place==null){
               status=HttpStatus.NOT_FOUND;
            }
            return new ResponseEntity(place, new HttpHeaders(), status);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<User> deletePlace(@PathVariable("id") int id) {
        try {
            placeService.deletePlaceById(id);
            User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new ResponseEntity(user, new HttpHeaders(), HttpStatus.OK);
        }catch (EmptyResultDataAccessException eNotFound){
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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