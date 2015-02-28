package com;

import com.pmc.config.DAOManager;
import com.pmc.dao.PlaceDAO;
import com.pmc.model.Place;
import com.util.Message4Client;
import com.util.Position;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author gaetandeshayes
 */
@RestController
public class PlaceController {

    private static final int defaultRadius = 100;
    private static final int maxRadius = 1000;

    /**
     * Route mapping to retrieve a position knowing its id.
     * @param id The id of the place to be found
     * @return The place founded or null otherwise
     */
    @RequestMapping("/place")
    public Place place(@RequestParam(value="id") int id) {

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        return new Place(); //TODO Do not keep this
        //return placeDAO.findById(id);
    }

    /**
     *
     * @param body
     * @return
     */
    @RequestMapping(value ="/place/released", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Message4Client> placeReleased(@RequestBody Position body ){

        //TODO Implement this method
        return new ResponseEntity(new Message4Client("OK"), new HttpHeaders(), HttpStatus.OK);
    }

    /**
     *
     * @param body
     * @return
     */
    @RequestMapping(value ="/place/taken", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Message4Client> placeTaken(@RequestBody Position body ){

        //TODO Implement this method
        return new ResponseEntity(new Message4Client("OK"), new HttpHeaders(), HttpStatus.OK);
    }


    /**
     * Route mapping to collect all the places around the position of teh driver. These places have to
     * be founded in a certain circle represented by the radius.
     * @param longitude longitude of the driver position
     * @param latitude latitude of the driver position
     * @param radius radius of the circle zone in which places have to be found
     * @return List of all the places in a circle knowing the circle center and the radius.
     */
    @RequestMapping("/places")
    public List<Place> listPlacesByPosition(@RequestParam(value="longitude") double longitude,
                                            @RequestParam(value="latitude") double latitude,
                                            @RequestParam(value="radius", defaultValue="0")
                                            int radius){

        if(radius<1 || radius>maxRadius) radius = defaultRadius;

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        return placeDAO.findPlacesByPosition(longitude, latitude, radius);
    }
}