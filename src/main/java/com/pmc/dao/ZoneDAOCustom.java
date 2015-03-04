package com.pmc.dao;

import com.pmc.model.Zone;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by stephaneki on 04/03/15.
 */
public interface ZoneDAOCustom {
    public List<Zone> getZones(double latitude, double longitude, DateTime date, int radius);
}
