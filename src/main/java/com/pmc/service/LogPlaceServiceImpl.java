package com.pmc.service;

import com.pmc.dao.LogPlaceDAO;
import com.pmc.model.LogPlace;
import com.pmc.model.Place;
import com.pmc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Gaetan on 02/03/2015.
 */
@Transactional
@Service(value = "logPlaceService")
public class LogPlaceServiceImpl implements LogPlaceService {

    @Autowired
    private LogPlaceDAO logPlaceDAO;

    public void logPlaceTaken(Place place, User user, double latitude, double longitude){
        this.saveLogPlace(new LogPlace(place, LogPlace.Action.take,user, latitude, longitude));
    }

    public void logPlaceReleased(Place place, User user, double latitude, double longitude){
        this.saveLogPlace(new LogPlace(place, LogPlace.Action.release,user, latitude, longitude));
    }

    public void logPlaceCreated(Place place, User user, double latitude, double longitude){
        this.saveLogPlace(new LogPlace(place, LogPlace.Action.create,user, latitude, longitude));
    }

    public void saveLogPlace(LogPlace logPlace){
        logPlaceDAO.save(logPlace);
    }
}
