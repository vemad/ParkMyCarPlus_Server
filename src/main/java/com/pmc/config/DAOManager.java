package com.pmc.config;

import com.pmc.dao.DAO;
import com.pmc.dao.LogPlaceDAO;
import com.pmc.dao.PlaceDAO;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Gaetan on 23/02/2015.
 */
public class DAOManager {
    private static DAOManager myDAO = new DAOManager();
    private AnnotationConfigApplicationContext ctx;

    public enum TypeDAO {
        PLACE,
        LOG_PLACE
    }

    private DAOManager() {
        this.ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
    }

    public static DAOManager getDAOManager(){
        return myDAO;
    }

    public DAO getDao(TypeDAO dao){
        switch(dao){
            case PLACE:
               return ctx.getBean(PlaceDAO.class);
            case LOG_PLACE:
                return ctx.getBean(LogPlaceDAO.class);
            default:
                return null;
        }
    }

}
