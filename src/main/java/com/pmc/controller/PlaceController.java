package com.pmc.controller;

import com.pmc.model.Place;
import com.pmc.model.User;
import com.pmc.service.PlaceService;
import com.pmc.service.PlaceServiceException.PlaceAlreadyReleased;
import com.pmc.service.PlaceServiceException.PlaceAlreadyTaken;
import com.pmc.service.PlaceServiceException.PlaceNotFound;
import com.pmc.service.PlaceServiceException.PlaceNotUsedByUser;
import com.util.Message4Client;
import com.util.Position;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Default controller for url concerning Places
 * @author stephaneki
 */
@RestController
@RequestMapping("rest/places")
public class PlaceController {

    /**
     * The default radius of the circle in which places has to be founded.
     * Its used when the user does not specify his own radius.
     */
    private static final int defaultRadius = 100;

    /**
     * To prevent client to ask for a too large perimeter this radius is the maximum.
     * We are using it when the specified radius is greater than this value.
     */
    private static final int maxRadius = 1000;

    @Resource
    private PlaceService placeService;


    /**
     * Find a place by its id
     * @param id : id of the place to find
     * @return The place founded or null otherwise. Check response status for more details.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Place> findPlaceById(@PathVariable("id") int id) {
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
            e.printStackTrace();
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    /**
     * Delete a place knowing its id
     * @param id : id of the place to delete
     * @return A message if the place have been deleted or null otherwise. Client should check response status
     * for more details on what happened
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Message4Client> deletePlaceById(@PathVariable("id") int id) {
        try {
            placeService.deletePlaceById(id);
            User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String message= "Place <"+id+"> deleted by <"+user.getUsername()+">";
            return new ResponseEntity(new Message4Client(message), new HttpHeaders(), HttpStatus.OK);
        }catch (EmptyResultDataAccessException eNotFound){
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Declare to the community that we are releasing a place by specifying our position.
     *
     * @param position Objected created using data send by the client.
     *                 These data contains latitude and longitude information.
     *
     * @return The place that has been released or null if the place has been founded.
     * If the place has already been released then a conflict status is sent.
     */
    @RequestMapping(value ="/released", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Place> placeReleased(@RequestBody Position position ){
        Place placeReleased = null;
        try {
            User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            placeReleased = placeService.releasePlace(position.getLatitude(), position.getLongitude(),user);
            return new ResponseEntity(placeReleased, new HttpHeaders(), HttpStatus.OK);

        } catch (PlaceNotFound placeNotFound) {
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (PlaceNotUsedByUser placeNotUsedByUser) {
            return new ResponseEntity(placeReleased, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (PlaceAlreadyReleased placeAlreadyReleased) {
            return new ResponseEntity(placeReleased, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Declare that the client is taking a place by specifying its position.
     *
     * @param position Objected created using data send by the client.
     *                 These data contains latitude and longitude information.
     * @return The place that has been taken or null otherwise.
     */
    @RequestMapping(value ="/taken", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Place> placeTaken(@RequestBody Position position ){
        Place placeTaken = null;
        try {
            User user =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            placeTaken = placeService.takePlace(position.getLatitude(), position.getLongitude(), user);
            return new ResponseEntity(placeTaken, new HttpHeaders(), HttpStatus.OK);

        } catch (PlaceAlreadyTaken placeAlreadyTaken) {
            return new ResponseEntity(placeTaken, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * List all the places founded free in a specified circle.
     * @param latitude latitude of position specified.
     * @param longitude longitude of the position specified.
     * @param radius radius of the circle in which we are looking for places.
     * @return A list of all the places founded.
     */
    @RequestMapping("")
    public List<Place> listPlacesByPosition(@RequestParam(value="latitude") double latitude,
                                            @RequestParam(value="longitude") double longitude,
                                            @RequestParam(value="radius", defaultValue="0") int radius){

        if(radius<1 || radius>maxRadius) radius = defaultRadius;

        return placeService.listPlacesByPosition(latitude, longitude, radius);
    }
}