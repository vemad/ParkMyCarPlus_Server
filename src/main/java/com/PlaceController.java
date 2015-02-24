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

@RestController
public class PlaceController {

    private static final int defaultRadius = 100;
    private static final int maxRadius = 1000;

    @RequestMapping("/place")
    public Place place(@RequestParam(value="id") int id) {

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);

        return placeDAO.findById(id);
    }

    @RequestMapping(value ="/place/released", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Message4Client> placeReleased(@RequestBody Position body ){

        //TODO Implement this method
        return new ResponseEntity(new Message4Client("OK"), new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value ="/place/taken", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Message4Client> placeTaken(@RequestBody Position body ){

        //TODO Implement this method
        return new ResponseEntity(new Message4Client("OK"), new HttpHeaders(), HttpStatus.OK);
    }


    @RequestMapping("/places")
    public List<Place> listPlacesByPosition(@RequestParam(value="longitude") double longitude,
                                            @RequestParam(value="latitude") double latitude,
                                            @RequestParam(value="radius", defaultValue="0") int radius){

        if(radius<1 || radius>maxRadius) radius = defaultRadius;

        PlaceDAO placeDAO = (PlaceDAO)DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.PLACE);
        return placeDAO.findPlacesByPosition(longitude, latitude, radius);
    }
}