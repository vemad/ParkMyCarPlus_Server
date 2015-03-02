package com.pmc.dao;

import com.pmc.model.LogPlace;

/**
 * Created by Gaetan on 02/03/2015.
 */
public interface LogPlaceDAO extends DAO {

    public LogPlace saveLogPlace(LogPlace place);
}
