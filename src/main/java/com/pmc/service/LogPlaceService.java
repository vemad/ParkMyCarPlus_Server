package com.pmc.service;

import com.pmc.config.DAOManager;
import com.pmc.dao.LogPlaceDAO;
import com.pmc.model.LogPlace;
import com.pmc.model.Place;

/**
 * Created by Gaetan on 02/03/2015.
 */
public class LogPlaceService {
    /*
    * LogPlaceService is a singleton
    */
    private static LogPlaceService logPlaceService = new LogPlaceService();

    public static LogPlaceService getInstance(){
        return logPlaceService;
    }

    private LogPlaceService(){}

    public void logPlaceTaken(Place place, double latitude, double longitude){
        this.saveLogPlace(new LogPlace(place, LogPlace.Action.take, latitude, longitude));
    }

    public void logPlaceReleased(Place place, double latitude, double longitude){
        this.saveLogPlace(new LogPlace(place, LogPlace.Action.release, latitude, longitude));
    }

    public void logPlaceCreated(Place place, double latitude, double longitude){
        this.saveLogPlace(new LogPlace(place, LogPlace.Action.create, latitude, longitude));
    }

    public void saveLogPlace(LogPlace logPlace){
        LogPlaceDAO logPlaceDAO = (LogPlaceDAO) DAOManager.getDAOManager().getDao(DAOManager.TypeDAO.LOG_PLACE);
        logPlaceDAO.saveLogPlace(logPlace);
    }
}
